package com.qaproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object covering the two-step SauceDemo checkout flow
 * (customer info -> overview/finish).
 * Maps to TC-019 to TC-025 in test-cases/Test_Case_Documentation.xlsx
 */
public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By completeHeader = By.className("complete-header");
    private final By summarySubtotal = By.className("summary_subtotal_label");
    private final By summaryTax = By.className("summary_tax_label");
    private final By summaryTotal = By.className("summary_total_label");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillCustomerInfo(String firstName, String lastName, String postalCode) {
        WebElement firstNameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        firstNameEl.clear();
        firstNameEl.sendKeys(firstName);

        WebElement lastNameEl = driver.findElement(lastNameField);
        lastNameEl.clear();
        lastNameEl.sendKeys(lastName);

        WebElement postalCodeEl = driver.findElement(postalCodeField);
        postalCodeEl.clear();
        postalCodeEl.sendKeys(postalCode);

        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public boolean isErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void finishOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    public boolean isOrderComplete() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader))
                .getText().toLowerCase().contains("thank you");
    }

    public String getSubtotalText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(summarySubtotal)).getText();
    }

    public String getTaxText() {
        return driver.findElement(summaryTax).getText();
    }

    public String getTotalText() {
        return driver.findElement(summaryTotal).getText();
    }
}