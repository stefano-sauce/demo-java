package com.saucedemo;

import io.appium.java_client.AppiumDriver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Mobile Tests extend this class to ensure the correct driver. */
public abstract class MobileTestsBase extends AbstractTestBase {
  /**
   * This casts RemoteWebDriver to AppiumDriver for mobile tests.
   *
   * @return instance of Appium Driver
   */
  public AppiumDriver getDriver() {
    return (AppiumDriver) driver;
  }

  /**
   * Base Sauce Labs tags shared by all mobile tests extending this class. Subclasses override
   * this to append their own use-case tag.
   */
  protected List<String> sauceTags() {
    return new ArrayList<>(Arrays.asList("appium", "junit4", "java"));
  }
}
