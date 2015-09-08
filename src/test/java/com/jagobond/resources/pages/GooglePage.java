package com.jagobond.resources.pages;


import com.jagobond.resources.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class GooglePage extends BasePage{

    public GooglePage(WebDriver driver, String browser){
        super(driver, browser);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "div#hplogo")
    WebElement logo;

    @FindBy(css = "input.gsfi")
    WebElement search_field;

    @FindBy(css = "input[name=btnK]")
    WebElement search_button;

    @FindBy(css = "input[name=btnI]")
    WebElement feeling_luck_button;


    public void verifyAllElementsDisplayed(){
        assert(logo.isDisplayed());
        assert(search_field.isDisplayed());
        assert(search_button.isDisplayed());
        assert(feeling_luck_button.isDisplayed());

        System.out.println(browser);
    }


}
