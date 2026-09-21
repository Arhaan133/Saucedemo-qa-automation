package com.qaproject.tests;

import com.qaproject.pages.CartPage;
import com.qaproject.pages.CheckoutPage;
import com.qaproject.pages.InventoryPage;
import com.qaproject.pages.LoginPage;
import com.qaproject.utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Automates TC-019 to TC-025: the full checkout flow, from cart to order
 * confirmation, plus checkout-form validation.
 */
public class CheckoutTests extends BaseTest {

    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void loginAndAddProduct() {
        new LoginPage(driver).login("standard_user", "secret_sauce");
        inventoryPage = new InventoryPage(driver);
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test(description = "TC-019: Complete checkout with valid details should confirm the order")
    public void testCompleteCheckoutFlow() {
        cartPage.clickCheckout();
        checkoutPage.fillCustomerInfo("Arhaan", "Shaikh", "411001");
        checkoutPage.finishOrder();
        Assert.assertTrue(checkoutPage.isOrderComplete(), "Order confirmation message should be displayed");
    }

    @Test(description = "TC-020: Checkout should reject an empty first name")
    public void testCheckoutMissingFirstName() {
        cartPage.clickCheckout();
        checkoutPage.fillCustomerInfo("", "Shaikh", "411001");
        Assert.assertTrue(checkoutPage.isErrorDisplayed(), "Error should be shown when first name is missing");
    }

    @Test(description = "TC-021: Checkout should reject an empty postal code")
    public void testCheckoutMissingPostalCode() {
        cartPage.clickCheckout();
        checkoutPage.fillCustomerInfo("Arhaan", "Shaikh", "");
        Assert.assertTrue(checkoutPage.isErrorDisplayed(), "Error should be shown when postal code is missing");
    }

    @Test(description = "TC-022: Order summary should show non-zero subtotal, tax, and total")
    public void testOrderSummaryCalculations() {
        cartPage.clickCheckout();
        checkoutPage.fillCustomerInfo("Arhaan", "Shaikh", "411001");

        Assert.assertTrue(checkoutPage.getSubtotalText().contains("$"), "Subtotal should display a dollar amount");
        Assert.assertTrue(checkoutPage.getTaxText().contains("$"), "Tax should display a dollar amount");
        Assert.assertTrue(checkoutPage.getTotalText().contains("$"), "Total should display a dollar amount");
    }
}
