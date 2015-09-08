package com.jagobond.resources.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class BasePage {

    protected WebDriver driver;
    protected String browser;

    public BasePage(WebDriver driver, String browser){
        this.driver = driver;
        this.browser = browser;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "body")
    WebElement body;

    protected int getLeftPosition(WebElement element) {
        return element.getLocation().getX();
    }

    protected int getRightPosition(WebElement element) {
        return element.getLocation().getX() + element.getSize().getWidth();
    }

    protected int getTopPosition(WebElement element) {
        return element.getLocation().getY();
    }

    protected int getBottomPosition(WebElement element) {
        return element.getLocation().getY() + element.getSize().getHeight();
    }

    public void verifyBodyDisplayed(){
        assert(body.isDisplayed());
    }

}
