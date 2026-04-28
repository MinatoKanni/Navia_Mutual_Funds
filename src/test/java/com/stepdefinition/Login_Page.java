package com.stepdefinition;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.baseclass.BaseClass;

import io.cucumber.java.en.Given;

public class Login_Page extends BaseClass {

    @Given("User Navigate to Navia")
    public void user_navigate_to_navia() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // Step 1: Open Yopmail
        driver.get("https://yopmail.com/");

        WebElement inbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter your inbox here']")));
        inbox.sendKeys("naviatestingntp@yopmail.com");

        driver.findElement(By.xpath("//i[@class='material-icons-outlined f36']")).click();

        // Step 2: Switch to mail iframe
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ifmail"));

        // Step 3: Wait and fetch OTP mail content
        WebElement mailBody = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='mail']//pre")));

        String mailText = mailBody.getText();

        Pattern otpPattern = Pattern.compile("\\b\\d{6}\\b");
        Matcher matcher = otpPattern.matcher(mailText);

        String otp = null;

        if (matcher.find()) {
            otp = matcher.group();
            System.out.println("Extracted OTP: " + otp);
        } else {
            throw new RuntimeException("OTP not found in Yopmail");
        }

        // Step 4: Switch back to main page
        driver.switchTo().defaultContent();

        // Step 5: Open Navia login page
        driver.get("https://web.navia.co.in/login.php");

        // Step 6: Click login with client code
        try {
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Login with client code')]")));
            loginBtn.click();
        } catch (Exception e) {
            WebElement loginBtnAlt = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@id='login_fsmt1'])[1]")));
            loginBtnAlt.click();
        }

        // Step 7: Enter client code
        WebElement clientCode = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("clientCode")));
        clientCode.sendKeys("63748379");

        // Step 8: Enter password
        WebElement password = driver.findElement(By.name("lPassword"));
        password.sendKeys("Navia@123");

        // Step 9: Request OTP
        driver.findElement(By.xpath("//input[@onclick='GetTOTP()']")).click();

        // Step 10: Enter OTP
        WebElement otpBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("usertotp")));
        otpBox.sendKeys(otp);

        // Step 11: Click login
        driver.findElement(By.id("login_fsmt")).click();

        // Step 12: Handle optional risk disclosure popup
        try {
            WebElement agreeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[text()='Agree']//parent::button")));
            agreeBtn.click();
            System.out.println("Risk disclosure accepted");
        } catch (Exception e) {
            System.out.println("Risk disclosure not displayed");
        }
    }
}