package com.saucedemo.selenium.selenium_features;

import com.saucedemo.selenium.TestBase;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("view_page")
public class ViewPageChromeTest extends TestBase {
  public static Path directory;

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("view_page");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) throws IOException {
    startChromeSession(testInfo);
    driver.navigate().to("https://www.saucedemo.com/v1/inventory.html");
    directory = Files.createTempDirectory("chrome-");
    directory.toFile().deleteOnExit();
  }

  @Test
  public void takeScreenshot() throws IOException {
    byte[] screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

    Path screenshot = Paths.get(directory + "Screenshot.png");
    Files.write(screenshot, screenshotAs);
    Assertions.assertTrue(screenshot.toFile().exists());
  }
}
