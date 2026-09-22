package com.saucedemo.selenium.testng.demo;

import com.saucelabs.saucebindings.options.SauceOptions;
import com.saucelabs.saucebindings.testng.SauceBaseTest;
import java.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Example Test for running with the TestNG Sauce Bindings library. */
public class SauceBindingsTestngTest extends SauceBaseTest {
  @Override
  protected SauceOptions createSauceOptions() {
    SauceOptions sauceOptions = super.createSauceOptions();
    sauceOptions.sauce().setTags(Arrays.asList("selenium", "testng", "java", "login"));
    return sauceOptions;
  }

  @Test(groups = {"selenium", "testng", "java", "login"})
  public void correctTitle() {

    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }
}
