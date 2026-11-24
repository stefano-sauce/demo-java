package com.saucedemo.tests;

import com.saucedemo.SauceTags;
import com.saucedemo.pages.LoginPage;
import com.saucelabs.saucebindings.DataCenter;
import com.saucelabs.saucebindings.junit4.SauceBaseTest;
import com.saucelabs.saucebindings.options.SauceOptions;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/** Performance Tests. */
public class PerformanceTests extends SauceBaseTest {
  // Sauce Bindings default to US West; this org runs everything in EU Central.
  @Override
  public DataCenter getDataCenter() {
    return DataCenter.EU_CENTRAL;
  }

  @Override
  public SauceOptions createSauceOptions() {
    return SauceTags.apply(
        SauceOptions.chrome()
            .setExtendedDebugging()
            .setName(testName.getMethodName())
            .setCapturePerformance()
            .setTags(Arrays.asList("selenium", "junit4", "java", "performance"))
            .build());
  }

  @Test
  public void performanceDidntDegrade() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.visit();

    Assert.assertTrue(loginPage.getPageLoadTime() < 2000);
  }
}
