package com.saucedemo.selenium.selenium_features;

import com.saucedemo.selenium.TestBase;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.By;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("find_by")
public class FindByTest extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("find_by");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) {
    startChromeSession(testInfo);
  }

  @Test
  public void findElement() {
    driver.navigate().to("https://www.saucedemo.com");

    // These are no longer available at all in Selenium 4:
    //  driver.findElementById("user-name");
    //  driver.findElementByCssSelector("#password");
    //  driver.findElementByClassName("btn_action");

    driver.findElement(By.cssSelector("#user-name"));
    driver.findElement(By.cssSelector("#password"));
    driver.findElement(By.cssSelector(".btn_action"));
  }
}
