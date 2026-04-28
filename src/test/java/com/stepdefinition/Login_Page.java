```java
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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // 🔹 Step 1: Open Yopmail
        driver.get("https://yopmail.com/");
        WebElement inbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter your inbox here']")));
        inbox.sendKeys("naviatestingntp@yopmail.com");

        driver.findElement(By.xpath("//i[@class='material-icons-outlined f36']")).click();

        // 🔹 Step 2: Wait for mail iframe
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ifmail"));

        // 🔹 Step 3: Extract OTP
        WebElement mailBody = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='mail']//pre")));

        String text = mailBody.getText();

        Pattern otpPattern = Pattern.compile("\\b\\d{6}\\b");
        Matcher matcher = otpPattern.matcher(text);

        String otp = null;
        if (matcher.find()) {
            otp = matcher.group();
            System.out.println("Extracted OTP: " + otp);
        } else {
            throw new RuntimeException("OTP not found in Yopmail");
        }

        driver.switchTo().defaultContent();

        // 🔹 Step 4: Open Navia login
        driver.get("https://web.navia.co.in/login.php");

        // 🔹 Step 5: Click login with client code
        try {
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Login with client code')]")));
            loginBtn.click();
        } catch (Exception e) {
            WebElement loginBtnAlt = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@id='login_fsmt1'])[1]")));
            loginBtnAlt.click();
        }

        // 🔹 Step 6: Enter credentials
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("clientCode")))
                .sendKeys("63748379");

        driver.findElement(By.name("lPassword"))
                .sendKeys("Navia@123");

        // 🔹 Step 7: Request OTP
        driver.findElement(By.xpath("//input[@onclick='GetTOTP()']")).click();

        // 🔹 Step 8: Enter OTP
        WebElement otpBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("usertotp")));
        otpBox.sendKeys(otp);

        // 🔹 Step 9: Final login
        driver.findElement(By.id("login_fsmt")).click();

        // 🔹 Step 10: Handle optional risk disclosure
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
```
