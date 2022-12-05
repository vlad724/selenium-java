package StepDefinitions;

import Functions.SeleniumFunctions;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.junit.Assert;
import Functions.CreateDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class StepDefinitions {


    WebDriver driver;
    SeleniumFunctions functions = new SeleniumFunctions();
    public static boolean  Actual = Boolean.parseBoolean(null);
    /******** Log Attribute ********/
    Logger log = Logger.getLogger(StepDefinitions.class);


    public StepDefinitions() {
        driver = Hooks.driver;
    }


    @Given("^I am in App main site")
    public void iAmInAppMainSite() throws IOException, InterruptedException {

        String url = functions.readProperties("MainAppUrlBase");
        log.info("Navigate to: " + url);
        driver.get(url);
        Thread.sleep(1000);
        functions.page_has_loaded();//metodo creado en la clase SeleniumFunctions para verficar si la pagina esta cargada correctamente
    }

    @Given("^I go to site (.*)")
    public void IGoToSite(String URL) throws InterruptedException {
        log.info("Navigate to: " + URL);
        driver.get(URL);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//tiempo de espera de la prueba
        Thread.sleep(1000);//tiempo para que la prueba no cierre rapido
        functions.page_has_loaded();//metodo creado en la clase SeleniumFunctions para verficar si la pagina esta cargada correctamente
        functions.WindowsHandle("Principal");
    }

    @Then("I quit the applications$")
    public void quitApp() {
        driver.quit();//cierro el driver(finalizo el testing)
    }

    @Then("I close the window")
    public void closeApp() {
        driver.close();//cierro la ventana
    }

    @Then("I load the DOM Information (.*)$")
    public void iLoadTheDOMInformation(String json) throws Exception {
        SeleniumFunctions.FileName = json;
        SeleniumFunctions.readJson();
        log.info("initialize file: " + json);


    }

    @Then("I do a click in element (.*)")
    public void iDoAClickInElement(String locator) throws Exception {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(locator);
       functions.waitForElementCliqueable(locator);
        driver.findElement(SeleniumElement).click();
        log.info("Click on element by " + locator);

    }

    /** I click in JS element. */
    @And("^I click in JS element (.+)$")
    public void ClickJSElement(String element) throws Exception
    {
        functions.ClickJSElement(element);
    }

    @And("I set (.*) with text (.*)")
    public void iSetWithText(String element, String text) throws Exception {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        driver.findElement(SeleniumElement).sendKeys(text);
        log.info("Send text" + text + " to element " + element);

    }

    @Given("I set (.*) value in Data Scenario")
    public void iSetUserEmailValueInDataScenario(String parameter) throws IOException {
        functions.RetriveTestData(parameter);
    }

    @And("^I Save text of (.*?) as Scenario Context$")
    public void iSaveTextOfElementAsScenarioContext(String element) throws Exception {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        String ScenarioElementText = driver.findElement(SeleniumElement).getText();
        functions.SaveInScenario(element + ".text", ScenarioElementText);

    }

    @And("I set (.*) with key value (.*)")
    public void iSetWithKeyValue(String element, String key) throws Exception {
        functions.iSetElementWithKeyValue(element, key);

    }

    @And("I set text (.*) in dropdown (.*)")
    public void iSetTextInDropdown(String option, String element) throws Exception {
        Select opt = (Select) functions.selectOption(element);
        opt.selectByVisibleText(option);
    }

    @And("I set index (.*) in dropdown (.*)")
    public void iSetIndexInDropdown(int option, String element ) throws Exception {
        Select opt = (Select) functions.selectOption(element);
        opt.selectByIndex(option);
    }

    /** Wait for an element to be present for a specific period of time */
    @Then("^I wait for element (.*) to be present$")
    public void waitForElementPresent(String element) throws Exception
    {
        functions.waitForElementPresent(element);
    }

    /** Wait for an element to be visible for a specific period of time */
    @Then("^I wait element (.*?) to be visible$")
    public void waitForElementVisible(String element) throws Exception
    {
        functions.waitForElementVisible(element);
    }

    @Then("I check if (.*?) error message is (.*)")
    public void iCheckIfErrorMessageIs(String element, String state) throws Exception {
     boolean  Actual=  functions.isElementDisplayed(element);
     Assert.assertEquals("El estado es diferente al esperado", Actual,Boolean.valueOf(state));

    }


    @And("I switch to Frame: (.*)")
    public void iSwitchToFrame(String frame) throws Exception {
        functions.switchToFrame(frame);
    }

    @And("I switch to parent frame")
    public void iSwitchToParentFrame() {
        functions.switchToParentFrame();
    }

    /** Check an option from a checkbox */
    @When("^I check the checkbox having (.*?)$")
    public void checkCheckbox(String element) throws Exception
    {
        functions.checkCheckbox(element);
    }

    /** Check an option from a checkbox */
    @When("^I Uncheck the checkbox having (.*?)$")
    public void UncheckCheckbox(String element) throws Exception
    {
        functions.UncheckCheckbox(element);
    }

    /** Scroll to an element. */
    @And("^I scroll to element (.+)$")
    public void scrollToElement(String element) throws Exception
    {
        functions.scrollToElement(element);
    }

    /** Scroll to the (top/end) of the page. */
    @And("^I scroll to (top|end) of page$")
    public void scrollPage(String to) throws Exception
    {
        functions.scrollPage(to);
    }

    @And("I wait site is loaded")
    public void iWaitSiteIsLoaded() {
        functions.page_has_loaded();;
    }

    @Given("^I open new tab with URL (.*)")
    public void OpenNewTabWithURL(String url){
        functions.OpenNewTabWithURL(url);
    }

    /** Switch to a new windows */
    @When("^I switch to new window$")
    public void switchNewWindow()
    {
        System.out.println(driver.getWindowHandles());
        for(String winHandle : driver.getWindowHandles()){
            System.out.println(winHandle);
            log.info("Switching to new windows");
            driver.switchTo().window(winHandle);
        }
    }

    /** Switch to a new windows */
    @When("^I go to (.*?) window$")
    public void switchNewNamedWindow(String WindowsName)
    {
        functions.WindowsHandle(WindowsName);
    }



    /** Handle and accept a alert */
    @Then("^I (accept|dismiss) alert$")
    public void AcceptAlert(String want)
    {
        functions.AcceptAlert(want);
    }

    @And("i take Screenshot: (.*?)")
    public void iTakeScreenshot(String TestCaptura) throws IOException {

        functions.ScreenShot(TestCaptura);
    }


    /** Assert Text is present be present*/
    @Then("^Assert if (.*) contains text (.*)$")
    public void checkPartialTextElementPresent(String element,String text) throws Exception {

        functions.checkPartialTextElementPresent(element, text);

    }



    @Then("Assert if (.*) is equal to (.*)")
    public void assertIfEqualTo(String element, String text) throws Exception {

        functions.checkTextElementEqualTo(element, text);

    }

    @Then("Check if (.*) is NOT contains text (.*)")
    public void assertEmailErrorIfNOTContainsText(String element,String text) throws Exception {

        functions.checkPartialTextElementNotPresent(element, text);
    }

    @Then("^Assert if (.*?) is Displayed$")
    public void checkIfElementIsPresent(String element) throws Exception {

        boolean isDisplayed = functions.isElementDisplayed(element);
        Assert.assertTrue("Element is not present: " + element, isDisplayed);

    }


    @Then("^Check if (.*?) is NOT Displayed$")
    public void checkIfElementIsNotPresent(String element) throws Exception {

        boolean isDisplayed = functions.isElementDisplayed(element);
        Assert.assertFalse("Element is present: " + element, isDisplayed);
    }



}
