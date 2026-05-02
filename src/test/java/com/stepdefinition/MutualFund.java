package com.stepdefinition;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.baseclass.BaseClass;

import io.cucumber.java.en.When;

public class MutualFund extends BaseClass {

    @When("User MouseOver Dashboard and Click Mutual Funds")
    public void user_mouse_over_dashboard_and_click_mutual_funds() throws InterruptedException {

        Thread.sleep(2000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement dashBoard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Dashboard']//ancestor::li[1]")));

        Actions ac = new Actions(driver);
        ac.moveToElement(dashBoard).perform();
        Thread.sleep(2000);

        driver.findElement(By.xpath("//a[@data-title='MF']//parent::li")).click();
        Thread.sleep(2000);
    }

    // FIX: Removed Robot class — uses OS-level keyboard, fails headless (no display)
    // Replaced with Selenium Keys.ENTER which works in all environments
    @When("User click Explore and Search {string}")
    public void user_click_explore_and_search(String string) throws InterruptedException {
        Thread.sleep(2000);

        WebElement iframe = driver.findElement(By.xpath("//iframe[@class='iframe_window']"));
        driver.switchTo().frame(iframe);
        Thread.sleep(2000);

        WebElement mandateList = driver.findElement(By.xpath("//a[@id='mandatelist-label']"));
        Actions ac = new Actions(driver);
        ac.moveToElement(mandateList).perform();
        Thread.sleep(15000);

        driver.findElement(By.xpath("//a[@id='dashboard-label']//descendant::i")).click();
        Thread.sleep(2000);

        WebElement searchInput = driver.findElement(By.xpath("//input[@id='Search_Input']"));
        searchInput.click();
        Thread.sleep(2000);
        searchInput.sendKeys(string);
        Thread.sleep(2000);

        // FIX: Robot.keyPress replaced with Selenium sendKeys
        searchInput.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
    }

    @When("User Click Explore")
    public void user_click_explore() throws InterruptedException {
        Thread.sleep(1000);

        WebElement element2 = driver.findElement(By.xpath("//iframe[@class='iframe_window']"));
        driver.switchTo().frame(element2);

        WebElement element = driver.findElement(By.xpath("//button[@id='tab-content-tab_explore']"));
        Actions ac = new Actions(driver);
        ac.moveToElement(element).perform();
        Thread.sleep(2000);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
        Thread.sleep(1000);
    }

    @When("User Search {string} in Serach Box and enter")
    public void user_search_in_serach_box_and_enter(String string) throws InterruptedException {
        Thread.sleep(1000);

        driver.findElement(By.xpath("//input[@placeholder='Search']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@placeholder='Search']")).sendKeys(string);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[text()='" + string + "']//parent::div")).click();
        Thread.sleep(1000);
    }

    @When("User Click One Time , enter amount {string} and click pay now")
    public void user_click_one_time_enter_amount_and_click_pay_now(String string)
            throws InterruptedException {
        Thread.sleep(1000);

        driver.findElement(By.xpath("//span[text()='One-Time']")).click();
        Thread.sleep(1000);

        WebElement amt = driver.findElement(By.xpath("//input[@data-dhx-id='ot_amt']"));
        Thread.sleep(1000);
        amt.click();
        Thread.sleep(1000);
        amt.sendKeys(string);
        Thread.sleep(2000);

        driver.findElement(By.xpath("//span[text()='Pay Now']//parent::button")).click();
        Thread.sleep(1000);
    }

    @When("User Click enter UPI ID {string} and Click Pay Via UPI")
    public void user_click_enter_upi_id_and_click_pay_via_upi(String string)
            throws InterruptedException {

        Actions ac = new Actions(driver);
        Thread.sleep(2000);

        driver.findElement(By.xpath("//input[@placeholder='Enter UPI ID']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@placeholder='Enter UPI ID']")).sendKeys(string);
        Thread.sleep(3000);

        driver.findElement(By.xpath("//span[text()='Pay Via UPI']//parent::button")).click();
        Thread.sleep(13000);

        WebElement cancel = driver.findElement(
            By.xpath("//span[text()='Cancel Payment']//parent::button"));
        ac.moveToElement(cancel).perform();
        ac.click(cancel).perform();
        Thread.sleep(3000);

        driver.findElement(By.xpath("//div[@class='py_back']")).click();
        driver.switchTo().defaultContent();

        Actions s = new Actions(driver);
        WebElement element = driver.findElement(By.xpath("//div[@class='funds_show']"));
        Thread.sleep(2000);
        s.moveToElement(element).build().perform();
        Thread.sleep(2000);

        driver.switchTo().frame(0);
        Thread.sleep(2000);
    }
}