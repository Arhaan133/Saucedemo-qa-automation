package com.qaproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the product listing (inventory) screen shown after login.
 * Maps to TC-007 to TC-014 in test-cases/Test_Case_Documentation.xlsx
 *
 * Add-to-cart uses a click -> verify -> retry pattern: on slow CI runners the page
 * can be visible before its click handlers are attached, so a single click may be
 * silently ignored. We confirm the button flipped to "Remove" and retry if not.
 */
public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    private final By pageTitle = By.className("title");
    private final By productCards = By.className("inventory_item");
    private final By sortDropdown = By.className("product_sort_container");
    private final By productPrices = By.className("inventory_item_price");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartIcon = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(4));
        this.shortWait.ignoring(StaleElementReferenceException.class);
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle))
                    .getText().equalsIgnoreCase("Products");
        } catch (TimeoutException e) {
            return false;
        }
    }

    public int getProductCount() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        return driver.findElements(productCards).size();
    }

    public void addProductToCartByName(String productName) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));

        for (int attempt = 1; attempt <= 3; attempt++) {
            WebElement button = findButtonForProduct(productName);
            // If an earlier click already registered, don't click again (it would toggle to "Add to cart").
            if (button.getText().equalsIgnoreCase("Remove")) {
                return;
            }
            wait.until(ExpectedConditions.elementToBeClickable(button)).click();
            try {
                // Verify the click actually registered: button text flips to "Remove".
                shortWait.until(d -> findButtonForProduct(productName)
                        .getText().equalsIgnoreCase("Remove"));
                return;
            } catch (TimeoutException e) {
                // Click was ignored (page not interactive yet) - try again.
            }
        }
        throw new NoSuchElementException("Could not add product to cart after 3 attempts: "
                + productName + " (current URL: " + driver.getCurrentUrl() + ")");
    }

    private WebElement findButtonForProduct(String productName) {
        for (WebElement card : driver.findElements(productCards)) {
            String name = card.findElement(By.className("inventory_item_name")).getText();
            if (name.equalsIgnoreCase(productName)) {
                return card.findElement(By.tagName("button"));
            }
        }
        throw new NoSuchElementException("Product not found: " + productName);
    }

    public int getCartItemCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        if (badges.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText());
    }

    public void goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        wait.until(ExpectedConditions.urlContains("cart.html"));
    }

    public void sortBy(String visibleText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    public List<Double> getAllPricesInOrder() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productPrices));
        List<WebElement> priceElements = driver.findElements(productPrices);
        return priceElements.stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }
}
