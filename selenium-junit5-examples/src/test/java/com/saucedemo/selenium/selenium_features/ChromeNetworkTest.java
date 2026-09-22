package com.saucedemo.selenium.selenium_features;

import com.saucedemo.selenium.TestBase;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chromium.ChromiumNetworkConditions;
import org.openqa.selenium.chromium.HasNetworkConditions;
import org.openqa.selenium.remote.Augmenter;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("network_interception")
public class ChromeNetworkTest extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("network_interception");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) {
    startChromeSession(testInfo);
  }

  @Test
  public void toggleOffline() {
    WebDriver augmentedDriver = new Augmenter().augment(driver);
    ChromiumNetworkConditions networkConditions = new ChromiumNetworkConditions();
    networkConditions.setOffline(true);
    ((HasNetworkConditions) augmentedDriver).setNetworkConditions(networkConditions);

    try {
      driver.get("https://www.saucedemo.com");
      Assertions.fail(
          "If Network is set to be offline, the previous line should throw an exception");
    } catch (WebDriverException ex) {
      ((HasNetworkConditions) augmentedDriver)
          .setNetworkConditions(new ChromiumNetworkConditions());
    }
    driver.get("https://www.saucedemo.com");
  }
}
