package com.saucedemo.selenium.sauce_features;

import com.saucedemo.selenium.TestBase;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Demonstrates Backtrace error/crash reporting for web, on top of Sauce Labs, the same way
 * BacktraceCrashActivity does it for the Android app (see mobile My Demo App Android).
 *
 * <p>The demo page ({@code backtrace-web-demo/index.html}) loads the Backtrace browser SDK
 * (@backtrace/browser) and wires up automatic capture of uncaught errors and unhandled promise
 * rejections, plus a manual report button.
 *
 * <p>Because the page only exists on this machine (it's a test resource, not a public site), it's
 * served by a tiny embedded HTTP server and exposed to the Sauce Labs cloud browser through a
 * Sauce Connect tunnel - see the "Backtrace Web Crash/Error Reporting Demo" section in
 * ~/scripts/run_sauce_tests.sh, which starts the tunnel (name: SAUCE_TUNNEL_NAME, defaulting to
 * "backtrace-web-demo") before running this test class.
 *
 * <p>The hostname "127.0.0.1.nip.io" (a public DNS wildcard that resolves to 127.0.0.1) is used
 * instead of "localhost" - browsers special-case literal "localhost" and won't route it through
 * an HTTP proxy such as the Sauce Connect tunnel, so a different hostname that still resolves to
 * the loopback address is required. See:
 * https://docs.saucelabs.com/secure-connections/sauce-connect-5/guides/testing-localhost/
 */
@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("crash_reporting")
public class BacktraceWebCrashReportingTest extends TestBase {

  private static final String TUNNEL_NAME =
      System.getenv().getOrDefault("SAUCE_TUNNEL_NAME", "backtrace-web-demo");
  private static final String HOSTNAME = "127.0.0.1.nip.io";
  private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(20);

  private static HttpServer server;
  private static String baseUrl;

  @Override
  protected List<String> sauceTags() {
    List<String> tags = new ArrayList<>(super.sauceTags());
    tags.add("crash_reporting");
    return tags;
  }

  @BeforeAll
  public static void startLocalServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress("0.0.0.0", 0), 0);
    server.createContext(
        "/",
        exchange -> {
          byte[] body;
          try (InputStream resource =
              BacktraceWebCrashReportingTest.class
                  .getClassLoader()
                  .getResourceAsStream("backtrace-web-demo/index.html")) {
            if (resource == null) {
              exchange.sendResponseHeaders(404, -1);
              return;
            }
            body = resource.readAllBytes();
          }
          exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
          exchange.sendResponseHeaders(200, body.length);
          try (OutputStream responseBody = exchange.getResponseBody()) {
            responseBody.write(body);
          }
        });
    server.start();

    baseUrl = String.format("http://%s:%d/", HOSTNAME, server.getAddress().getPort());
    System.out.println("Backtrace web demo page served at: " + baseUrl);
  }

  @AfterAll
  public static void stopLocalServer() {
    if (server != null) {
      server.stop(0);
    }
  }

  @BeforeEach
  public void setup(TestInfo testInfo) {
    ChromeOptions options = new ChromeOptions();
    Map<String, Object> sauceOptions = defaultSauceOptions(testInfo);
    sauceOptions.put("tunnelName", TUNNEL_NAME);

    // extendedDebugging captures HAR (network) files and browser console logs - this is what
    // surfaces the uncaught JS error/crash in the Sauce Labs job's Logs tab, the web equivalent
    // of the device crash log captured for the Android app.
    // capturePerformance was tried and dropped: it consistently errored with "Navigation event
    // unexpectedly had no navigation ID" against this locally-tunneled page (see conversation
    // history / job be16d86d.../0779640...), most likely because Lighthouse-based performance
    // tracing doesn't cope well with a Sauce Connect-tunneled, non-public origin.
    // https://docs.saucelabs.com/dev/test-configuration-options/
    sauceOptions.put("extendedDebugging", true);

    startSession(options, sauceOptions);
  }

  private void waitForClientReady() {
    driver.get(baseUrl);
    new WebDriverWait(driver, WAIT_TIMEOUT)
        .until(d -> "ready".equals(d.findElement(By.id("status")).getDomAttribute("data-status")));
  }

  private void waitForLogEntryContaining(String expectedSubstring) {
    new WebDriverWait(driver, WAIT_TIMEOUT)
        .until(
            d ->
                d.findElements(By.cssSelector("#log li")).stream()
                    .anyMatch(li -> li.getText().contains(expectedSubstring)));
  }

  @DisplayName("Uncaught JS error is auto-captured and reported to Backtrace")
  @Test
  public void uncaughtErrorIsReportedToBacktrace() {
    waitForClientReady();

    driver.findElement(By.id("btn-uncaught")).click();

    waitForLogEntryContaining("after-send:");
    Assertions.assertTrue(
        driver.findElements(By.cssSelector("#log li")).stream()
            .anyMatch(li -> li.getText().contains("throwing uncaught error")),
        "Expected the page to log that it threw the uncaught error");
  }

  @DisplayName("Unhandled promise rejection is auto-captured and reported to Backtrace")
  @Test
  public void unhandledRejectionIsReportedToBacktrace() {
    waitForClientReady();

    driver.findElement(By.id("btn-rejection")).click();

    waitForLogEntryContaining("after-send:");
    Assertions.assertTrue(
        driver.findElements(By.cssSelector("#log li")).stream()
            .anyMatch(li -> li.getText().contains("triggering unhandled promise rejection")),
        "Expected the page to log that it triggered the unhandled rejection");
  }

  @DisplayName("Manual error report is sent to Backtrace on demand")
  @Test
  public void manualReportIsSentToBacktrace() {
    waitForClientReady();

    driver.findElement(By.id("btn-manual")).click();

    waitForLogEntryContaining("manual report result:");
  }
}
