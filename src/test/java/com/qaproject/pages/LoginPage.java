package com.qaproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the SauceDemo login screen.
 * Maps to TC-001 to TC-006 in test-cases/Test_Case_Documentation.xlsx
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void login(String username, String password) {
        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        user.clear();
        user.sendKeys(username);

        WebElement pass = driver.findElement(passwordField);
        pass.clear();
        pass.sendKeys(password);

        driver.findElement(loginButton).click();
    }

    public boolean isErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorText() {
        return driver.findElement(errorMessage).getText();
    }
}
