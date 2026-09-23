package com.saucedemo.selenium.demo;

import com.saucedemo.selenium.TestBase;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

@Tag("selenium")
@Tag("junit5")
@Tag("java")
@Tag("navigation")
public class NavigationTest extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("navigation");
    return tags;
  }

  @BeforeEach
  public void setup(TestInfo testInfo) {
    startFirefoxSession(testInfo, Map.of("extendedDebugging", true));
  }

  @Test
  public void cancelFromCart() {
    driver.get("https://www.saucedemo.com/");
    driver.findElement(By.cssSelector("input[data-test='username']")).sendKeys("standard_user");
    driver.findElement(By.cssSelector("input[data-test='password']")).sendKeys("secret_sauce");
    driver.findElement(By.cssSelector("input[data-test='login-button']")).click();
    driver.findElement(By.className("shopping_cart_link")).click();

    driver.findElement(By.cssSelector("button[data-test='continue-shopping']")).click();

    Assertions.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
  }

  @Test
  public void cancelFromInfoPage() {
    driver.get("https://www.saucedemo.com/");
    driver.findElement(By.cssSelector("input[data-test='username']")).sendKeys("standard_user");
    driver.findElement(By.cssSelector("input[data-test='password']")).sendKeys("secret_sauce");
    driver.findElement(By.cssSelector("input[data-test='login-button']")).click();
    driver.findElement(By.cssSelector("button[data-test='add-to-cart-sauce-labs-onesie']")).click();
    driver.findElement(By.className("shopping_cart_link")).click();
    driver.findElement(By.cssSelector("button[data-test='checkout']")).click();

    driver.findElement(By.cssSelector("button[data-test='cancel']")).click();

    Assertions.assertEquals("https://www.saucedemo.com/cart.html", driver.getCurrentUrl());
  }

  @Test
  public void cancelFromCheckoutPage() {
    driver.get("https://www.saucedemo.com/");
    driver.findElement(By.cssSelector("input[data-test='username']")).sendKeys("standard_user");
    driver.findElement(By.cssSelector("input[data-test='password']")).sendKeys("secret_sauce");
    driver.findElement(By.cssSelector("input[data-test='login-button']")).click();
    driver.findElement(By.cssSelector("button[data-test='add-to-cart-sauce-labs-onesie']")).click();
    driver.findElement(By.className("shopping_cart_link")).click();
    driver.findElement(By.cssSelector("button[data-test='checkout']")).click();
    driver.findElement(By.cssSelector("input[data-test='firstName']")).sendKeys("Luke");
    driver.findElement(By.cssSelector("input[data-test='lastName']")).sendKeys("Perry");
    driver.findElement(By.cssSelector("input[data-test='postalCode']")).sendKeys("90210");
    driver.findElement(By.cssSelector("input[data-test='continue']")).click();

    driver.findElement(By.cssSelector("button[data-test='cancel']")).click();

    Assertions.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
  }

  @Test
  public void startCheckout() {
    driver.get("https://www.saucedemo.com/");
    driver.findElement(By.cssSelector("input[data-test='username']")).sendKeys("standard_user");
    driver.findElement(By.cssSelector("input[data-test='password']")).sendKeys("secret_sauce");
    driver.findElement(By.cssSelector("input[data-test='login-button']")).click();
    driver.findElement(By.cssSelector("button[data-test='add-to-cart-sauce-labs-onesie']")).click();
    driver.findElement(By.className("shopping_cart_link")).click();

    driver.findElement(By.cssSelector("button[data-test='checkout']")).click();

    Assertions.assertEquals(
        "https://www.saucedemo.com/checkout-step-one.html", driver.getCurrentUrl());
  }
}
