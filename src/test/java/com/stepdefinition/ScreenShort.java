package com.stepdefinition;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.baseclass.BaseClass;
import com.pom.Login_Navia_POM;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.When;

public class ScreenShort extends BaseClass {

    Login_Navia_POM l;
    WebDriverWait wait;

    // FIX: Removed ATUTestRecorder — requires display, breaks headless Jenkins
    // FIX: Removed ATUTestRecorderException from throws clause
    @Before
    public void beforeTest() {
        l = new Login_Navia_POM(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Screenshot for ALL scenarios (PASSED + FAILED) with status in filename
    // FIX: Removed ATUTestRecorderException from throws clause
    @After
    public void afterTest(Scenario scenario) {
        String status = scenario.getStatus().toString().toUpperCase();
        takeScreenshot(scenario.getName() + "_" + status);
        if (scenario.isFailed()) {
            System.out.println("[FAILED] " + scenario.getName());
        } else {
            System.out.println("[PASSED] " + scenario.getName());
        }
        // NOTE: No deleteAllCookies() — destroys session between scenarios
    }

    public void waitForPageReady() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                d -> ((JavascriptExecutor) d)
                        .executeScript("return document.readyState").equals("complete"));

            new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//label[text()='Dashboard']")),
                    ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[@class='user-name']")),
                    ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//a[@data-title='MF']")),
                    ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//li[@class='widgets_mf']")),
                    ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//button[@data-dhx-id='btn_addmoney']"))
                ));
            System.out.println("[INFO] Page ready.");

        } catch (Exception e) {
            System.out.println("[INFO] waitForPageReady timed out, proceeding: " + e.getMessage());
        }
    }

    @When("Navigate to home page")
    public void navigate_to_home_page() {
        driver.get("https://web.navia.co.in/index.php");
        waitForPageReady();
    }
}