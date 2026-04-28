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

        // FIX: Open Navia login page FIRST so the OTP email is sent
        // before we check YopMail. Previously the inbox was polled before
        // any credentials were submitted, so there was never an OTP to find.

        // STEP 1: Open Navia login page
        driver.get("https://web.navia.co.in/login.php");

        // STEP 2: Click login with client code
        try {
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Login with client code')]")));
            loginBtn.click();
        } catch (Exception e) {
            WebElement loginBtnAlt = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@id='login_fsmt1'])[1]")));
            loginBtnAlt.click();
        }

        // STEP 3: Enter credentials
        WebElement clientCode = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("clientCode")));
        clientCode.sendKeys("63748379");

        WebElement password = driver.findElement(By.name("lPassword"));
        password.sendKeys("Navia@123");

        // STEP 4: Request OTP — this triggers the email to YopMail
        driver.findElement(By.xpath("//input[@onclick='GetTOTP()']")).click();

        // STEP 5: Brief pause to allow email delivery before opening YopMail
        Thread.sleep(5000);

        // STEP 6: Open YopMail to fetch the OTP
        driver.get("https://yopmail.com/");

        WebElement inbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter your inbox here']")));
        inbox.clear();
        inbox.sendKeys("naviatestingntp@yopmail.com");

        driver.findElement(By.xpath("//i[@class='material-icons-outlined f36']")).click();

        // STEP 7: Switch to mail iframe and retry fetching OTP
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ifmail"));

        String otp = null;

        for (int i = 0; i < 5; i++) {
            try {
                WebElement mailBody = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@id='mail']//pre")));

                String text = mailBody.getText();

                Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
                Matcher matcher = pattern.matcher(text);

                if (matcher.find()) {
                    otp = matcher.group();
                    System.out.println("OTP Found: " + otp);
                    break;
                }

            } catch (Exception e) {
                System.out.println("Retrying OTP fetch... attempt " + (i + 1));
            }

            // Refresh inbox and retry
            driver.switchTo().defaultContent();

            WebElement refresh = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("refresh")));
            refresh.click();

            Thread.sleep(5000);

            driver.switchTo().frame("ifmail");
        }

        if (otp == null) {
            throw new RuntimeException("OTP not found after retries");
        }

        driver.switchTo().defaultContent();

        // STEP 8: Return to Navia and re-enter credentials + OTP
        driver.get("https://web.navia.co.in/login.php");

        try {
            WebElement loginBtn2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Login with client code')]")));
            loginBtn2.click();
        } catch (Exception e) {
            WebElement loginBtnAlt2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@id='login_fsmt1'])[1]")));
            loginBtnAlt2.click();
        }

        WebElement clientCode2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("clientCode")));
        clientCode2.sendKeys("63748379");

        WebElement password2 = driver.findElement(By.name("lPassword"));
        password2.sendKeys("Navia@123");

        // STEP 9: Enter the OTP that was fetched from YopMail
        WebElement otpBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("usertotp")));
        otpBox.sendKeys(otp);

        // STEP 10: Click login
        driver.findElement(By.id("login_fsmt")).click();

        // STEP 11: Wait for successful login
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='Dashboard']")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='user-name']"))
        ));

        // STEP 12: Handle optional risk disclosure
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
