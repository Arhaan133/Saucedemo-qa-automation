package com.qaproject.tests;

import com.qaproject.pages.InventoryPage;
import com.qaproject.pages.LoginPage;
import com.qaproject.utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Automates TC-007 to TC-014: product listing, sorting, and add-to-cart behavior.
 */
public class InventoryTests extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void loginAsStandardUser() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        inventoryPage = new InventoryPage(driver);
    }

    @Test(description = "TC-007: Products page should list exactly 6 items")
    public void testProductCount() {
        Assert.assertEquals(inventoryPage.getProductCount(), 6, "SauceDemo should always list 6 products");
    }

    @Test(description = "TC-008: Adding a product should increment the cart badge")
    public void testAddSingleProductToCart() {
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        Assert.assertEquals(inventoryPage.getCartItemCount(), 1, "Cart badge should show 1 after adding one item");
    }

    @Test(description = "TC-009: Adding multiple products should accumulate the cart badge count")
    public void testAddMultipleProductsToCart() {
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.addProductToCartByName("Sauce Labs Bike Light");
        inventoryPage.addProductToCartByName("Sauce Labs Bolt T-Shirt");
        Assert.assertEquals(inventoryPage.getCartItemCount(), 3, "Cart badge should reflect all 3 added items");
    }

    @Test(description = "TC-010: Sorting Price (low to high) should return an ascending price list")
    public void testSortPriceLowToHigh() {
        inventoryPage.sortBy("Price (low to high)");
        List<Double> prices = inventoryPage.getAllPricesInOrder();
        List<Double> sorted = prices.stream().sorted().toList();
        Assert.assertEquals(prices, sorted, "Prices should be in ascending order after sorting low to high");
    }

    @Test(description = "TC-011: Sorting Price (high to low) should return a descending price list")
    public void testSortPriceHighToLow() {
        inventoryPage.sortBy("Price (high to low)");
        List<Double> prices = inventoryPage.getAllPricesInOrder();
        List<Double> sortedDesc = prices.stream().sorted((a, b) -> Double.compare(b, a)).toList();
        Assert.assertEquals(prices, sortedDesc, "Prices should be in descending order after sorting high to low");
    }

    @Test(description = "TC-012: Cart icon should navigate to the cart page")
    public void testNavigateToCart() {
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"), "Should navigate to the cart page");
    }
}
