package com.saucelabs.tests.iOS;

import com.saucelabs.tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DemoSimpleTest extends BaseTest {

    By productsScreenLocator = By.id("products screen");
    By sortButtonLocator = By.id("sort button");
    By sortModalLocator = By.id("active option");

    @Override
    protected List<String> sauceTags() {
        List<String> tags = new ArrayList<>(super.sauceTags());
        tags.add("sort_modal");
        return tags;
    }

    @Test(groups = {"appium", "testng", "java", "sort_modal"})
    public void verifyPromptSortModal() throws MalformedURLException {
        //Wait for the application to start and load the initial screen (products screen)
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productsScreenLocator));

        getDriver().findElement(sortButtonLocator).click();

        //Verify the sort modal is displayed on screen
        assertThat(isDisplayed(sortModalLocator, 5)).as("Verify sort modal is displayed").isTrue();
    }

    public Boolean isDisplayed(By locator, long timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (org.openqa.selenium.TimeoutException exception) {
            return false;
        }
        return true;
    }
}
