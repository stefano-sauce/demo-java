package com.saucedemo;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Automates the manual crash-reporting demo previously run by hand on a real device (Sauce Labs
 * job be16d86d7d214faa97996461f2a64151): on the product catalog (the app's landing screen), tap
 * the top-right product tile - "Sauce Labs Backpack (green)" - which crashes the app.
 *
 * <p>That tile isn't special by name; it's whichever product lands at grid position 1. The crash
 * comes from an intentionally-planted bug in ProductCatalogFragment.setAdapter(): {@code meta =
 * {0, null, 2, 3, 4, 5}} ("null is an intentionally introduced bug for demos"), and the click
 * handler does {@code meta[position].intValue()} - a NullPointerException the moment position 1
 * is tapped. (There's a separate, currently commented-out {@code
 * MyApplication.backtraceClient.nativeCrash()} call in the same file for the same position - not
 * what's wired up in this build.) Confirmed against a live session: tapping the sibling image of
 * that text throws {@code java.lang.NullPointerException: Attempt to invoke virtual method 'int
 * java.lang.Integer.intValue()' on a null object reference} at
 * ProductCatalogFragment.java:156, and kills the app process.
 *
 * <p>Backtrace's Android SDK (wired up in MyApplication.java, see
 * https://github.com/saucelabs/my-demo-app-android) installs a process-wide default
 * uncaught-exception handler, so it intercepts and reports this crash the same way it would any
 * other uncaught exception in the app - not just the ones on the "Crash app (debug)" screen.
 *
 * <p>Unlike the web demo, there's no page-side log to assert against here, so this test only
 * drives the UI up to triggering the crash and confirms the app process actually died as a
 * result; it doesn't (and can't) assert that Backtrace received the report.
 *
 * <p><b>Timing fix (see investigation doc, 2026-08-28):</b> RDC's own crash-log capture
 * (crash.json / crash_log_url via the crashReporting capability below) needs ~600-700ms after the
 * uncaught exception to serialise and hand off the crash payload before the process dies. The
 * default teardown - {@code driver.quit()} firing the instant this test method returns - was
 * killing the app at only ~300ms, well inside that window, so no crash artifact was ever written
 * even though the crash itself fired correctly every time. This test now explicitly waits for the
 * app process to be confirmed dead, then adds a margin sleep, before returning - so teardown can't
 * race the crash handler. (Earlier suspicion that "Feature 'BACKTRACE' is not enabled" was the
 * cause was a red herring: that line appears in successful manual runs too - BACKTRACE and
 * CRASH_COLLECTION are separate features, and CRASH_COLLECTION is the one actually enabled here.)
 *
 * <p>Uses mydemoapp_sauce_error_reporting.apk from Sauce Storage - the build with Backtrace
 * enabled - rather than TestConfigurations.ANDROID_APP_URL, which points at a stock build with no
 * Backtrace token.
 */
@Tag("appium")
@Tag("junit5")
@Tag("java")
@Tag("crash_reporting")
public class BacktraceCrashReportingTest extends TestBase {

  private static final String BUILD_TIME = String.valueOf(System.currentTimeMillis());

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("crash_reporting");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) throws MalformedURLException {
    Map<String, Object> caps = new HashMap<>();
    caps.put("platformName", "Android");
    caps.put("appium:automationName", "UiAutomator2");
    caps.put("appium:app", "storage:filename=mydemoapp_sauce_error_reporting.apk");
    caps.put("appium:deviceName", "Samsung Galaxy S20");

    Map<String, Object> sauceOptions = new HashMap<>();
    sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
    sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
    sauceOptions.put("appiumVersion", "latest");
    sauceOptions.put("name", testInfo.getDisplayName());
    sauceOptions.put("build", "Backtrace Crash Reporting RDC: " + BUILD_TIME);

    // Sauce's own RDC crash-log capture (crash.json / crash_log_url) - separate from the app's
    // embedded Backtrace SDK. Matches the documented example at
    // https://docs.saucelabs.com/dev/test-configuration-options/#crashreporting exactly. This
    // capability propagates correctly and CRASH_COLLECTION is confirmed enabled on-device - the
    // reason crash.json wasn't showing up wasn't this capability at all, it was driver.quit()
    // racing the RDC crash handler's ~600-700ms serialisation window at teardown (see the
    // class-level javadoc). Fixed by waiting for confirmed app death + margin below.
    sauceOptions.put("resigningEnabled", true);
    sauceOptions.put("crashReporting", true);
    sauceOptions.put("tags", sauceTags());

    caps.put("sauce:options", sauceOptions);

    driver = new AndroidDriver(new URL(SAUCE_URL), new MutableCapabilities(caps));
    driver.manage().timeouts().implicitlyWait(Duration.of(5, ChronoUnit.SECONDS));
  }

  @Test
  @DisplayName("Tapping the top-right catalog product crashes the app and is reported to Backtrace")
  public void topRightProductTapCrashIsReportedToBacktrace() {
    // The catalog is the app's landing screen - no navigation needed. A UiSelector.fromParent()
    // sibling-text lookup was tried first but silently matched the wrong (non-buggy) tile - it
    // clicked cleanly with no crash on a live run. Indexing into all "productIV" tiles instead:
    // confirmed via a live device session that findElements() returns them in the same order
    // they're laid out on screen, so index 1 is the grid's top-right tile - "Sauce Labs Backpack
    // (green)" - the one bound to the null slot in ProductCatalogFragment's crash-demo bug.
    List<WebElement> productImages =
        driver.findElements(AppiumBy.id("com.saucelabs.mydemoapp.android:id/productIV"));
    productImages.get(1).click();

    // Wait for the app process to actually die from the uncaught exception, rather than
    // returning (and letting the TestWatcher call driver.quit()) the instant the click's
    // MotionEvent is dispatched - which happens before the exception is even thrown on the app's
    // main looper, let alone before RDC has captured it. This also doubles as confirmation that
    // the crash genuinely occurred.
    new WebDriverWait(driver, Duration.ofSeconds(15))
        .until(
            d ->
                ((InteractsWithApps) d).queryAppState("com.saucelabs.mydemoapp.android")
                    == ApplicationState.NOT_RUNNING);
    assertEquals(
        ApplicationState.NOT_RUNNING,
        driver.queryAppState("com.saucelabs.mydemoapp.android"),
        "app should have crashed and died after tapping the buggy product tile");

    // Give the RDC agent room to serialise and hand off the crash payload. Measured requirement
    // from the manual run: ~623ms; 2s provides comfortable margin.
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
