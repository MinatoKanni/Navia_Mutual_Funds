package com.stepdefinition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.baseclass.BaseClass;
import com.pom.Login_Navia_POM;

import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.When;

public class ScreenShort extends BaseClass {
	
	//public static ATUTestRecorder recorder;
	
	 Login_Navia_POM l;
	    WebDriverWait wait;
	    String otpValue;
	    
	    
	    @Before
	    public void beforeTest() throws ATUTestRecorderException {
	        l = new Login_Navia_POM(driver);
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        
//	        DateFormat dateFormat = new SimpleDateFormat("dd-yy-MMMM HH-mm-ss");
//			Date date = new Date();
//			recorder = new ATUTestRecorder("target/Video",
//					"PreMarket-" + dateFormat.format(date), false);
//			recorder.start();
	    }

	    // CHANGE: Screenshots now captured for ALL scenarios (pass + fail)
	    // Filename includes scenario name + PASSED/FAILED status + timestamp
	    // so attachments in email clearly show which scenarios passed and failed
	    @After
	    public void afterTest(Scenario scenario) throws ATUTestRecorderException {
	        String status = scenario.getStatus().toString().toUpperCase();
	        takeScreenshot(scenario.getName() + "_" + status);
	        if (scenario.isFailed()) {
	            System.out.println("[FAILED] " + scenario.getName());
	        } else {
	            System.out.println("[PASSED] " + scenario.getName());
	        }
	        
//	        recorder.stop();
//			driver.manage().deleteAllCookies();
	    }

	    

	    // FIX: waitForPageReady now uses actual Navia app element XPaths.
	    // Previous version used //nav | //header which don't exist on this site,
	    // causing a guaranteed 30-second timeout before every scenario.
	    public void waitForPageReady() {
	        try {
	            new WebDriverWait(driver, Duration.ofSeconds(10)).until(
	                d -> ((JavascriptExecutor) d)
	                        .executeScript("return document.readyState").equals("complete")
	            );

	            new WebDriverWait(driver, Duration.ofSeconds(5)).until(
	                ExpectedConditions.or(
	                    ExpectedConditions.presenceOfElementLocated(
	                        By.xpath("//input[@id='project-id']")),
	                    ExpectedConditions.presenceOfElementLocated(
	                        By.xpath("//a[@class='menu_tab0 right ntool-tip']")),
	                    ExpectedConditions.presenceOfElementLocated(
	                        By.xpath("//div[@class='user-name']")),
	                    ExpectedConditions.presenceOfElementLocated(
	                        By.xpath("//button[@data-dhx-id='btn_addmoney']")),
	                    ExpectedConditions.presenceOfElementLocated(
	                        By.xpath("//li[@class='widgets_mf']")),
	                    ExpectedConditions.presenceOfElementLocated(
	                        By.xpath("//label[text()='Dashboard']")),
	                    ExpectedConditions.presenceOfElementLocated(
	                        By.xpath("//div[@class='selected']"))
	                )
	            );
	            System.out.println("[INFO] Page ready.");
	            driver.navigate().refresh();

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
