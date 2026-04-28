package com.stepdefinition;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.baseclass.BaseClass;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class Login_Page extends BaseClass {


	@Given("User Navigate to Navia")
	public void user_navigate_to_navia() throws InterruptedException, AWTException {
		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		driver.get("https://yopmail.com/");

//		String currentUrl = driver.getCurrentUrl();
//		System.out.println(currentUrl);
		WebElement yopMail = driver.findElement(By.xpath("//input[@placeholder='Enter your inbox here']"));
		yopMail.sendKeys("naviatestingntp@yopmail.com");
		
		Thread.sleep(2000);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);


		
//		driver.findElement(By.xpath("(//div[@class='txtlien'])[1]")).click();
//		
//	
//		Thread.sleep(1000);
//		driver.navigate().back();
		
		Thread.sleep(2000);
		driver.findElement(By.xpath("//i[@class='material-icons-outlined f36']")).click();
		Thread.sleep(2000);
		WebElement createAccount = driver.findElement(By.xpath("//a[@title='YOPmail - Temporary email']"));
		Actions ac = new Actions(driver);
		Thread.sleep(2000);
		ac.contextClick(createAccount).perform();

		Thread.sleep(1000);
		//Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);

		Thread.sleep(1000);
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);

		Thread.sleep(1000);
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);
		
		Thread.sleep(1000);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);

		Thread.sleep(8000);

		Set<String> windowHandles = driver.getWindowHandles();
		ArrayList<String> li = new ArrayList<String>(windowHandles);
		int size = li.size();
		System.out.println(size);

		driver.switchTo().window(li.get(1));

		driver.navigate().refresh();

		getUrl("https://web.navia.co.in/login.php");
		

		Thread.sleep(2000);

		try {
			
			WebDriverWait wait1 = new WebDriverWait(driver, java.time.Duration.ofSeconds(1));
			WebElement element2 = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Login with client code')]")));
			JavascriptExecutor js = (JavascriptExecutor) driver;
		       js.executeScript("arguments[0].click();", element2);
			sleep(3000);
			
		} catch (Exception e) {
			
			WebDriverWait wait1 = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
			WebElement element2 = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[@id='login_fsmt1'])[1]")));
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
		       js.executeScript("arguments[0].click();", element2);
			sleep(3000);
		}
		
		
	
		
		driver.findElement(By.xpath("//input[@name='clientCode']")).click();
		sleep(1000);
		driver.findElement(By.xpath("//input[@name='clientCode']")).sendKeys("63748379");
		sleep(2000);
		
		driver.findElement(By.xpath("//input[@name='lPassword']")).click();
		sleep(1000);
		driver.findElement(By.xpath("//input[@name='lPassword']")).sendKeys("Navia@123");
		sleep(1000);
		


		sleep(2000);
		driver.findElement(By.xpath("//input[@onclick='GetTOTP()']")).click();
		sleep(2000);


		driver.switchTo().window(li.get(0));

		Thread.sleep(28000);

		WebElement refresh = driver.findElement(By.xpath("//button[@id='refresh']"));
		refresh.click();
		
		Thread.sleep(2000);
		refresh.click();
		WebElement iframe = driver.findElement(By.xpath("//iframe[@id='ifmail']"));

		driver.switchTo().frame(iframe);

		Thread.sleep(6000);

WebDriverWait wait7 = new WebDriverWait(driver, java.time.Duration.ofSeconds(120));
WebElement otp = wait7.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='mail']//child::pre")));


String text2 = otp.getText();

Pattern otpPattern = Pattern.compile("\\b\\d{6}\\b");
Matcher matcher = otpPattern.matcher(text2);

// Extract and print OTP
if (matcher.find()) {
    String text = matcher.group();
    System.out.println("Extracted OTP: " + text);
    
    driver.switchTo().defaultContent();

	Thread.sleep(4000);

		driver.switchTo().window(li.get(1));
		Thread.sleep(3000);

		driver.findElement(By.xpath("//input[@name='usertotp']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@name='usertotp']")).sendKeys(text);

		Thread.sleep(1000);
		driver.switchTo().window(li.get(0));
		Thread.sleep(1000);
		driver.close();
		Thread.sleep(1000);
		driver.switchTo().window(li.get(1));
		Thread.sleep(3000);
		driver.findElement(By.xpath("//button[@id='login_fsmt']")).click();
		Thread.sleep(2000);
		
      } else {
          System.err.println("OTP not found in the message.");
      }

//		
		Thread.sleep(4000);


	}

	@When("User Click login with client code")
	public void user_click_login_with_client_code() throws InterruptedException {
		System.out.println("User Click login with client code");
	}

	@When("User Enter Client Code")
	public void user_enter_client_code() throws InterruptedException {
		System.out.println("User Enter Client Code");

	}

	@When("User  Enter Password")
	public void user_enter_password() throws InterruptedException {

		System.out.println("User Enter Password");

	}

	@When("User Click Agree CheckBox")
	public void user_click_agree_check_box() {
		System.out.println("User Click Agree CheckBox");

	}

	@When("User Click Login button")
	public void user_click_login_button() throws InterruptedException, AWTException {

		System.out.println("User Click Login button");

	}

	@When("User Click Otp Verification and enter manualy")
	public void user_click_otp_verification_and_enter_manualy() throws InterruptedException {

		System.out.println("User Click Otp Verification and enter manualy");

	}

	@When("User Click Login Again")
	public void user_click_login_again() throws InterruptedException {

	Thread.sleep(2000);
		//clickOnElement(l.getLoginAfterOTP());
//		setImplicitWait(24);
//	driver.findElement(By.xpath("//button[@data-dhx-id='form_button_1']")).click();
	
	try {
		
		WebElement textBox = driver.findElement(By.xpath("//h2[text()='RISK DISCLOSURES ON DERIVATIVES ']"));

		Thread.sleep(2000);

		if (textBox.isDisplayed()) {
			Thread.sleep(2000);
			driver.findElement(By.xpath("//span[text()='Agree']//parent::button")).click();
			
			System.out.println("Login Page");


		}

		else {
			System.out.println("Not Preset in Agree Page");

		}
		
		
	} catch (Exception e) {
		
		System.err.println("Not Visiable in Agree page");
		
	}



	}
	
	
	
}
