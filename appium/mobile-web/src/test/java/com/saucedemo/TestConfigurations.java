package com.saucedemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

public class TestConfigurations {
  static final String SAUCE_CLOUD = System.getProperty("sauce.cloud", "vdc");
  static final String SAUCE_PLATFORM = System.getProperty("sauce.browser", "chrome");
  static final String BUILD_TIME = String.valueOf(System.currentTimeMillis());

  // Best practice would put these constants in a config file and dynamically pull them.
  public static Capabilities getCapabilities(TestInfo testInfo, List<String> tags) {
    if (SAUCE_PLATFORM.equalsIgnoreCase("chrome")) {
      if (SAUCE_CLOUD.equalsIgnoreCase("vdc")) {
        return androidVDC(testInfo, tags);
      } else if (SAUCE_CLOUD.equalsIgnoreCase("rdc")) {
        return androidRDC(testInfo, tags);
      }
    }

    if (SAUCE_PLATFORM.equalsIgnoreCase("safari")) {
      if (SAUCE_CLOUD.equalsIgnoreCase("vdc")) {
        return iosVDC(testInfo, tags);
      } else if (SAUCE_CLOUD.equalsIgnoreCase("rdc")) {
        return iosRDC(testInfo, tags);
      }
    }

    throw new RuntimeException("Invalid platform/cloud combination. Browser must be chrome or safari and  cloud must be vdc or rdc.");
  }

  private static Capabilities androidVDC(TestInfo testInfo, List<String> tags) {
    Map<String, Object> caps = new HashMap<>();
    caps.put("platformName", "Android");
    caps.put("browserName", "Chrome");
    caps.put("appium:deviceName", "Android GoogleAPI Emulator");
    caps.put("appium:platformVersion", "current_major");
    caps.put("appium:automationName", "UiAutomator2");

    Map<String, Object> sauceOptions = new HashMap<>();
    sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
    sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
    sauceOptions.put("name", testInfo.getDisplayName());
    sauceOptions.put("build", "Android Web VDC: " + BUILD_TIME);
    sauceOptions.put("tags", tags);
    caps.put("sauce:options", sauceOptions);

    return new MutableCapabilities(caps);
  }

  private static Capabilities androidRDC(TestInfo testInfo, List<String> tags) {
    Map<String, Object> caps = new HashMap<>();
    caps.put("platformName", "Android");
    caps.put("browserName", "Chrome");
    caps.put("appium:deviceName", "Google.*");
    caps.put("appium:automationName", "UiAutomator2");

    Map<String, Object> sauceOptions = new HashMap<>();
    sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
    sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
    sauceOptions.put("name", testInfo.getDisplayName());
    sauceOptions.put("build", "Android Web RDC: " + BUILD_TIME);
    sauceOptions.put("appiumVersion", "latest");
    sauceOptions.put("resigningEnabled", true);
    sauceOptions.put("networkCapture", true);
    sauceOptions.put("vitals", true);
    sauceOptions.put("tags", tags);
    caps.put("sauce:options", sauceOptions);

    return new MutableCapabilities(caps);
  }

  private static Capabilities iosVDC(TestInfo testInfo, List<String> tags) {
    Map<String, Object> caps = new HashMap<>();
    caps.put("platformName", "iOS");
    caps.put("browserName", "Safari");
    caps.put("appium:deviceName", "iPhone Simulator");
    caps.put("appium:platformVersion", "current_major");
    caps.put("appium:automationName", "XCUITest");

    Map<String, Object> sauceOptions = new HashMap<>();
    sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
    sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
    sauceOptions.put("name", testInfo.getDisplayName());
    sauceOptions.put("build", "iOS Web VDC: " + BUILD_TIME);
    sauceOptions.put("tags", tags);
    caps.put("sauce:options", sauceOptions);

    return new MutableCapabilities(caps);
  }

  private static Capabilities iosRDC(TestInfo testInfo, List<String> tags) {
    Map<String, Object> caps = new HashMap<>();
    caps.put("platformName", "iOS");
    caps.put("browserName", "Safari");
    caps.put("appium:deviceName", "iPhone.*");
    caps.put("appium:automationName", "XCUITest");

    Map<String, Object> sauceOptions = new HashMap<>();
    sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
    sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
    sauceOptions.put("appiumVersion", "latest");
    sauceOptions.put("name", testInfo.getDisplayName());
    sauceOptions.put("build", "iOS Web RDC: " + BUILD_TIME);
    sauceOptions.put("resigningEnabled", true);
    sauceOptions.put("networkCapture", true);
    sauceOptions.put("vitals", true);
    sauceOptions.put("tags", tags);
    caps.put("sauce:options", sauceOptions);

    return new MutableCapabilities(caps);
  }
}
