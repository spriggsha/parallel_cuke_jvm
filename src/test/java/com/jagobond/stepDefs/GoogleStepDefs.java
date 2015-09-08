package com.jagobond.stepDefs;

import com.jagobond.resources.pages.*;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GoogleStepDefs {

    BrowserStepDefs browserStepDefs;
    RemoteWebDriver driver;
    String browser;
    GooglePage googlePage;


    public GoogleStepDefs(BrowserStepDefs browserStepDefs){
        this.browserStepDefs = browserStepDefs;
        this.driver = browserStepDefs.webDriverWrapper.getDriver();
        this.browser = browserStepDefs.webDriverWrapper.getBrowser();
    }


    ////////////// WHEN STATEMENTS //////////////////

    @When("^I navigate to a Google search page$")
    public void I_navigate_to_a_fastFT_index_page() throws Throwable {
        driver.get("http://www.google.com");
        googlePage = new GooglePage(driver,browser);
    }


    ////////////// THEN STATEMENTS //////////////////

    @Then("^the google search page elements are displayed$")
    public void the_google_search_elements_are_displayed() throws Throwable {
        googlePage.verifyAllElementsDisplayed();
    }

}
