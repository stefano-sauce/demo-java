package com.saucedemo.selenium.selenium_features;

import com.saucedemo.selenium.TestBase;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("timeouts")
public class TimeoutsTest extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("timeouts");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) {
    startChromeSession(testInfo);
  }

  @Test
  public void getTimoutValues() {
    WebDriver.Timeouts timeouts = driver.manage().timeouts();

    timeouts.pageLoadTimeout(Duration.ofSeconds(33));
    timeouts.implicitlyWait(Duration.ofMillis(333));
    timeouts.scriptTimeout(Duration.ofSeconds(33));
    timeouts.getPageLoadTimeout();
    // These getters do not exist in Selenium 3
    Assertions.assertEquals(Duration.ofSeconds(33), timeouts.getPageLoadTimeout());
    Assertions.assertEquals(Duration.ofMillis(333), timeouts.getImplicitWaitTimeout());
    Assertions.assertEquals(Duration.ofSeconds(33), timeouts.getScriptTimeout());
  }
}
