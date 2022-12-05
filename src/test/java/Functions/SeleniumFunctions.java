package Functions;


import StepDefinitions.Hooks;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ISelect;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class SeleniumFunctions {

    WebDriver driver;
    public SeleniumFunctions(){driver = Hooks.driver;
    }

    /******** Page Path ********/
    public static String FileName = "";
    public static String PagesFilePath = "src/test/resources/Pages/";

    /*****test properties config*****/
    public static Properties prop= new Properties();
    public static InputStream in = SeleniumFunctions.class.getResourceAsStream("../test.properties");

    public static int EXPLICIT_TIMEOUT=15;
    public static boolean isDisplayed = Boolean.parseBoolean(null);

    /*****Scenario test data*****/
    public static Map<String, String> ScenaryData = new HashMap<>();
    public static String Environment ="";
    public static Map<String, String> HandleMyWindows = new HashMap<>();

    public static String GetFieldBy = "";
    public static String ValueToFind = "";

    public static String ElementText="";

    /******** Log Attribute ********/
    private static Logger log = Logger.getLogger(SeleniumFunctions.class);


    public static Object readJson() throws Exception {
        FileReader reader = new FileReader(PagesFilePath + FileName);
        try {

            if (reader != null) {
                JSONParser jsonParser = new JSONParser();
                return jsonParser.parse(reader);
            } else {
                return null;
            }
        } catch (FileNotFoundException | NullPointerException e) {
            log.error("ReadEntity: No existe el archivo " + FileName);
            throw new IllegalStateException("ReadEntity: No existe el archivo " + FileName, e);
        }

    }

    public static JSONObject ReadEntity(String element) throws Exception {
        JSONObject Entity = null;
        JSONObject jsonObject = (JSONObject) readJson();
        Entity = (JSONObject) jsonObject.get(element);
        log.info(Entity.toJSONString());
        return Entity;

    }

    public static By getCompleteElement(String element) throws Exception {
        By result = null;
        JSONObject Entity = ReadEntity(element);

        GetFieldBy = (String) Entity.get("GetFieldBy");
        ValueToFind = (String) Entity.get("ValueToFind");

        if ("className".equalsIgnoreCase(GetFieldBy)) {
            result = By.className(ValueToFind);
        } else if ("cssSelector".equalsIgnoreCase(GetFieldBy)) {
            result = By.cssSelector(ValueToFind);
        } else if ("id".equalsIgnoreCase(GetFieldBy)) {
            result = By.id(ValueToFind);
        } else if ("linkText".equalsIgnoreCase(GetFieldBy)) {
            result = By.linkText(ValueToFind);
        } else if ("name".equalsIgnoreCase(GetFieldBy)) {
            result = By.name(ValueToFind);
        } else if ("link".equalsIgnoreCase(GetFieldBy)) {
            result = By.partialLinkText(ValueToFind);
        } else if ("tagName".equalsIgnoreCase(GetFieldBy)) {
            result = By.tagName(ValueToFind);
        } else if ("xpath".equalsIgnoreCase(GetFieldBy)) {
            result = By.xpath(ValueToFind);
        }
        return result;
    }
      public String readProperties(String property) throws IOException{
        prop.load(in);
        return prop.getProperty(property);
    }
//guardar dato en un escenario
    public void SaveInScenario(String key, String text) {

        if (!this.ScenaryData.containsKey(key)) {
            this.ScenaryData.put(key,text);
            log.info(String.format("Save as Scenario Context key: %s with value: %s ", key,text));
        } else {
            this.ScenaryData.replace(key,text);
            log.info(String.format("Update Scenario Context key: %s with value: %s ", key,text));
        }

    }
//traer dato parammetrizado
    public void RetriveTestData(String parameter) throws IOException {
        Environment = readProperties("Environment");
        try {
            SaveInScenario(parameter, readProperties(parameter+"."+Environment));
            System.out.println("este es el valor de la parametrizacion: "+parameter+": "+this.ScenaryData.get(parameter));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //capturar una caja de texto y escribir en ella
    public void iSetElementWithKeyValue(String element, String key) throws Exception {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        boolean exist = this.ScenaryData.containsKey(key);
        if (exist){
            String text = this.ScenaryData.get(key);
            driver.findElement(SeleniumElement).sendKeys(text);
            log.info(String.format("Set on element %s with text %s", element, text));
            System.out.println(String.format("Set on element %s with text %s", element, text));
        }else{
            Assert.assertTrue(String.format("The given key %s do not exist in Context", key), this.ScenaryData.containsKey(key));
        }

    }

       //capturar dropbox
    public ISelect selectOption (String element)throws Exception{
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        log.info(String.format("Waiting element: %s",element));
        Select opt=new Select(driver.findElement(SeleniumElement));
        return opt;

    }

      //Expected Conditions - esperar si el elemento esta presente
    public void waitForElementPresent(String element) throws Exception
    {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        WebDriverWait w = new WebDriverWait(driver, EXPLICIT_TIMEOUT);
        log.info("Waiting for the element: "+element + " to be present");
        w.until(ExpectedConditions.presenceOfElementLocated(SeleniumElement));

    }

    //Expected Conditions - esperar si el elemento esta visible
    public void waitForElementVisible(String element) throws Exception
    {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        WebDriverWait w = new WebDriverWait(driver, EXPLICIT_TIMEOUT);
        log.info("Waiting for the element: "+element+ " to be visible");
        w.until(ExpectedConditions.visibilityOfElementLocated(SeleniumElement));
    }

    //Expected Conditions - esperar si el elñementoo fue cicleado
    public void waitForElementCliqueable(String element) throws Exception
    {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        WebDriverWait w = new WebDriverWait(driver, EXPLICIT_TIMEOUT);
        log.info("Waiting for the element: "+element+ " to be visible");
        w.until(ExpectedConditions.elementToBeClickable(SeleniumElement));
    }



    public void switchToFrame(String Frame) throws Exception {

        By SeleniumElement = SeleniumFunctions.getCompleteElement(Frame);
        log.info("Switching to frame: " + Frame);
        driver.switchTo().frame(driver.findElement(SeleniumElement));

    }

    public void switchToParentFrame() {

        log.info("Switching to parent frame");
        driver.switchTo().parentFrame();

    }

    public void checkCheckbox(String element) throws Exception
    {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        boolean isChecked = driver.findElement(SeleniumElement).isSelected();
        if(!isChecked){
            log.info("Clicking on the checkbox to select: " + element);
            driver.findElement(SeleniumElement).click();
        }
    }

    public void UncheckCheckbox(String element) throws Exception
    {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        boolean isChecked = driver.findElement(SeleniumElement).isSelected();
        if(isChecked){
            log.info("Clicking on the checkbox to select: " + element);
            driver.findElement(SeleniumElement).click();
        }
    }

    public void ClickJSElement(String element) throws Exception
    {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        log.info("Click to element with: " + element);
        jse.executeScript("arguments[0].click()", driver.findElement(SeleniumElement));

    }

    public void scrollPage(String to) throws Exception
    {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        if(to.equals("top")){
            log.info("Scrolling to the top of the page");
            jse.executeScript("scroll(0, -250);");

        }
        else if(to.equals("end")){
            log.info("Scrolling to the end of the page");
            jse.executeScript("scroll(0, 250);");
        }
    }

    public void scrollToElement(String element) throws Exception
    {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        log.info("Scrolling to element: " + element);
        jse.executeScript("arguments[0].scrollIntoView();", driver.findElement(SeleniumElement));

    }

    public void page_has_loaded (){
        String GetActual = driver.getCurrentUrl();
        System.out.println(String.format("Checking if %s page is loaded.", GetActual));
        log.info(String.format("Checking if %s page is loaded.", GetActual));
        new WebDriverWait(driver, EXPLICIT_TIMEOUT).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public void OpenNewTabWithURL(String URL)
    {
        log.info("Open New tab with URL: " + URL);
        System.out.println("Open New tab with URL: " + URL);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript(String.format("window.open('%s','_blank');", URL));

    }


    public void WindowsHandle(String WindowsName){
        if (this.HandleMyWindows.containsKey(WindowsName)) {
            driver.switchTo().window(this.HandleMyWindows.get(WindowsName));
            log.info(String.format("I go to Windows: %s with value: %s ", WindowsName ,this.HandleMyWindows.get(WindowsName)));
        } else {
            for(String winHandle : driver.getWindowHandles()){
                this.HandleMyWindows.put(WindowsName,winHandle);
                System.out.println("The New window "+ WindowsName + " is saved in scenario with value " + this.HandleMyWindows.get(WindowsName));
                log.info("The New window "+ WindowsName + " is saved in scenario with value " + this.HandleMyWindows.get(WindowsName));
                driver.switchTo().window(this.HandleMyWindows.get(WindowsName));
            }

        }
    }

    public void AcceptAlert(String want)
    {
        try{
            WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_TIMEOUT);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println(alert.getText());
            ElementText=alert.getText();
            if (want=="accept"){
                alert.accept();
            }else{
                alert.dismiss();
            }

            log.info("The alert was accepted successfully.");
        }catch(Throwable e){
            log.error("Error came while waiting for the alert popup. "+e.getMessage());
        }
    }

    public void ScreenShot(String TestCaptura) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
        String screenShotName = readProperties("ScreenShotPath") + "\\" + readProperties("browser") + "\\" + TestCaptura + "_(" + dateFormat.format(GregorianCalendar.getInstance().getTime()) + ")";
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        log.info("Screenshot saved as:" + screenShotName);
        FileUtils.copyFile(scrFile, new File(String.format("%s.png", screenShotName)));
    }

    public String GetTextElement(String element) throws Exception {
        By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_TIMEOUT);
        wait.until(ExpectedConditions.presenceOfElementLocated(SeleniumElement));
        log.info(String.format("Esperando el elemento: %s", element));

        ElementText = driver.findElement(SeleniumElement).getText();

        return ElementText;

    }

    public void checkPartialTextElementNotPresent(String element,String text) throws Exception {
        ElementText = GetTextElement(element);

        boolean isFoundFalse = ElementText.indexOf(text) !=-1? true: false;
        Assert.assertFalse("Text is present in element: " + element + " current text is: " + ElementText, isFoundFalse);

    }

    public void checkPartialTextElementPresent(String element,String text) throws Exception {

        ElementText = GetTextElement(element);

        boolean isFound = ElementText.indexOf(text) !=-1? true: false;

        Assert.assertTrue("Text is not present in element: " + element + " current text is: " + ElementText, isFound);

    }


    public void checkTextElementEqualTo(String element,String text) throws Exception {

        ElementText = GetTextElement(element);

        Assert.assertEquals("Text is not present in element: " + element + " current text is: " + ElementText, text, ElementText);

    }
    public boolean isElementDisplayed(String element) throws Exception {

        try {
            By SeleniumElement = SeleniumFunctions.getCompleteElement(element);
            log.info(String.format("Waiting Element: %s", element));
            WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_TIMEOUT);
            isDisplayed = wait.until(ExpectedConditions.presenceOfElementLocated(SeleniumElement)).isDisplayed();
        }catch (NoSuchElementException | TimeoutException e){
            isDisplayed = false;
            log.info(e);
        }
        log.info(String.format("%s visibility is: %s", element, isDisplayed));
        System.out.println(String.format("%s visibility is: %s", element, isDisplayed));
        return isDisplayed;
    }

}
