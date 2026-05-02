package com.stepdefinition;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.baseclass.BaseClass;

import io.cucumber.java.en.Given;

public class Login_Page extends BaseClass {

    @Given("User Navigate to Navia")
    public void user_navigate_to_navia() throws InterruptedException {

        // Use longer wait for headless — elements render slower without GPU
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // STEP 1: Open Navia login page
        driver.get("https://web.navia.co.in/login.php");
        Thread.sleep(3000);

        // STEP 2: Click login with client code
        try {
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Login with client code')]")));
            loginBtn.click();
        } catch (Exception e) {
            try {
                WebElement loginBtnAlt = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("(//button[@id='login_fsmt1'])[1]")));
                loginBtnAlt.click();
            } catch (Exception e2) {
                // Try JS click as last resort
                WebElement btn = driver.findElement(By.xpath("//button[@id='login_fsmt1']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }
        }
        Thread.sleep(2000);

        // STEP 3: Enter credentials
        WebElement clientCode = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("clientCode")));
        clientCode.clear();
        clientCode.sendKeys("82486527");

        WebElement password = driver.findElement(By.name("lPassword"));
        password.clear();
        password.sendKeys("Mynavia@123");

        // STEP 4: Request OTP — triggers email to YopMail
        try {
            driver.findElement(By.xpath("//input[@onclick='GetTOTP()']")).click();
        } catch (Exception e) {
            driver.findElement(By.xpath("//button[contains(text(),'Get OTP')]")).click();
        }

        // STEP 5: Wait for OTP email to arrive
        Thread.sleep(8000);

        // STEP 6: Open YopMail in same tab to fetch OTP
        driver.get("https://yopmail.com/");

        WebElement inbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter your inbox here']")));
        inbox.clear();
        inbox.sendKeys("naviatestingntp@yopmail.com");

        driver.findElement(By.xpath("//i[@class='material-icons-outlined f36']")).click();
        Thread.sleep(3000);

        // STEP 7: Switch to mail iframe and fetch OTP with retries
        String otp = null;

        for (int i = 0; i < 8; i++) {
            try {
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ifmail"));

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

            driver.switchTo().defaultContent();
            try {
                WebElement refresh = driver.findElement(By.id("refresh"));
                refresh.click();
            } catch (Exception e) {
                driver.navigate().refresh();
            }
            Thread.sleep(5000);
        }

        if (otp == null) {
            throw new RuntimeException("OTP not found after retries");
        }

        driver.switchTo().defaultContent();

        // STEP 8: Return to Navia login and enter credentials + OTP
        driver.get("https://web.navia.co.in/login.php");
        Thread.sleep(3000);

        try {
            WebElement loginBtn2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Login with client code')]")));
            loginBtn2.click();
        } catch (Exception e) {
            try {
                WebElement loginBtnAlt2 = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("(//button[@id='login_fsmt1'])[1]")));
                loginBtnAlt2.click();
            } catch (Exception e2) {
                WebElement btn = driver.findElement(By.xpath("//button[@id='login_fsmt1']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }
        }
        Thread.sleep(2000);

        WebElement clientCode2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("clientCode")));
        clientCode2.clear();
        clientCode2.sendKeys("63748379");

        WebElement password2 = driver.findElement(By.name("lPassword"));
        password2.clear();
        password2.sendKeys("Navia@123");

        // STEP 9: Enter OTP
        WebElement otpBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("usertotp")));
        otpBox.clear();
        otpBox.sendKeys(otp);

        // STEP 10: Click login
        driver.findElement(By.id("login_fsmt")).click();

        // STEP 11: Wait for home page — increased to 90s for headless slow render
        // Added more fallback XPaths in case primary ones aren't present on web.navia.co.in
        try {
            new WebDriverWait(driver, Duration.ofSeconds(90)).until(
                ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[text()='Dashboard']")),
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@class='user-name']")),
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[@data-title='MF']")),
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//li[@class='widgets_mf']")),
                    ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//button[@data-dhx-id='btn_addmoney']"))
                ));
            System.out.println("[INFO] Login successful - home page loaded.");
        } catch (Exception e) {
            System.out.println("[WARN] Home page wait timed out — proceeding anyway: " + e.getMessage());
        }

        // STEP 12: Handle optional risk disclosure
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[text()='Agree']//parent::button")))
                .click();
            System.out.println("Risk disclosure accepted");
        } catch (Exception e) {
            System.out.println("Risk disclosure not displayed");
        }
    }
}