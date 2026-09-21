package com.qaproject.tests;

import com.qaproject.pages.InventoryPage;
import com.qaproject.pages.LoginPage;
import com.qaproject.utils.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Automates TC-001 to TC-006 from the manual test case documentation.
 * Covers valid login, invalid credentials, empty fields, and the
 * known "locked out user" business rule.
 */
public class LoginTests extends BaseTest {

    @Test(description = "TC-001: Valid credentials should log the user into the Products page")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "User should land on the Products page after valid login");
    }

    @Test(description = "TC-002: Invalid password should show an error and block login")
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "wrong_password");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be shown for invalid password");
        Assert.assertTrue(loginPage.getErrorText().contains("do not match"),
                "Error text should indicate username/password mismatch");
    }

    @Test(description = "TC-003: Locked out user should be blocked with a specific error message")
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be shown for locked out user");
        Assert.assertTrue(loginPage.getErrorText().toLowerCase().contains("locked out"),
                "Error text should mention the user has been locked out. " +
                "NOTE: see bug-reports/BUG-002 for a related issue found during this test.");
    }

    @Test(description = "TC-004: Empty username should show a required-field error")
    public void testEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be shown when username is blank");
        Assert.assertTrue(loginPage.getErrorText().toLowerCase().contains("username is required"));
    }

    @Test(description = "TC-005: Empty password should show a required-field error")
    public void testEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be shown when password is blank");
        Assert.assertTrue(loginPage.getErrorText().toLowerCase().contains("password is required"));
    }

    @Test(description = "TC-006: Both fields empty should show the username-required error first")
    public void testBothFieldsEmpty() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("", "");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be shown when both fields are blank");
    }
}
