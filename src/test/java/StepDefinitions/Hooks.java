package StepDefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import Functions.CreateDriver;
import java.io.IOException;

public class Hooks {
    public static WebDriver driver;
    Logger log = Logger.getLogger(Hooks.class);

    Scenario scenario = null;

    @Before
    public void initDriver(Scenario scenario) throws IOException {
        log.info("***********************************************************************************************************");
        log.info("[ Configuration ] - Initializing driver configuration");
        log.info("***********************************************************************************************************");
        driver = CreateDriver.initConfig();
        this.scenario = scenario;
        log.info("***********************************************************************************************************");
        log.info("[ Scenario ] - "+ scenario.getName());
        log.info("***********************************************************************************************************");
    }

    @After
    /**
     * Embed a screenshot in test report if test is marked as failed
     */
    public void tearDown( ){

        log.info("***********************************************************************************************************");
        log.info("[ Driver Status ] - Clean and close the intance of the driver");
        log.info("***********************************************************************************************************");
        driver.quit();

    }
}

