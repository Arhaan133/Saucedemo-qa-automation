package com.qaproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for the product listing (inventory) screen shown after login.
 * Maps to TC-007 to TC-014 in test-cases/Test_Case_Documentation.xlsx
 */
public class InventoryPage {

    private final WebDriver driver;

    private final By pageTitle = By.className("title");
    private final By productCards = By.className("inventory_item");
    private final By sortDropdown = By.className("product_sort_container");
    private final By productPrices = By.className("inventory_item_price");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartIcon = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isLoaded() {
        return driver.findElement(pageTitle).getText().equalsIgnoreCase("Products");
    }

    public int getProductCount() {
        return driver.findElements(productCards).size();
    }

    public void addProductToCartByName(String productName) {
        List<WebElement> cards = driver.findElements(productCards);
        for (WebElement card : cards) {
            String name = card.findElement(By.className("inventory_item_name")).getText();
            if (name.equalsIgnoreCase(productName)) {
                card.findElement(By.tagName("button")).click();
                return;
            }
        }
        throw new NoSuchElementExceptionWrapper("Product not found: " + productName);
    }

    public int getCartItemCount() {
        try {
            return Integer.parseInt(driver.findElement(cartBadge).getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public void goToCart() {
        driver.findElement(cartIcon).click();
    }

    public void sortBy(String visibleText) {
        Select select = new Select(driver.findElement(sortDropdown));
        select.selectByVisibleText(visibleText);
    }

    public List<Double> getAllPricesInOrder() {
        List<WebElement> priceElements = driver.findElements(productPrices);
        return priceElements.stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    // Small wrapper so this page object doesn't force callers to import java.util.NoSuchElementException
    private static class NoSuchElementExceptionWrapper extends RuntimeException {
        NoSuchElementExceptionWrapper(String message) {
            super(message);
        }
    }
}
