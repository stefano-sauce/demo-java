package com.saucedemo.playwright;

import com.microsoft.playwright.Page;
import com.saucelabs.bindings.SaucePlaywrightSession;
import com.saucelabs.extensions.SaucePlaywrightExtension;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

public class TestBase {
  SaucePlaywrightSession session;
  Page page;

  @RegisterExtension
  public SaucePlaywrightExtension sauceExtension = new SaucePlaywrightExtension(sauceTags());

  /**
   * Sauce Labs tags identifying this test's driver framework, test runner and language.
   * Subclasses override this to append their own use-case tag.
   */
  protected List<String> sauceTags() {
    return new ArrayList<>(List.of("playwright", "junit5", "java"));
  }

  @BeforeEach
  public void setUp(SaucePlaywrightSession session, Page page) {
    this.session = session;
    this.page = page;
  }

  static {
    System.setProperty("sauce.build.name", "Playwright Sauce Demo");
    System.setProperty("sauce.build.number", String.valueOf(System.currentTimeMillis()));
  }
}
