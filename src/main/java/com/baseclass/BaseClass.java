package com.baseclass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

    public static WebDriver driver;
    public static Actions a;

    public static WebDriver launchBrowser(String browser) {

        if (browser.equalsIgnoreCase("Chrome")) {

            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // Auto-detect CI/Jenkins environment and run headless
            boolean isCI = System.getenv("CI") != null
                    || System.getenv("JENKINS_HOME") != null
                    || System.getenv("JENKINS_URL") != null;

            if (isCI) {
                System.out.println("[INFO] CI/Jenkins environment detected - launching Chrome in HEADLESS mode.");
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--remote-allow-origins=*");
            } else {
                System.out.println("[INFO] Local environment detected - launching Chrome in HEADED mode.");
            }

            options.addArguments("--disable-notifications");
            driver = new ChromeDriver(options);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        return driver;
    }

    public static void getUrl(String url) {
        driver.get(url);
    }

    public static void clickOnElement(WebElement element) {
        element.click();
    }

    public static void sendValues(WebElement element, String value) {
        element.sendKeys(value);
    }

    public static void quitBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    // FIX: Timestamp in filename prevents FileAlreadyExistsException
    // when multiple scenarios share the same name
    public static void takeScreenshot(String name) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String safeName = name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
            File dest = new File("target/screenshots/" + safeName + "_" + timestamp + ".png");

            Files.createDirectories(dest.getParentFile().toPath());
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[SCREENSHOT] Saved: " + dest.getPath());

        } catch (IOException e) {
            System.err.println("[SCREENSHOT ERROR] Failed to save screenshot for: " + name);
            e.printStackTrace();
        }
    }

    public static void framesHandling() throws InterruptedException {
        Thread.sleep(1000);
        WebElement frame1 = driver.findElement(By.xpath("//iframe[@class='iframe_window']"));
        driver.switchTo().frame(frame1);
        WebElement frame2 = driver.findElement(By.xpath("//iframe[@title='Financial Chart']"));
        driver.switchTo().frame(frame2);
        Thread.sleep(1000);
    }

    public static void outOfFrames() throws InterruptedException {
        Thread.sleep(1000);
        driver.switchTo().defaultContent();
    }
    
    public static void sleep(long timeoutInSeconds) throws InterruptedException {

		Thread.sleep(timeoutInSeconds);
	}
    
    public static void setImplicitWait(long timeoutInSeconds) {
		driver.manage().timeouts().implicitlyWait(timeoutInSeconds, TimeUnit.SECONDS);
	}
    
    
    
    
    
    
}