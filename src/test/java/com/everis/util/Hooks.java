package com.everis.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.everis.pages.BasePage;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Hooks class integrates with JUnit and Cucumber lifecycle.
 * - Initializes reporting structure (ExtentReports).
 * - Manages WebDriver lifecycle (setup, quit).
 * - Handles before/after scenario actions including logging and screenshots.
 */
public class Hooks extends TestWatcher {

    private static WebDriver driver;
    private static ExtentReports extentReport;
    private static Scenario scenario;
    private static ExtentTest extentTest;

    public Hooks() {
        super();
    }

    /**
     * Executed before test execution starts.
     * Initializes reporting directories and ExtentReports.
     *
     * @param description JUnit test description
     */
    @Override
    protected void starting(Description description) {
        super.starting(description);

        new File("target/report/html/img").mkdirs();

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(
                "target/report/html/" + description.getDisplayName().replace("tests.", "") + ".html"
        );
        htmlReporter.config().setEncoding("ISO-8859-1");

        extentReport = new ExtentReports();
        extentReport.attachReporter(htmlReporter);
        extentReport.setSystemInfo("os.name", System.getProperty("os.name"));
    }

    /**
     * Runs before each Cucumber scenario.
     * Creates a new ExtentTest instance and assigns scenario metadata.
     *
     * @param scenario the current Cucumber scenario
     */
    @Before
    public void beforeScenario(Scenario scenario) {
        Hooks.scenario = scenario;
        extentTest = extentReport.createTest("Scenario: " + scenario.getName(), scenario.getName());
        extentTest.assignCategory("feature:" + scenario.getId().replaceAll(";.*", ""));

        Collection<String> tags = scenario.getSourceTagNames();
        for (String tag : tags) {
            extentTest.assignCategory(tag);
        }

        System.out.println("Scenario: " + scenario.getName());
    }

    /**
     * Runs after each Cucumber scenario.
     * Logs failures with screenshots and flushes the ExtentReport.
     *
     * @throws IOException if screenshot saving fails
     */
    @After
    public void afterScenario() throws IOException {
        if (scenario.isFailed() && driver != null) {
            BasePage basePage = new BasePage();
            basePage.logPrintFail("The test has failed.");

            Throwable throwable = logError(scenario);
            extentTest.fail(throwable);
        }

        extentReport.flush();

        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Extracts error/exception information from a failed scenario
     * using reflection (Cucumber internal API).
     *
     * @param scenario the failed scenario
     * @return Throwable error if found, otherwise null
     */
    private Throwable logError(Scenario scenario) {
        try {
            Field field = FieldUtils.getField(Scenario.class, "delegate", true);
            final TestCaseState testCase = (TestCaseState) field.get(scenario);

            Method getError = MethodUtils.getMatchingMethod(testCase.getClass(), "getError");
            getError.setAccessible(true);

            return (Throwable) getError.invoke(testCase);
        } catch (Exception e) {
            System.err.println("Error retrieving exception: " + e);
            return null;
        }
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);
    }

    // ===== Getters =====

    public static WebDriver getDriver() { return driver; }

    public static ExtentTest getExtentTest() { return extentTest; }

    public static Scenario getScenario() { return scenario; }

    public static ExtentReports getExtentReports() { return extentReport; }

    // ===== Browser Navigation =====

    /**
     * Opens Chrome browser with predefined options and navigates to the given URL.
     *
     * @param url the target website URL
     */
    public static void navigateToULRChrome(String url) {
        String downloadFilepath = System.getProperty("user.dir") + "/target/temp";

        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", downloadFilepath);
        chromePrefs.put("credentials_enable_service", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("disable-infobars");
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.navigate().to(url);
    }

    /**
     * Opens Chrome browser in mobile emulation mode and navigates to the given URL.
     *
     * @param url the target website URL
     */
    public static void navigateToULRChromeMobile(String url) {
        String downloadFilepath = System.getProperty("user.dir") + "/target/temp";

        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", downloadFilepath);
        chromePrefs.put("credentials_enable_service", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("disable-infobars", "--disable-print-preview");

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone X");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.navigate().to(url);
    }
}
