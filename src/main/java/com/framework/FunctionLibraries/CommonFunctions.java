package com.framework.FunctionLibraries;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

import autoitx4java.AutoItX;

public class CommonFunctions {

	// Creating singleton object
	public WebDriver driver = null;
	public String strLogFileName = null;
	public boolean iStatus = true;
	private AutoItX objAutoIT = null;
	public ExtentReports extent;
	public ExtentTest test;
	public String strTestCaseName = null;
	public URL url;
	public HttpsURLConnection conn;
	public Connection objConnDB = null;
	public String strToken_SSO = null;
	public HttpResponse response;
	private Initializer initializer = null;
	private Stage stage = null;

	public CommonFunctions() {
	}

	public CommonFunctions(Initializer initializer, Stage stage) {
		this.initializer = initializer;
		this.stage = stage;
	}

	/**
	 * This method initialize the test object in CommonFunctions library
	 *
	 * @param test This is extentTest object, ExtentTest is used for report
	 *             generation.
	 *             @author Santhosh Karra
	 */
	public void init(ExtentReports extent) {
		this.extent = extent;
	}

	/**
	 * This method initialize the AutoIt object in Application functions library
	 *
	 * @param objAutoIT This is autoIt object
	 * @author Santhosh Karra
	 */
	public void init(AutoItX objAutoIT) {
		this.objAutoIT = objAutoIT;
	}

	/**
	 * Function Name : killbyProcess Description : This function kills the process
	 *
	 * @param strProcessName , process name to kill
	 * @author Santhosh Karra
	 */
	public void killbyProcess(String strProcessName) {
		String strProcess = null;

		try {
			switch (strProcessName.toUpperCase()) {
			case "IE":
				strProcess = "iexplore.exe";
				break;
			case "FIREFOX":
				strProcess = "firefox.exe";
				break;
			case "CHROME":
				strProcess = "chrome.exe";
				break;
			}
			Runtime.getRuntime().exec("taskkill /f /IM " + strProcess);
			initializer.wait(2);

		} catch (Exception e) {
			initializer.log("Issue on killing the process :" + strProcessName + ". Exception : " + e.getMessage());
		}
	}

	/**
	 * Function Name : getElement Description : This function generates web elements
	 * based on the property name using the web driver
	 *
	 * @param driver       Driver object for the browser
	 * @param propertyName Property name of the element to be constructed
	 * @return Returns an webelement
	 * @author Santhosh Karra
	 */
	public WebElement getElement(WebDriver driver, String propertyName) {
		WebElement element = null;

		String propertyValue = initializer.GetValue(propertyName).trim();
		String propertyType = initializer.GetType(propertyName).trim();
		try {
			switch (propertyType.toUpperCase()) {
			case "NAME":
				element = driver.findElement(By.name(propertyValue));
				break;
			case "ID":
				element = driver.findElement(By.id(propertyValue));
				break;
			case "CSS":
				element = driver.findElement(By.cssSelector(propertyValue));
				break;
			case "CLASSNAME":
				element = driver.findElement(By.className(propertyValue));
				break;
			case "XPATH":
				element = driver.findElement(By.xpath(propertyValue));
				break;
			case "LINKTEXT":
				element = driver.findElement(By.linkText(propertyValue));
				break;
			}
		} catch (Exception e) {
			initializer.failureCallwithException("Issue on getting element : " + propertyValue, e);
		}
		return element;
	}

	/**
	 * Function Name : getLocator Description : This function generates web element
	 * locator based on the property name using the web driver
	 *
	 * @param driver       Driver object for the browser
	 * @param propertyName Property name of the element to be constructed
	 * @return Returns locator
	 * @author Santhosh Karra
	 */
	public By getLocator(WebDriver driver, String propertyName) {
		By locator = null;

		String propertyValue = initializer.GetValue(propertyName).trim();
		String propertyType = initializer.GetType(propertyName).trim();

		try {
			switch (propertyType.toUpperCase()) {
			case "NAME":
				locator = By.name(propertyValue);
				break;
			case "ID":
				locator = By.id(propertyValue);
				break;
			case "CSS":
				locator = By.cssSelector(propertyValue);
				break;
			case "CLASSNAME":
				locator = By.className(propertyValue);
				break;
			case "XPATH":
				locator = By.xpath(propertyValue);
				break;
			case "LINKTEXT":
				locator = By.linkText(propertyValue);
				break;
			}
		} catch (Exception e) {
			initializer.failureCallwithException("Issue on getting locator : " + propertyValue, e);
		}
		return locator;
	}

	/**
	 * Function Name : getElements Description : This function generates web
	 * elements based on the property name using the web driver
	 *
	 * @param driver       Drive object for the browser
	 * @param propertyName Property name of the element to be constructed
	 * @return Returns an webelement
	 * @author Santhosh Karra
	 */
	public List<WebElement> getElements(WebDriver driver, String propertyName) {
		List<WebElement> element = null;
		String propertyValue = initializer.GetValue(propertyName).trim();
		String propertyType = initializer.GetType(propertyName).trim();
		try {
			switch (propertyType.toUpperCase()) {
			case "NAME":
				element = driver.findElements(By.name(propertyValue));
				break;
			case "ID":
				element = driver.findElements(By.id(propertyValue));
				break;
			case "CSS":
				element = driver.findElements(By.cssSelector(propertyValue));
				break;
			case "CLASSNAME":
				element = driver.findElements(By.className(propertyValue));
				break;
			case "XPATH":
				element = driver.findElements(By.xpath(propertyValue));
				break;
			case "LINKTEXT":

				break;
			}
		} catch (Exception e) {
			initializer.failureCallwithException("Issue on getting elements : " + propertyValue, e);
		}
		return element;
	}

	/**
	 * Function Name : startTestCase Description : This function Prints in log file
	 * as Specific test cases started execution from test suite
	 *
	 * @param sTestCaseName Test case name which is currently running
	 * @author Santhosh Karra
	 */
	public void startTestCase(String sTestCaseName) {
		try {
			initializer.log("****************************************************************************************");
			initializer.log("------------------  " + sTestCaseName + " Execution is STARTED   ---------------------");
			initializer.log("****************************************************************************************");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Function Name : endTestCase Description : This function Prints in log file as
	 * Specific test cases started execution in test suite
	 *
	 * @param sTestCaseName Test case name which is currently running
	 * @param result        Test Result
	 * @author Santhosh Karra
	 */
	public void endTestCase(String sTestCaseName, ITestResult result) {
		initializer.log("---------------------------------- END   -----------------------------------");
	}

	/**
	 * Function Name : waitAndAction Description : This waits for the object to get
	 * loaded and perform action, This is applicable for autoit objects only
	 *
	 * @param objTitle  Title of the object to be handled
	 * @param objID     ID of the object to be handled
	 * @param strAction Action to be performed
	 * @param strInput  Input into the element, if it is text box
	 * @author Santhosh Karra
	 */
	public void waitAndAction(String objTitle, String objID, String strAction, String strInput) {
		boolean blnObject = false;
		// Activate the window based on title
		if (objTitle.trim().length() != 0) {
			objAutoIT.winActivate(objTitle);
			blnObject = true;
		}
		try {
			// Action
			if (blnObject) {
				if (strAction.trim().toUpperCase().contains("CLICK")) {
					objAutoIT.controlClick(objTitle, strInput.trim(), objID.trim());
				} else if (strAction.trim().toUpperCase().contains("SETTEXT")) {
					objAutoIT.ControlSetText(objTitle.trim(), "", objID.trim(), strInput);
				}
			}
			initializer.wait(2);
		} catch (Exception e) {
			initializer.log("Issue on identifying object :" + objID + "Exception : " + e.getMessage());
		}
	}

	/**
	 * Function Name : stepValidate Description : Validates the Actual end Expected
	 * results for String
	 *
	 * @param strActual      Actual value to validate
	 * @param strExpected    Expected Value to validate
	 * @param strDescription Step description to report in log/result the status of
	 *                       this validation
	 * @param exitHandler    this helps to exit the entire test case, if any key
	 *                       failures(True - Exit the test case execution, false -
	 *                       proceed to next step)
	 *                       @author Santhosh Karra
	 */
	public void stepValidate(String strActual, String strExpected, String strDescription, boolean exitHandler) {
		if (strActual.toLowerCase().trim().contains(strExpected.toLowerCase().trim())) {
			initializer.successCallWithOutSnapShot(
					strDescription + " : Actual is : " + strActual + " and Expected is : " + strExpected);
		} else {
			initializer
					.failureCall(strDescription + " : Actual is : " + strActual + " and Expected is : " + strExpected);
			if (exitHandler) {
				initializer.finalizeResults();
			}
		}
	}

	/**
	 * Function Name : selectValidate Description : This will select input value in
	 * drop down preset on web browser
	 *
	 * @param list                  webElement
	 * @param strValue              Value to be selected in drop down
	 * @param strElementDescription Step description to report in log/result the
	 *                              status of this validation
	 * @param bTakeScreenShot       Screenshot is required or not(True - take the
	 *                              screen shot, false - don't take the screen shot)
	 * @param exitHandler           this helps to exit the entire test case, if any
	 *                              key failures(True - Exit the test case
	 *                              execution, false - proceed to next step)
	 *                              @author Santhosh Karra
	 */
	public void selectValidate(List<WebElement> list, String strValue, String strElementDescription,
			boolean bTakeScreenShot, boolean exitHandler) {
		boolean found = false;
		try {
			Select oE = new Select((WebElement) list);
			List<WebElement> allOptions = oE.getOptions();
			for (WebElement element : allOptions) {
				if (element.getText().toLowerCase().trim().contains(strValue))
					;
				element.click();
				found = true;
			}
			if (found) {
				initializer.successCallwithSnapShot(strElementDescription + " is present ");
			}
		} catch (Exception e) {
			initializer.failureCallwithException("Issue on Identifying drop down : " + strElementDescription, e);
		}
	}

	/**
	 * Function Name : getTestData Description : This reads test data from
	 * Testdata.xml, This will have script level external data
	 *
	 * @param TestName Test case name which is currently running
	 * @param strParam Parameter name for the test case from TestData.xml
	 * @return Returns the data for the parameter passed
	 * @author Santhosh Karra
	 */
	public String getTestData(String TestName, String strParam, String filePath) {
		String testDataParam = null;
		DocumentBuilder dBuilder = null;
		Document doc = null;
		Node node = null;
		File fXmlFile = new File(System.getProperty("user.dir") + initializer.GetValue(filePath));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		XPath xPath = XPathFactory.newInstance().newXPath();
		String expression = String.format("/TestCases/Test[@Name='" + TestName + "']/" + strParam);
		try {
			node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		if (node != null) {
			testDataParam = node.getTextContent();
		} else {
			initializer.infoCall("cannot read the test Data xml file");
			stage.setStatus(false);
			initializer.finalizeResults();
		}
		return testDataParam;
	}

	/**
	 * Function Name : waitSync: This function makes execution to wait for element
	 * present. It checks for the element loaded on the page for every 2 sec for 15
	 * iterations.
	 *
	 * @param oElement
	 * @author Santhosh Karra
	 */
	public synchronized boolean waitSync(WebElement oElement) {
		for (int i = 0; i < 15; i++) {
			try {
				if (oElement != null) {
					return true;
				} else {
					wait(2);
				}
			} catch (Exception e) {
			}
		}
		return false;
	}

}
