package com.saucedemo.selenium.selenium_features;

import com.saucedemo.selenium.TestBase;
import java.util.List;
import org.junit.jupiter.api.*;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("capabilities_merge")
public class CapabilitiesMergeTest extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("capabilities_merge");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) {
    startChromeSession(testInfo);
  }

  @DisplayName("Selenium 4 Can not merge in place!")
  @Test
  public void doesNotMergeInPlace() {
    ChromeOptions options1 = new ChromeOptions();
    ChromeOptions options2 = new ChromeOptions();

    options1.setPageLoadStrategy(PageLoadStrategy.EAGER);
    options2.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);

    options1.merge(options2);

    Assertions.assertNotEquals(
        UnexpectedAlertBehaviour.IGNORE, options1.getCapability("unhandledPromptBehavior"));
  }

  @DisplayName("Selenium 4 Has to merge as a new object")
  @Test
  public void mergeNewObject() {
    ChromeOptions options1 = new ChromeOptions();
    ChromeOptions options2 = new ChromeOptions();

    options1.setPageLoadStrategy(PageLoadStrategy.EAGER);
    options2.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);

    ChromeOptions options3 = options1.merge(options2);

    Assertions.assertEquals(
        UnexpectedAlertBehaviour.IGNORE, options3.getCapability("unhandledPromptBehavior"));
  }
}
