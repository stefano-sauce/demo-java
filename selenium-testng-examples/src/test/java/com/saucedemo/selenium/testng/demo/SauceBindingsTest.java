package com.saucedemo.selenium.testng.demo;

import com.saucedemo.selenium.testng.SauceTags;
import com.saucelabs.saucebindings.DataCenter;
import com.saucelabs.saucebindings.SauceSession;
import com.saucelabs.saucebindings.options.SauceOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

/** Example Tests for running with Sauce Bindings directly without test runner jar. */
public class SauceBindingsTest {
  protected SauceSession session;
  protected RemoteWebDriver driver;

  @BeforeMethod
  public void setup(Method method) {
    SauceOptions options =
        SauceOptions.chrome()
            .setName(method.getName())
            .setTags(Arrays.asList("selenium", "testng", "java", "login"))
            .build();
    session = new SauceSession(SauceTags.apply(options));
    session.setDataCenter(DataCenter.EU_CENTRAL);
    driver = session.start();
  }

  @Test(groups = {"selenium", "testng", "java", "login"})
  public void correctTitle() {
    driver.navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", driver.getTitle());
  }

  @AfterMethod
  public void teardown(ITestResult result) {
    session.stop(result.isSuccess());
  }
}
