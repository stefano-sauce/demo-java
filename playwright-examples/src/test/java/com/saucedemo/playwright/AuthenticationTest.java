package com.saucedemo.playwright;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("playwright")
@Tag("junit5")
@Tag("java")
@Tag("login")
public class AuthenticationTest extends TestBase {

  @Override
  protected List<String> sauceTags() {
    List<String> tags = super.sauceTags();
    tags.add("login");
    return tags;
  }

  @Test
  public void signInUnsuccessful() {
    page.navigate("https://www.saucedemo.com/");

    page.fill("[data-test='username']", "locked_out_user");
    page.fill("[data-test='password']", "secret_sauce");
    page.click("[data-test='login-button']");

    String errorText = page.textContent("[data-test='error']");
    Assertions.assertTrue(
        errorText.contains("Sorry, this user has been locked out"), "Error Not Found");
  }

  @Test
  public void signInSuccessful() {
    page.navigate("https://www.saucedemo.com/");

    page.fill("[data-test='username']", "standard_user");
    page.fill("[data-test='password']", "secret_sauce");
    page.click("[data-test='login-button']");

    Assertions.assertEquals(
        "https://www.saucedemo.com/inventory.html", page.url(), "Login Not Successful");
  }

  @Test
  public void logout() throws InterruptedException {
    page.navigate("https://www.saucedemo.com/");
    page.fill("[data-test='username']", "standard_user");
    page.fill("[data-test='password']", "secret_sauce");
    page.click("[data-test='login-button']");

    page.click("#react-burger-menu-btn");
    page.click("#logout_sidebar_link");

    Assertions.assertEquals("https://www.saucedemo.com/", page.url(), "Logout Not Successful");
  }
}
