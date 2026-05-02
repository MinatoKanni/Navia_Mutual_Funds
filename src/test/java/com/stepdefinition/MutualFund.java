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

        // Robot replaced with Selenium Keys — works in headless mode
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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Type the search text
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='Search']")));
        searchBox.click();
        Thread.sleep(1000);
        searchBox.sendKeys(string);
        Thread.sleep(2000);

        // FIX: ElementClickInterceptedException — the iframe_window overlay sits on top
        // of the search result at point (206,128), blocking a normal click.
        // scrollIntoView centres the result away from the toolbar overlay,
        // then JS click bypasses the interception entirely.
        WebElement result = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[text()='" + string + "']//parent::div")));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", result);
        Thread.sleep(500);
        js.executeScript("arguments[0].click();", result);
        Thread.sleep(1000);
    }

    @When("User Click One Time , enter amount {string} and click pay now")
    public void user_click_one_time_enter_amount_and_click_pay_now(String string)
            throws InterruptedException {
        Thread.sleep(2000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // FIX: After user_search_in_serach_box_and_enter() JS-clicks the fund result,
        // the driver context returns to default content. The fund detail page
        // (with One-Time / ot_amt) renders inside the iframe_window, so we must
        // switch back into it before trying to interact with any elements here.
        try {
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//iframe[@class='iframe_window']")));
            driver.switchTo().frame(iframe);
        } catch (Exception e) {
            // Already inside the iframe — proceed
        }

        WebElement oneTime = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[text()='One-Time']")));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", oneTime);
        Thread.sleep(500);
        js.executeScript("arguments[0].click();", oneTime);
        Thread.sleep(1000);

        WebElement amt = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@data-dhx-id='ot_amt']")));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", amt);
        Thread.sleep(300);
        amt.click();
        Thread.sleep(500);
        amt.sendKeys(string);
        Thread.sleep(2000);

        WebElement payNow = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[text()='Pay Now']//parent::button")));
        js.executeScript("arguments[0].click();", payNow);
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