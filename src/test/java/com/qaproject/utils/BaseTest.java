package com.qaproject.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Base class every test class extends.
 * Handles WebDriver lifecycle, base URL, and failure screenshots
 * so individual test classes only contain test logic, not setup boilerplate.
 */
public class BaseTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "https://www.saucedemo.com/";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Runs headless in CI (GitHub Actions); comment out locally if you want to watch the browser.
        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(BASE_URL);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getName());
        }
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(Paths.get("screenshots"));
            File dest = new File("screenshots/" + testName + "_" + System.currentTimeMillis() + ".png");
            Files.copy(src.toPath(), dest.toPath());
            System.out.println("Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }
}
