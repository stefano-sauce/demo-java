package com.saucedemo.selenium.testng.demo;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/** Example of running a TestNG test without using Sauce Bindings. */
public class SeleniumTest {

  protected RemoteWebDriver driver;

  @BeforeMethod
  public void setup(Method method) throws MalformedURLException {
    MutableCapabilities sauceOptions = new MutableCapabilities();
    sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
    sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
    sauceOptions.setCapability("name", method.getName());
    sauceOptions.setCapability("browserVersion", "latest");
    sauceOptions.setCapability("tags", Arrays.asList("selenium", "testng", "java", "login"));

    ChromeOptions options = new ChromeOptions();
    options.setCapability("sauce:options", sauceOptions);
    URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");

    driver = new RemoteWebDriver(url, options);
  }

  @Test(groups = {"selenium", "testng", "java", "login"})
  public void correctTitle() {
    driver.navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", driver.getTitle());
  }

  @AfterMethod
  public void teardown(ITestResult result) {
    String status = result.isSuccess() ? "passed" : "failed";
    driver.executeScript("sauce:job-result=" + status);
  }
}
