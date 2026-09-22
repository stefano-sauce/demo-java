package com.saucedemo.selenium.testng;

import com.saucelabs.saucebindings.options.SauceOptions;
import com.saucelabs.saucebindings.testng.SauceBaseTest;
import java.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Tests for running a single browser in parallel. */
public class ParallelSingleBrowserTest extends SauceBaseTest {
  private static final String[] TAGS = {"selenium", "testng", "java", "parallel_execution"};

  @Override
  protected SauceOptions createSauceOptions() {
    SauceOptions sauceOptions = super.createSauceOptions();
    sauceOptions.sauce().setTags(Arrays.asList(TAGS));
    return sauceOptions;
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase1() {
    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase2() {

    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase3() {
    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase4() {
    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase5() {
    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase6() {
    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase7() {
    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase8() {
    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase9() {
    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }

  @Test(groups = {"selenium", "testng", "java", "parallel_execution"})
  public void testCase10() {
    getDriver().navigate().to("https://www.saucedemo.com");
    Assert.assertEquals("Swag Labs", getDriver().getTitle());
  }
}
