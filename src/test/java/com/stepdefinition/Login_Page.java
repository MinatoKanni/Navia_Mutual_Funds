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
    public void user_navigate_to_navia() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // 🔹 STEP 1: Open Yopmail
        driver.get("https://yopmail.com/");

        WebElement inbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter your inbox here']")));
        inbox.clear();
        inbox.sendKeys("naviatestingntp@yopmail.com");

        driver.findElement(By.xpath("//i[@class='material-icons-outlined f36']")).click();

        // 🔹 STEP 2: Switch to mail iframe
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ifmail"));

        String otp = null;

        // 🔥 STEP 3: Retry OTP fetch (important for Jenkins)
        for (int i = 0; i < 5; i++) {

            try {
                WebElement mailBody = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@id='mail']//pre")));

                String text = mailBody.getText();

                Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
                Matcher matcher = pattern.matcher(text);

                if (matcher.find()) {
                    otp = matcher.group();
                    System.out.println("✅ OTP Found: " + otp);
                    break;
                }

            } catch (Exception e) {
                System.out.println("Retrying OTP fetch...");
            }

            // 🔄 Refresh inbox
            driver.switchTo().defaultContent();

            WebElement refresh = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("refresh")));
            refresh.click();

            Thread.sleep(5000);

            driver.switchTo().frame("ifmail");
        }

        if (otp == null) {
            throw new RuntimeException("❌ OTP not found after retries");
        }

        driver.switchTo().defaultContent();

        // 🔹 STEP 4: Open Navia login page
        driver.get("https://web.navia.co.in/login.php");

        // 🔹 STEP 5: Click login with client code
        try {
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Login with client code')]")));
            loginBtn.click();
        } catch (Exception e) {
            WebElement loginBtnAlt = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@id='login_fsmt1'])[1]")));
            loginBtnAlt.click();
        }

        // 🔹 STEP 6: Enter credentials
        WebElement clientCode = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("clientCode")));
        clientCode.sendKeys("63748379");

        WebElement password = driver.findElement(By.name("lPassword"));
        password.sendKeys("Navia@123");

        // 🔹 STEP 7: Request OTP
        driver.findElement(By.xpath("//input[@onclick='GetTOTP()']")).click();

        // 🔹 STEP 8: Enter OTP
        WebElement otpBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("usertotp")));
        otpBox.sendKeys(otp);

        // 🔹 STEP 9: Click login
        driver.findElement(By.id("login_fsmt")).click();

        // 🔹 STEP 10: Wait for successful login (VERY IMPORTANT)
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='Dashboard']")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='user-name']"))
        ));

        // 🔹 STEP 11: Handle optional risk disclosure
        try {
            WebElement agreeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[text()='Agree']//parent::button")));
            agreeBtn.click();
            System.out.println("✅ Risk disclosure accepted");
        } catch (Exception e) {
            System.out.println("ℹ Risk disclosure not displayed");
        }
    }
}