package com.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.baseclass.BaseClass;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/feature/Mutual_Fund.feature",
    glue = {"com.stepdefinition"},
    plugin = {
        "pretty",
        "html:target/cucumber-report.html",
        "json:target/cucumber.json",
        "junit:target/cucumber.xml"
    }
)
public class Runner {

    public static WebDriver driver;

    @BeforeClass
    public static void browserLaunch() {
        driver = BaseClass.launchBrowser("Chrome");
    }

    @AfterClass
    public static void afterSuite() {
       //BaseClass.quitBrowser();
    }
}