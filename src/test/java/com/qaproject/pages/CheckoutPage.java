package com.qaproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object covering the two-step SauceDemo checkout flow
 * (customer info -> overview/finish).
 * Maps to TC-019 to TC-025 in test-cases/Test_Case_Documentation.xlsx
 */
public class CheckoutPage {

    private final WebDriver driver;

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
    }

    public void fillCustomerInfo(String firstName, String lastName, String postalCode) {
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(postalCodeField).sendKeys(postalCode);
        driver.findElement(continueButton).click();
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(errorMessage).isEmpty();
    }

    public void finishOrder() {
        driver.findElement(finishButton).click();
    }

    public boolean isOrderComplete() {
        return driver.findElement(completeHeader).getText().toLowerCase().contains("thank you");
    }

    public String getSubtotalText() {
        return driver.findElement(summarySubtotal).getText();
    }

    public String getTaxText() {
        return driver.findElement(summaryTax).getText();
    }

    public String getTotalText() {
        return driver.findElement(summaryTotal).getText();
    }
}
