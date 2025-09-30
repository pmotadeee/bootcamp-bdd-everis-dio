package com.everis.pages;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import com.aventstack.extentreports.*;
import com.everis.util.Hooks;
import com.everis.util.Utils;

public class BasePage {

    protected WebDriver driver = Hooks.getDriver();
    protected ExtentTest extentTest = Hooks.getExtentTest();
    protected ExtentReports extentReport = Hooks.getExtentReports();

    public BasePage() { }

    protected void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    protected void waitMilliseconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    protected WebElement waitElement(By by, int timeOutInSeconds) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
                .pollingEvery(200, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement waitElement(WebElement webElement, int timeOutInSeconds) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
                .pollingEvery(10, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotVisibleException.class);
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    protected List<WebElement> waitElements(By by, int timeOutInSeconds) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
                .pollingEvery(10, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    protected boolean waitNotPresent(By by, int timeOutInSeconds) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean waitUntilElementHasValue(WebElement element, String text) {
        try {
            waitMilliseconds(500);
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(1, TimeUnit.SECONDS)
                    .pollingEvery(200, TimeUnit.MILLISECONDS)
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);
            return wait.until(ExpectedConditions.textToBePresentInElementValue(element, text));
        } catch (Exception e) {
            return false;
        }
    }

    protected void moveToElement(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }

    protected boolean isElementDisplayed(By by) {
        return driver.findElements(by).stream()
                .findFirst()
                .map(WebElement::isDisplayed)
                .orElse(false);
    }

    protected void aguardarLoading() {
        try {
            waitElement(By.id("loading"), 3);
        } catch (Exception ignored) { }
        waitNotPresent(By.id("loading"), 120);
    }

    private String saveScreenshotInRelatoriosPath() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmssSSS"));
        String screenshotName = timestamp + ".png";
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("target/report/html/img/" + screenshotName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return screenshotName;
    }

    protected void log(String log) {
        String screenshotName = saveScreenshotInRelatoriosPath();
        try {
            extentTest.pass(log, MediaEntityBuilder
                    .createScreenCaptureFromPath("img/" + screenshotName)
                    .build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logPrintFail(String log) {
        String screenshotName = saveScreenshotInRelatoriosPath();
        try {
            extentTest.fail(log, MediaEntityBuilder
                    .createScreenCaptureFromPath("img/" + screenshotName)
                    .build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void logInfo(String log) { extentTest.info(log); }

    protected void logSkip(String log) { extentTest.skip(log); }

    protected void logFail(String log) { extentTest.fail(log); }

    protected void logError(String log) { extentTest.error(log); }

    protected void logPass(String log) { extentTest.pass(log); }

    protected ExtentTest createChildStart(String testName) {
        return Hooks.getExtentTest().createNode(testName);
    }

    protected void childLogFail(ExtentTest child, String log) { child.fail(log); }

    protected void childLogPass(ExtentTest child, String log) { child.pass(log); }

    protected void childLogInfo(ExtentTest child, String log) { child.info(log); }

    public void aguardarDownloadArquivo() {
        String downloadPath = System.getProperty("user.dir") + "/target/temp";
        Utils.waitForFileExistsInPath(downloadPath, 10);
        waitMilliseconds(500);
    }
}
