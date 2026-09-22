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
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.print.PrintOptions;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("print_pdf")
public class PrintPdfChromeTest extends TestBase {
  public static Path directory;

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("print_pdf");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) throws IOException {
    // PDF printing is only working in headless old mode
    startChromeSession(testInfo, List.of("--headless=new"));
    driver.navigate().to("https://www.saucedemo.com/v1/inventory.html");
    directory = Files.createTempDirectory("chrome-");
    directory.toFile().deleteOnExit();
  }

  @Test
  public void printPage() throws IOException {
    Pdf print = ((PrintsPage) driver).print(new PrintOptions());
    Path printPage = Paths.get(directory + "PrintPage.pdf");
    Files.write(printPage, OutputType.BYTES.convertFromBase64Png(print.getContent()));
    Assertions.assertTrue(printPage.toFile().exists());
  }
}
