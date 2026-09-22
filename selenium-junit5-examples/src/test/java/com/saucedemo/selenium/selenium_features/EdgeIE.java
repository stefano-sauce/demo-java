package com.saucedemo.selenium.selenium_features;

import com.saucedemo.selenium.TestBase;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.ie.InternetExplorerOptions;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("edge_ie_mode")
public class EdgeIE extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("edge_ie_mode");
    return tags;
  }

  @BeforeEach
  public void createSauceOptions(TestInfo testInfo) {
    InternetExplorerOptions options = new InternetExplorerOptions();
    options.attachToEdgeChrome();
    options.setCapability("browserName", "microsoftedge");
    Map<String, Object> sauceOptions = defaultSauceOptions(testInfo);

    startSession(options, sauceOptions);
  }

  @Test
  public void ieMode() {
    driver.get("https://www.saucedemo.com");
  }
}
