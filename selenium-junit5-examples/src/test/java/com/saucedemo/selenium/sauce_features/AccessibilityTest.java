package com.saucedemo.selenium.sauce_features;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.saucedemo.selenium.TestBase;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("accessibility")
public class AccessibilityTest extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("accessibility");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) {
    startChromeSession(testInfo);
  }

  @DisplayName("Deque Axe Test With Selenium Not html")
  @Test
  public void accessibilityTest() {
    driver.navigate().to("https://www.saucedemo.com");

    AxeBuilder axeBuilder;
    axeBuilder = new AxeBuilder();
    Results accessibilityResults = axeBuilder.analyze(driver);

    Assertions.assertEquals(3, accessibilityResults.getViolations().size());
  }
}
