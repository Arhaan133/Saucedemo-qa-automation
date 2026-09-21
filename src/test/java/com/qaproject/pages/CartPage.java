package com.qaproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Page Object for the shopping cart screen.
 * Maps to TC-015 to TC-018 in test-cases/Test_Case_Documentation.xlsx
 */
public class CartPage {

    private final WebDriver driver;

    private final By cartItems = By.className("cart_item");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    public void clickCheckout() {
        driver.findElement(checkoutButton).click();
    }

    public void continueShopping() {
        driver.findElement(continueShoppingButton).click();
    }
}
