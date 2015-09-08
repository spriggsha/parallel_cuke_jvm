package com.jagobond.resources;

import org.openqa.selenium.Platform;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.*;
import java.util.concurrent.TimeUnit;


public class WebDriverWrapper {

    RemoteWebDriver driver;
    String browser;
    URL hubURL;

    // Change me to match the location of your Selenium Grid hub (locahost is an option)
    String hubLocation = "http://url:port/wd/hub";

    public WebDriverWrapper(String browser) {
        this.browser = browser;
        DesiredCapabilities dc = new DesiredCapabilities();

        try {
            hubURL = new URL(hubLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Change or add here, matching the cases up with the nodes available on your Grid
        switch (browser) {
            case "chrome":
                dc.setBrowserName("chrome");
                dc.setVersion("40");
                dc.setPlatform(Platform.WINDOWS);
                break;

            case "ie8":
                dc.setBrowserName("internet explorer");
                dc.setVersion("8");
                dc.setPlatform(Platform.WINDOWS);
                dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                break;

            case "ie9":
                dc.setBrowserName("internet explorer");
                dc.setVersion("9");
                dc.setPlatform(Platform.WINDOWS);
                dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                break;

            case "ie10":
                dc.setBrowserName("internet explorer");
                dc.setVersion("10");
                dc.setPlatform(Platform.WINDOWS);
                dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                break;

            case "ie11":
                dc.setBrowserName("internet explorer");
                dc.setVersion("11");
                dc.setPlatform(Platform.WINDOWS);
                dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                break;

            case "firefox":
                FirefoxProfile profile = new FirefoxProfile();
                dc = DesiredCapabilities.firefox();
                dc.setCapability(FirefoxDriver.PROFILE, profile);
                dc.setBrowserName("firefox");
                dc.setVersion("40");
                dc.setPlatform(Platform.WINDOWS);
                break;

            case "nojs":
                FirefoxProfile fp = new FirefoxProfile();
                fp.setPreference("javascript.enabled", false);
                dc.setBrowserName("firefox");
                dc.setVersion("40");
                dc.setPlatform(Platform.WINDOWS);
                dc.setCapability(FirefoxDriver.PROFILE, fp);
                break;

        }

        driver = new RemoteWebDriver(hubURL, dc);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    public String getBrowser() {
        return browser;
    }

}
