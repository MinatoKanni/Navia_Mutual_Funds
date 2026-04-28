package com.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Login_Navia_POM {
	
	public static WebDriver driver;
	
	@FindBy(xpath="//button[@onclick='LoginScreen()']")
	private WebElement loginWithClientCode;
	
	@FindBy(xpath="//input[@id='clientCode']")
	private WebElement clientCode;
	
	@FindBy(xpath="//input[@id='userpassword']")
	private WebElement passWord;
	
	@FindBy(xpath="(//input[@type='checkbox'])[1]")
	private WebElement checkBox;
	
	@FindBy(xpath="(//button[text()='Login '])[1]")
	private WebElement login;

	@FindBy(xpath="//input[@id='userotp']")
	private WebElement otpClick;

	
	@FindBy(xpath="(//button[@value='Login'])[3]")
	private WebElement loginAfterOTP;
	
	
	public Login_Navia_POM(WebDriver driver2) {
		
		this.driver=driver2;
		PageFactory.initElements(driver2, this);
	}

	public WebElement getLoginWithClientCode() {
		return loginWithClientCode;
	}

	public void setLoginWithClientCode(WebElement loginWithClientCode) {
		this.loginWithClientCode = loginWithClientCode;
	}

	public WebElement getClientCode() {
		return clientCode;
	}

	public void setClientCode(WebElement clientCode) {
		this.clientCode = clientCode;
	}

	public WebElement getPassWord() {
		return passWord;
	}

	public void setPassWord(WebElement passWord) {
		this.passWord = passWord;
	}

	public WebElement getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(WebElement checkBox) {
		this.checkBox = checkBox;
	}

	public WebElement getLogin() {
		return login;
	}

	public void setLogin(WebElement login) {
		this.login = login;
	}

	public WebElement getOtpClick() {
		return otpClick;
	}

	public void setOtpClick(WebElement otpClick) {
		this.otpClick = otpClick;
	}

	public WebElement getLoginAfterOTP() {
		return loginAfterOTP;
	}

	public void setLoginAfterOTP(WebElement loginAfterOTP) {
		this.loginAfterOTP = loginAfterOTP;
	}
	

}
