package com.qaproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the shopping cart screen.
 * Maps to TC-015 to TC-018 in test-cases/Test_Case_Documentation.xlsx
 *
 * clickCheckout() verifies navigation actually happened and retries the click if
 * it was ignored (page visible but not yet interactive on a slow CI runner).
 */
public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    private final By cartItems = By.className("cart_item");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(4));
    }

    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    public void clickCheckout() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
            try {
                shortWait.until(ExpectedConditions.urlContains("checkout-step-one"));
                return;
            } catch (TimeoutException e) {
                // Click was ignored - try again.
            }
        }
        throw new TimeoutException("Checkout page did not open after 3 click attempts");
    }

    public void continueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
    }
}
