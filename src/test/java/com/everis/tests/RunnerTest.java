package com.everis.tests;

import org.junit.ClassRule;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import com.everis.util.Hooks;

/**
 * RunnerTest is the entry point for executing Cucumber scenarios.
 * It configures the test execution options such as feature path,
 * tags, glue code, and reporting plugins.
 *
 * Uses JUnit's @RunWith to integrate with Cucumber.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features",                  // Path to the feature files
    tags = "@test",                                   // Tag filter for scenarios
    glue = {""},                                      // Package containing step definitions
    monochrome = true,                                // Cleaner console output
    dryRun = false,                                   // Validates steps without running if true
    plugin = {                                        // Reporting configuration
        "json:target/cucumber.json", 
        "rerun:target/rerun.txt"
    }
)
public class RunnerTest {

    /**
     * JUnit ClassRule to manage test lifecycle hooks.
     * Ensures setup and teardown defined in Hooks are applied.
     */
    @ClassRule
    public static Hooks hooks = new Hooks();
}
