package com.saucedemo.selenium.selenium_features;

import com.saucedemo.selenium.TestBase;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.Point;
import org.openqa.selenium.WindowType;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("new_window")
public class NewWindowTest extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("new_window");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) {
    startChromeSession(testInfo);
  }

  @Test
  public void secondWindow() {
    driver.switchTo().newWindow(WindowType.WINDOW);
    driver.manage().window().setPosition(new Point(100, 400));

    Assertions.assertEquals(2, driver.getWindowHandles().toArray().length);
  }

  @Test
  public void secondTab() {
    driver.switchTo().newWindow(WindowType.TAB);

    Assertions.assertEquals(2, driver.getWindowHandles().toArray().length);
  }
}
