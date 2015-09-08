package com.jagobond.stepDefs;

import com.jagobond.resources.WebDriverWrapper;
import com.jagobond.resources.pages.BasePage;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;


public class BrowserStepDefs {

    WebDriverWrapper webDriverWrapper;
    RemoteWebDriver driver;
    BasePage basePage;



    //////////////     SETUP        //////////////////

    @Before
    public void initializeBrowserTest(){
        // any setup goes here
    }


    ////////////// GIVEN STATEMENTS //////////////////

    @Given("^I am using a (.*) browser$")
    public void I_am_using_a_typeOfBrowser_browser(String browser) throws Throwable {
        webDriverWrapper = new  WebDriverWrapper(browser);
        driver = webDriverWrapper.getDriver();
    }


    ////////////// WHEN STATEMENTS //////////////////

    @When("^I open a browser window of size (.*) by (.*)$")
    public void I_open_a_browser_window_of_size_width_by_height(int width, int height) throws Throwable {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    @When("^I change the browser size to (.*) by (.*)$")
    public void I_change_the_browser_size_to_width_by_height_(int width, int height) throws Throwable {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    @When("^I make the browser a small size$")
    public void I_make_the_browser_a_small_size() throws Throwable {
        driver.manage().window().setSize(new Dimension(500, 800));
    }

    @When("^I make the browser a medium size$")
    public void I_make_the_browser_a_medium_size() throws Throwable {
        driver.manage().window().setSize(new Dimension(1000, 800));
    }

    @When("^I make the browser a large size$")
    public void I_make_the_browser_a_large_size() throws Throwable {
        driver.manage().window().setSize(new Dimension(1200, 800));
    }

    @When("^I make the browser an extra large size$")
    public void I_make_the_browser_an_extra_large_size() throws Throwable {
        driver.manage().window().setSize(new Dimension(1650, 800));
    }

    @When("^I navigate back$")
    public void I_navigate_back() throws Throwable {
        driver.navigate().back();
    }


    ////////////// THEN STATEMENTS //////////////////

    @Then("^the body is displayed$")
    public void the_body_is_displayed() throws Throwable {
        basePage.verifyBodyDisplayed();
    }


    ////////////// CLEANUP //////////////////

    @After
    public void cleanUpBrowserTest(Scenario scenario) throws IOException, InterruptedException {

        if(scenario.isFailed()){

            try{
                WebDriver augmentedDriver = new Augmenter().augment(driver);
                byte[] screenshot = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot,"image/png");

            }catch (WebDriverException wde){
                System.out.println("TEST FAILED: TRIED TO GET A SCREENSHOT, BUT COULDN'T!");
                System.out.println(wde.getMessage());
            }
        }
    }

}
