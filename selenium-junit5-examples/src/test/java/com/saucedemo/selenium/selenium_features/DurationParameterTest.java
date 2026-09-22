package com.saucedemo.selenium.selenium_features;

import com.saucedemo.selenium.TestBase;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.support.ui.WebDriverWait;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("duration_parameter")
public class DurationParameterTest extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("duration_parameter");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) {
    startChromeSession(testInfo);
  }

  @DisplayName("Timeout integers still work but are deprecated")
  @Test
  public void timeoutIntegersDeprecated() {

    // Uses Seconds
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    // Uses Long / TimeUnit
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(555));
  }

  @DisplayName("Timeouts now use Duration instances")
  @Test
  public void timeoutUnitsDeprecated() {

    // Uses Seconds
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    // Uses Long / TimeoutUnit
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(555));
  }
}
