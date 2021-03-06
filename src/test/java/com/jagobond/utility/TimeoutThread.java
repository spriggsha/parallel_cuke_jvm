package com.jagobond.utility;

import org.openqa.selenium.TimeoutException;

import java.awt.*;
import java.awt.event.KeyEvent;


public class TimeoutThread extends Thread {

    final private int timeout;

    public TimeoutThread(int seconds) {
        this.timeout = seconds;
    }

    public void run() {
        try {
            Thread.sleep(timeout * 1000);

            /* Timeout occurred */
            ThreadReturn.save(new TimeoutException("Time out occured waiting for page to load"));

            /* Stop WebDriver.get */
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);

        } catch (InterruptedException ex) {
            return;
        } catch (AWTException ex) {
            System.out.println("Error occurned pressing ESC");
            ex.printStackTrace();
        }

    }
}