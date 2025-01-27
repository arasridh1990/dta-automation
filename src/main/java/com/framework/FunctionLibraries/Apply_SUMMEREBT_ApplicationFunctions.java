package com.framework.FunctionLibraries;

import java.awt.Robot;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class Apply_SUMMEREBT_ApplicationFunctions{
	public WebDriver driver = null;
	public ExtentTest test = null;
	public WebElement element;
	public String strLoginUserName = null;
	public String strLoginPassword = null;
	public boolean enableSnapshots = true;
	public PreparedStatement prSt = null;
	public Connection objConndB;
	public HashMap<String, String> globalParams;
	String objTitle = "";
	Robot robot;
	private CommonFunctions commonFunctions = null;
	private Initializer initializer = null;
	private Stage stage = null;
	private DB dB = null;

	public Apply_SUMMEREBT_ApplicationFunctions() {
		/*
		 * This condition gets enableSnapshots from Jenkins/pom to enable or disable
		 * screenshots.
		 *
		 * By default enableSnapshots will be true and screenshots will capture
		 * 
		 * @author Santhosh Karra
		 */
		if (System.getProperty("enableSnapshots").equalsIgnoreCase("NO")) {
			enableSnapshots = false;
		}

		/*
		 * This condition is to handle upload pop up for browsers
		 * 
		 * @author Santhosh Karra
		 */
		if (System.getProperty("Browser").equalsIgnoreCase("CHROME")) {
			objTitle = "Open";
		} else if (System.getProperty("Browser").equalsIgnoreCase("FIREFOX")) {
			objTitle = "File Upload";
		} else if (System.getProperty("Browser").equalsIgnoreCase("IE")) {
			objTitle = "Choose File to Upload";
		}
	}

	public Apply_SUMMEREBT_ApplicationFunctions(CommonFunctions commonFunctions, Initializer initializer, Stage stage,
			DB dB) {
		this();
		this.commonFunctions = commonFunctions;
		this.initializer = initializer;
		this.stage = stage;
		this.dB = dB;
	}

	/**
	 * This method initialize the test object in Application functions library
	 *
	 * @param test This is ExtentTest object, ExtentTest object is used for report
	 *             generation.
	 * @author Santhosh Karra
	 */
	public void init(ExtentTest test) {
		this.test = test;
	}

	/**
	 * This method initialize the driver object in Application functions library
	 *
	 * @param driver This is webdriver object
	 * @author Santhosh Karra
	 */
	public void init(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Function Name : launchURL Description : This function will launch URL *
	 *
	 * @param strURL , URL to launch application
	 * @throws SQLException
	 * @author Santhosh Karra
	 */
	@SuppressWarnings("deprecation")
	public void launchURL(String strURL) {
		String strBrowser = System.getProperty("Browser");
		try {
			// Killing opened browser by process
			commonFunctions.killbyProcess(strBrowser);
			switch (strBrowser.toUpperCase()) {
			case "FIREFOX":
				// Create FireFox Profile object
				FirefoxProfile profile = new FirefoxProfile();
				// Set Location to store files for downloads.
				profile.setPreference("browser.helperApps.neverAsk.saveFile",
						"attachment/pdf,text/html,text/plain,application/pdf,application/x-zip-compressed,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
						"attachment/pdf,text/html,text/plain,application/pdf,application/x-zip-compressed,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				profile.setPreference("browser.helperApps.neverAsk.openFile",
						"attachment/pdf,text/html,text/plain,application/pdf,application/x-zip-compressed,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				profile.setPreference("browser.download.dir",
						System.getProperty("user.dir") + initializer.GetValue("file.documentDownloadPath"));
				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.download.manager.showWhenStarting", false);
				profile.setPreference("pdfjs.disabled", true);
				profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
				profile.setPreference("browser.download.manager.closeWhenDone", true);
				System.setProperty("webdriver.gecko.driver", initializer.GetValue("java.firefox.path"));
				FirefoxOptions options1 = new FirefoxOptions();
				options1.setProfile(profile);
				options1.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options1);
				driver = new FirefoxDriver(options1);
				driver.manage().window().maximize();
				break;
			case "IE":
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability("requireWindowFocus", true);
				capabilities.setCapability("nativeEvents", false);
				capabilities.setCapability("unexpectedAlertBehaviour", "accept");
				capabilities.setCapability("ignoreProtectedModeSettings", true);
				capabilities.setCapability("disable-popup-blocking", true);
				capabilities.setCapability("enablePersistentHover", true);
				System.setProperty("webdriver.ie.driver", initializer.GetValue("java.ie.path"));
				driver = new InternetExplorerDriver(capabilities);
				// Setting the default download directory
				String downloadPath = System.getProperty("user.dir")
						+ initializer.GetValue("file.documentDownloadPath");
				String path = "\"" + downloadPath + "\"";
				String cmd1 = "REG ADD \"HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\Main\" /F /V \"Default Download Directory\" /T REG_SZ /D "
						+ path;
				try {
					Runtime.getRuntime().exec(cmd1);
				} catch (Exception e) {
					initializer.failureCallWithOutSnapShot(
							"Coulnd't change the registry for default download directory for IE");
				}
				break;
			case "CHROME":
				ChromeOptions options = new ChromeOptions();
				System.setProperty("webdriver.chrome.driver", initializer.GetValue("java.chrome.path"));
				HashMap<String, Object> chromePrefs = new HashMap<>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.prompt_for_download", false);
				chromePrefs.put("download.default_directory",
						System.getProperty("user.dir") + initializer.GetValue("file.documentDownloadPath"));
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("disable-extensions");
				options.addArguments("--start-maximized");
				driver = new ChromeDriver(options);
				break;
			default:
				System.setProperty("webdriver.gecko.driver", initializer.GetValue("java.firefox.path"));
				driver = new FirefoxDriver();
			}
			driver.get(strURL);
			initializer.wait(2);
			initializer.successCallwithSnapShot("DTA Connect application is launched successfully");
		} catch (Exception e) {
			initializer.log("Issue on launching Apply Summer EBT! Application. Exception : " + e.getMessage());
		}
	}

	/**
	 * Function Name : quitBrowser Description : This function Quits the browser
	 * after execution
	 *
	 * @throws Exception , throws an exception if any while closing the browser
	 * @author Santhosh Karra
	 */

	public void quitBrowser() throws Exception {
		try {
			if (driver != null)
				driver.quit();
		} catch (Exception e) {
			System.out.println("Issue on closing the browser" + e.getMessage());
		}
	}
	
	/**
	 * @author Santhosh Karra
	 */
	public void waitUntil(By ele) {
		WebDriverWait oWait = new WebDriverWait(driver, 30);
		oWait.until(ExpectedConditions.presenceOfElementLocated(ele));
	}

	/**
	 * @author Santhosh Karra
	 */
	public void selectDropDown(WebElement oElement, String optin) {
		Select oSel = new Select(oElement);
		int count = oSel.getOptions().size();
		for (int i = 0; i < count; i++) {
			if (oSel.getOptions().get(i).getAttribute("value").equalsIgnoreCase(optin)) {
				oSel.getOptions().get(i).click();
				break;
			}
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void getDropDownValue(WebElement oElement) {
		Select oSel = new Select(oElement);
		List<WebElement> weblist = oSel.getOptions();
		int iCnt = weblist.size();
		Random num = new Random();
		int iSelect = num.nextInt(iCnt);
		oSel.selectByIndex(iSelect);
		oElement.getAttribute("value");
	}

	String FValidation = "Please enter your first name";
	String LValidation = "Please enter your last name";
	String DOBValidation = "Please enter a valid date of birth";
	String SSNValidation = "Please enter a valid Social Security Number";
	String addressvalidation = "Please enter a valid address";
	String phonevalidation = "Please enter a valid phone number";
	String emailvalidation = "Please enter a valid email";
	String zipvalidation = "This Summer EBT application is for Massachusetts residents. If you live in Massachusetts, please enter your Zip Code. If you do not live in Massachusetts, you must apply in your state.";
	String mailingzipvalidation = "This Summer EBT application is for Massachusetts residents. If you live in Massachusetts, please enter your Zip Code. If you do not live in Massachusetts, you must apply in your state.";
	String householdvalidation = "Please choose no. of household";
	
	String hhincomevalidation = "Please select an option below";
	String hhincomeamountvalidation = "Please enter a valid amount";
	String agreeValidation = "Please agree to the terms";
	String signValidation = "Please sign your application";
	String elgibility_people = "Please select how many people live in your household";
	String elgibility_seniors = "Please answer if anyone is age 60 or older";
	String elgibility_disabilities = "Please answer if anyone in the household has a physical or mental disability";
	String elgibility_income = "Please enter the total gross income for your household";

	/**
	 * @author Santhosh Karra
	 */
	public String getName() {
		int len = 9;
		String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		String Small_chars = "abcdefghijklmnopqrstuvwxyz";
//		String values = Small_chars+Capital_chars;
		String values = Capital_chars;
		Random r = new Random();
		char[] u = new char[len];
		for (int i = 0; i < len; i++) {
			u[i] = values.charAt(r.nextInt(values.length()));
		}

		String userName = new String(u);
		return userName;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getMinorYear() {
		Year dob = Year.now().minusYears(10);
		String s = String.valueOf(dob);
		return s;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getMajorYear() {
		Year dob = Year.now().minusYears(30);
		String s = String.valueOf(dob);
		return s;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getSeniorYear() {
		Year dob = Year.now().minusYears(65);
		String s = String.valueOf(dob);
		return s;
	}

	/**
	 * @author Santhosh Karra
	 */
	@SuppressWarnings("resource")
	public String getMonth() {
		Formatter fmt = new Formatter();
		Calendar cal = Calendar.getInstance();
		fmt = new Formatter();
		fmt.format("%tB", cal);
		String month = String.valueOf(fmt);
		String temp = month;
		@SuppressWarnings("unused")
		String result = "August";
		if (temp != null && !temp.isEmpty()) {
			result = temp;
		}
		System.out.print("The Random month is " + month);
		return month;
	}

	/**
	 * @author Santhosh Karra
	 */
	/*
	 * public String getMonth() { Calendar mCalendar = Calendar.getInstance();
	 * String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG,
	 * Locale.getDefault()); String temp = month;
	 *
	 * @SuppressWarnings("unused") String result = "August"; if (temp != null &&
	 * !temp.isEmpty()) { result = temp; } System.out.print("The Random month is " +
	 * month); return month; }
	 */
	/**
	 * @author Santhosh Karra
	 */
	public String getDay() {
//		Random objGenerator = new Random();
//		int randomNumber = objGenerator.nextInt(25);
//		System.out.println("Random No : " + randomNumber);
		int max = 28;
		int min = 1;
		Random rn = new Random();
		int i = rn.nextInt(max - min) + min;
		System.out.println("Random No : " + i);
		String s = Integer.toString(i);
		return s;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getAmount() {
		float max = 1000;
		float min = 100;
		Random rn = new Random();
		float i = rn.nextFloat() * (max - min) + min;
		String s = Float.toString(i);
		return s;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getWrongZip() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 4, num.length());
		String wrongZip = "9" + number;
		return wrongZip;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getZipCode() {
		String zipCode = "02111";
		return zipCode;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getStreetName() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 4, num.length());
		String streetName = number + " Street Name";
		return streetName;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getEmail() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 4, num.length());
		String email = "username_" + number + "@gmail.com";
		return email;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getWrongEmail() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 4, num.length());
		String email = "username_" + number + ".com";
		return email;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getPhoneNumber() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 10, num.length());
		String phoneNumber = number;
		return phoneNumber;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getSSN() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 9, num.length());
		String SSN = number;
		return SSN;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getWrongSSN() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 5, num.length());
		String SSN = number;
		return SSN;
	}

	/**
	 * @author Santhosh Karra
	 */
	public String getMonth_Frequency() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 5, num.length());
		String SSN = number;
		return SSN;
	}

//	*********************Apply SNAP! Generic methods************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	
	public String getSnapData(String param) {
		return commonFunctions.getTestData(stage.getTestName(), param, "file.summerebtDataFilePath");
	}
	
	public void app_Down() {

		try {
			initializer.wait(2);
			String title1 = commonFunctions.getElement(driver, "appdown").getText();
			String title2 = commonFunctions.getElement(driver, "appdownback").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title1 + " " + title2 + "' Page successfully.");
			initializer.wait(2);
			 System.exit(0);
	        
		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void applySUMMEREBTButton() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "sebt.applySUMMEREBT").click();
			initializer.infoCall("Clicking 'Apply Summer EBT!' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'ApplySUMMEREBT!' application is launched successfully");
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}
	public void applySUMMEREBTButton_es() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "es.applySUMMEREBT").click();
			initializer.infoCall("Clicking 'Apply Summer EBT!' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'ApplySUMMEREBT!' application is launched successfully");
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}
	public void languageSelect() {
		try {
			initializer.wait(5);
			selectDropDown(commonFunctions.getElement(driver, "language.select"), getSnapData("Language"));
			initializer.infoCall("Language selected sucessfully.");
			initializer.wait(5);
			} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}
	/**
	 * @author Santhosh Karra
	 */
	public void continue_Option() {

		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Enter/Select all '" + title + "' Page information successfully.");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.infoCall("Click 'Continue' button successfully.");
			initializer.wait(2);
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title1 + "' page successfully ");

		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void goBack_Option() {

		try {

			commonFunctions.getElement(driver, "generic.goback").click();
			initializer.infoCall("Click 'Go Back' button sucessfully.");
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' Page successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void noOne_Option() {
		try {
			commonFunctions.getElement(driver, "generic.noone").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select 'No one' option successfully in ' " + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in (Disability) '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void grantee_Option() {
		try {
			commonFunctions.getElement(driver, "generic.grantee").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select grantee successfully in ' " + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in (Disability) '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dependent_1_Option() {
		try {
			commonFunctions.getElement(driver, "generic.dep1").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select dependent in the house successfully in ' " + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void yes_Option() {
		try {
			commonFunctions.getElement(driver, "generic.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void no_Option() {
		try {
			commonFunctions.getElement(driver, "generic.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void male_Option() {
		try {
			commonFunctions.getElement(driver, "fs.male").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select gender as 'Male' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void female_Option() {
		try {
			commonFunctions.getElement(driver, "fs.female").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select gender as 'Female' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void sSN_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "household.ssn").sendKeys(getSnapData("Wrongssn"));
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String ssn = commonFunctions.getElement(driver, "fs.ssnvalidation").getText();
				initializer.infoCall("SSN format validation: " + ssn);
				Assert.assertEquals(ssn, SSNValidation);

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("SSN formate validate successfully in '" + title + "' Page.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void noSSN_Option() {
		try {
			commonFunctions.getElement(driver, "household.nossn").click();
			initializer.infoCall("Click 'I don't have one' button sucessfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void sSN_Option() {
		try {
			commonFunctions.getElement(driver, "household.ssn").clear();
			String SSN = getSSN();
			commonFunctions.getElement(driver, "household.ssn").sendKeys(SSN);
			initializer.infoCall("Enter SSN number :" + SSN + " successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void one_Option() {
		try {
			commonFunctions.getElement(driver, "fs.hh1").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select option as 1 in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void two_Option() {
		try {
			commonFunctions.getElement(driver, "fs.hh2").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select option as 2 in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void three_Option() {
		try {
			commonFunctions.getElement(driver, "fs.hh3").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select option as 3 in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void four_Option() {
		try {
			commonFunctions.getElement(driver, "fs.hh4").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select option as 4 in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void five_Option() {
		try {
			commonFunctions.getElement(driver, "fs.hh5").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select option as 5 in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	/*
	 * public void six_Option() { try { commonFunctions.getElement(driver,
	 * "fs.hh6").click(); initializer.wait(2); String title =
	 * commonFunctions.getElement(driver, "page.title").getText();
	 * initializer.successCallwithSnapShot("Select option as 6 in '" + title +
	 * "' Page."); } catch (Exception e) { String title =
	 * commonFunctions.getElement(driver, "page.title").getText();
	 * initializer.failureCallwithException("Exception occured in '" + title +
	 * "' Page", e); } }
	 */
	/**
	 * @author Santhosh Karra
	 */
	public void more_Option(String TDRequired) {
		try {
			commonFunctions.getElement(driver, "fs.more").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select option as more in '" + title + "' Page.");
			commonFunctions.getElement(driver, "fs.moreinput")
					.sendKeys(commonFunctions.getTestData(stage.getTestName(), "moreHH", "file.recertDataFilePath"));
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//  *********************How does applying work---Before you start**********************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void beforeYouStart() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Displayed '" + title + "' page successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void hotline_BeforeYouStart() {
		try {

			commonFunctions.getElement(driver, "hotline").click();
			initializer.wait(2);
			initializer.infoCall("Display 'Phone call' popup successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dTAOffice_BeforeYouStart() {
		try {

			commonFunctions.getElement(driver, "dtaoffice.link").click();
			initializer.wait(2);
			initializer.infoCall("Navigate to 'Department of Transitional Assistance Locations' tab successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void goBack_BeforeYouStart() {
		try {
			commonFunctions.getElement(driver, "generic.goback").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot("Display 'DTA Connect home' page successfully");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void handyDocs_BeforeYouStart() {
		try {

			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Displayed '" + title + "' page successfully");
			String handy = commonFunctions.getElement(driver, "fs.handy").getText();
			String handy1 = commonFunctions.getElement(driver, "fs.handy1").getText();
			String handy2 = commonFunctions.getElement(driver, "fs.handy2").getText();
			String handy3 = commonFunctions.getElement(driver, "fs.handy3").getText();
			String handy4 = commonFunctions.getElement(driver, "fs.handy4").getText();
			initializer.successCallwithSnapShot(
					handy + "<br>" + handy1 + "<br>" + handy2 + "<br>" + handy3 + "<br>" + handy4);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//  *********************About me*******************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void firstLast_Name() {
		try {
			commonFunctions.getElement(driver, "household.fname").sendKeys(getSnapData("FName"));
			commonFunctions.getElement(driver, "household.lname").sendKeys(getSnapData("LName"));
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void firstLast_Name1() {
		try {
			commonFunctions.getElement(driver, "household.fname").sendKeys(getSnapData("FName1"));
			commonFunctions.getElement(driver, "household.lname").sendKeys(getSnapData("LName1"));
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void firstLast_Name2() {
		try {
			commonFunctions.getElement(driver, "household.fname").sendKeys(getSnapData("FName2"));
			commonFunctions.getElement(driver, "household.lname").sendKeys(getSnapData("LName2"));
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void firstLast_Name3() {
		try {
			commonFunctions.getElement(driver, "household.fname").sendKeys(getSnapData("FName3"));
			commonFunctions.getElement(driver, "household.lname").sendKeys(getSnapData("LName3"));
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void firstLast_Name4() {
		try {
			commonFunctions.getElement(driver, "household.fname").sendKeys(getSnapData("FName4"));
			commonFunctions.getElement(driver, "household.lname").sendKeys(getSnapData("LName4"));
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	/**
	 * @author Santhosh Karra
	 */
	public void dateOfBirth_Minor() {
		try {
			commonFunctions.getElement(driver, "household.day").sendKeys(getSnapData("Day1"));
			selectDropDown(commonFunctions.getElement(driver, "household.month"), getSnapData("Month1"));
			commonFunctions.getElement(driver, "household.year").sendKeys(getSnapData("Minoryear"));
			initializer.wait(2);
			initializer.infoCall("Enter date of birth under minor group age.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dateOfBirth_Major() {
		try {
			commonFunctions.getElement(driver, "household.day").sendKeys(getSnapData("Day2"));
			selectDropDown(commonFunctions.getElement(driver, "household.month"), getSnapData("Month2"));
			commonFunctions.getElement(driver, "household.year").sendKeys(getSnapData("Majoryear"));
			initializer.infoCall("Enter date of birth under mjor group age.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dateOfBirth_Senior() {
		try {
			commonFunctions.getElement(driver, "household.day").sendKeys(getSnapData("Day3"));
			selectDropDown(commonFunctions.getElement(driver, "household.month"), getSnapData("Month3"));
			commonFunctions.getElement(driver, "household.year").sendKeys(getSnapData("Senioryear"));
			initializer.infoCall("Enter date of birth under senior group age.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void middle_Name() {
		try {
			commonFunctions.getElement(driver, "household.mname").sendKeys(getSnapData("MName"));
			initializer.infoCall("Enter middle name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void suffix_Name() {
		try {
			getDropDownValue(commonFunctions.getElement(driver, "household.suffix"));
			initializer.infoCall("Enter suffix successfully.");
//			selectDropDown(commonFunctions.getElement(driver, "household.suffix"),
//					(commonFunctions.getTestData(stage.getTestName(), "Suffix")));
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void aboutMe_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String fname = commonFunctions.getElement(driver, "fs.fnvalidation").getText();
				String lname = commonFunctions.getElement(driver, "fs.lnvalidation").getText();
				String dob = commonFunctions.getElement(driver, "fs.dobvalidation").getText();
				initializer.infoCall("First Name validation: " + fname);
				initializer.infoCall("Last Name validation: " + lname);
				initializer.infoCall("Date of Birth validation: " + dob);
				Assert.assertEquals(fname, FValidation);
				Assert.assertEquals(lname, LValidation);
				Assert.assertEquals(dob, DOBValidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"First Name, Last Name and Date of birth fields validated successfully in '" + title + "' Page.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}

//	*********************About me---My contact info*************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void myContactInfo_Address_Phone_ValidationMsg() {
		try {

			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String addr = commonFunctions.getElement(driver, "fs.addressvalidation").getText();
				String phone = commonFunctions.getElement(driver, "fs.phonevalidation").getText();
				
				initializer.infoCall("Address validation: " + addr);
				initializer.infoCall("Phone number validation: " + phone);
				Assert.assertEquals(addr, addressvalidation);
				Assert.assertEquals(phone, phonevalidation);

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
					} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void myContactInfo_Email_ValidationMsg() {
		try {
			try {
				String email = commonFunctions.getElement(driver, "fs.emailvalidation").getText();
				initializer.infoCall("Email validation: " + email);
				Assert.assertEquals(email, emailvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("All '" + title + "' Page Required fields validated successfully ");
			initializer.wait(4);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	
	public void myContactInfo_AddressZipCode_ValidationMsg() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "fs.street").sendKeys(getSnapData("StreetName"));
			commonFunctions.getElement(driver, "fs.zip").sendKeys(getSnapData("Wrongzip"));
			initializer.wait(2);
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String zip = commonFunctions.getElement(driver, "fs.zipcodevalidation").getText();
				initializer.infoCall("Zip Validation: " + zip);
				Assert.assertEquals(zip, zipvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"'" + title + "' Non residents of Massachusetts Zipcode validation successfully ");

		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	/**
	 * @author Santhosh Karra
	 */

	public void address() {
		try {
			commonFunctions.getElement(driver, "fs.street").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "fs.street").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "fs.street").sendKeys(getSnapData("StreetName"));
			commonFunctions.getElement(driver, "fs.zip").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "fs.zip").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "fs.zip").sendKeys(getSnapData("Zipcode"));
			initializer.infoCall("Enter client address successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	/**
	 * @author Santhosh Karra
	 */
	public void yes_MailAddress_MyContactInfo() {
		try {
			commonFunctions.getElement(driver, "fs.mailingyes").click();
			initializer.wait(2);
			initializer.infoCall("Select mailing addresss as 'YES' successfully");

		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void no_MailAddress_MyContactInfo() {
		try {
			commonFunctions.getElement(driver, "fs.mailingno").click();
			initializer.wait(2);
			initializer.infoCall("Select mailing addresss as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void myContactInfo_MailAddressEmail_ValidationMsg() {
		try {
			try {
				commonFunctions.getElement(driver, "generic.continue").click();
				initializer.wait(2);
				String email = commonFunctions.getElement(driver, "fs.emailvalidation").getText();
				initializer.wait(3);
				initializer.infoCall("Email validation: " + email);
				Assert.assertEquals(email, emailvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("All '" + title + "' Page Required fields validated successfully ");
			initializer.wait(4);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void myContactInfo_MailAddressZipCode_ValidationMsg() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "fs.mailstreet").sendKeys(getSnapData("MailStreetName"));
			commonFunctions.getElement(driver, "fs.mailzip").sendKeys(getSnapData("Wrongzip"));
			initializer.wait(2);
			try {
				commonFunctions.getElement(driver, "generic.continue").click();
				String zip = commonFunctions.getElement(driver, "fs.zipcodevalidation").getText();
				initializer.infoCall("Massachusetts residents Zip code validation: " + zip);
				Assert.assertEquals(zip, mailingzipvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"'" + title + "' Non residents of Massachusetts Zipcode validated successfully.");

		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	/**
	 * @author Santhosh Karra
	 */
	
	public void mail_address() {
		try {
			commonFunctions.getElement(driver, "fs.mailstreet").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "fs.mailstreet").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "fs.mailstreet").sendKeys(getSnapData("MailStreetName"));
			commonFunctions.getElement(driver, "fs.mailzip").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "fs.mailzip").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "fs.mailzip").sendKeys(getSnapData("MailZipcode"));
			initializer.infoCall("Enter client address successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void email_MyContactInfo() {
		try {
			commonFunctions.getElement(driver, "fs.email").clear();
			String email = getEmail();
			commonFunctions.getElement(driver, "fs.email").sendKeys(email);
			initializer.infoCall("Enter client email address as " + email + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	/**
	 * @author Santhosh Karra
	 */
	public void phone_MyContactInfo() {
		try {
			String phone = getSnapData("Phone");
			commonFunctions.getElement(driver, "fs.phone").clear();
			commonFunctions.getElement(driver, "fs.phone").sendKeys(phone);
			initializer.infoCall("Enter phone number :" + phone + " successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
//	*********************About me---Emergency SNAP benefits*****************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void emergencyFSBenefits_Lesshouseexp_Yes() {
		try {
			commonFunctions.getElement(driver, "fs.lesshouseexpyes").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select less housing expenses as 'Yes' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void emergencyFSBenefits_Lesshouseexp_No() {
		try {
			commonFunctions.getElement(driver, "fs.lesshouseexpno").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select less housing expenses as 'No' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void emergencyFSBenefits_LessMonthlyIncome_Yes() {
		try {
			commonFunctions.getElement(driver, "fs.lessincomeyes").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select monthly income less as 'Yes' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void emergencyFSBenefits_LessMonthlyIncome_No() {
		try {
			commonFunctions.getElement(driver, "fs.lessincomeno").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select monthly income less as 'No' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void emergencyFSBenefits_MigrantWorker_Yes() {
		try {
			commonFunctions.getElement(driver, "fs.migrantworkeryes").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select Migrant Worker option as 'Yes' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void emergencyFSBenefits_MigrantWorker_No() {
		try {
			commonFunctions.getElement(driver, "fs.migrantworkerno").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select Migrant Worker option as 'No' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */

	public void language() {
		try {
			getDropDownValue(commonFunctions.getElement(driver, "fs.language"));
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer
					.successCallwithSnapShot("Prefer to speak language successfully select in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
//	*********************About me---More about me_2*************************************************************************************************************

	public void hispanic_MoreAboutMe() {
		try {
			commonFunctions.getElement(driver, "fs.ethnicityHispanic").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select ethnicity as 'Hispanic/Latino' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void non_Hispanic_MoreAboutMe() {
		try {
			commonFunctions.getElement(driver, "fs.ethnicityNonHispanic").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select ethnicity as 'Non-Hispanic/Latino' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void decline_Ethnicity_MoreAboutMe() {
		try {
			commonFunctions.getElement(driver, "fs.ethnicityDecline").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select ethnicity as 'Decline to answer' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void american_Indian_MoreAboutMe() {
		try {
			commonFunctions.getElement(driver, "fs.raceAmericanIndian").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer
					.successCallwithSnapShot("Select race as 'American Indian' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void asian_MoreAboutMe() {
		try {
			commonFunctions.getElement(driver, "fs.raceAsian").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select race as 'Asain' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void black_American_MoreAboutMe() {
		try {
			commonFunctions.getElement(driver, "fs.raceBlackAfricanAmerican").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select race as 'Black/Afrecan American' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void pacific_Islander_MoreAboutMe() {
		try {
			commonFunctions.getElement(driver, "fs.racePacificIslander").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer
					.successCallwithSnapShot("Select race as 'Pacific Islander' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void white_MoreAboutMe() {
		try {
			commonFunctions.getElement(driver, "fs.raceWhite").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select race as 'White' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void decline_Race_MoreAboutMe() {
		try {
			commonFunctions.getElement(driver, "fs.raceDeclinetoanswer").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select race as 'Decline to answer' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************My household---About my household******************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void aboutMyHousehold_ValidationMsg() {
		try {
			initializer.wait(2);
			try {
				String household = commonFunctions.getElement(driver, "fs.householdvalidation").getText();
				initializer.successCallwithSnapShot("Household validation: " + household);
				Assert.assertEquals(household, householdvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("All '" + title + "' Page Require fields validate successfully ");

		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************My household---About my household - Disability*****************************************************************************************

//	*********************My household---About FNMAE LNAME(DD.MMM.YYYY)******************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void aboutMember_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String fname = commonFunctions.getElement(driver, "hh.fnvalidation").getText();
				String lname = commonFunctions.getElement(driver, "hh.lnvalidation").getText();
				String dob = commonFunctions.getElement(driver, "fs.dobvalidation").getText();
				initializer.successCallwithSnapShot("First Name validation: " + fname);
				initializer.successCallwithSnapShot("Last Name validation: " + lname);
				initializer.successCallwithSnapShot("Date of Birth validation: " + dob);
				Assert.assertEquals(fname, commonFunctions.getTestData(stage.getTestName(), "HHFValidation", "file.recertDataFilePath"));
				Assert.assertEquals(lname, commonFunctions.getTestData(stage.getTestName(), "HHLValidation", "file.recertDataFilePath"));
				Assert.assertEquals(dob, commonFunctions.getTestData(stage.getTestName(), "DOBValidation", "file.recertDataFilePath"));

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"First Name, Last Name and Date of birth fields validate successfully in '" + title + "' Page.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void spouse_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.spouse").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Spouse' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void mother_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.mother").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Mother' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void father_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.father").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Father' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void daughter_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.daughter").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Daughter' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void son_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.son").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Son' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void brother_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.brother").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Brother' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void sister_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.sister").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Sister' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void grandmother_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.grandmother").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Grandmother' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void grandfather_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.grandfather").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Grandfather' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void granddaughter_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.granddaughter").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Granddaughter' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void grandson_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.grandson").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Grandson' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void other_AboutMember() {
		try {
			commonFunctions.getElement(driver, "household.other").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Other' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************My household---Household details---About my household---Disability*********************************************************************

//	*********************Income---About your household's income*************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void aboutYourHouseholdeIncome_ValidationMsg() {
		try {
			initializer.wait(2);
			try {
				String hhincome = commonFunctions.getElement(driver, "fs.hhincomevalidation").getText();

				initializer.successCallwithSnapShot("Household income validation: " + hhincome);

				Assert.assertEquals(hhincome, hhincomevalidation);

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("'" + title + "' Page Require field validate successfully ");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Income---FNMAE LNAME(DD.MMM.YYYY)---FNMAE LNAME(DD.MMM.YYYY)'s income and benefits_1***************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void incomeAndBenefits_ValidationMsg_1() {
		try {
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed '" + title + "' page successfully");
			commonFunctions.getElement(driver, "generic.getstarted").click();
			initializer.wait(2);
			try {
				String hhincome = commonFunctions.getElement(driver, "fs.hhincomevalidation").getText();

				initializer.successCallwithSnapShot("Household income validation: " + hhincome);

				Assert.assertEquals(hhincome, hhincomevalidation);

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			initializer.successCallwithSnapShot("'" + title + "' Page Require field validate successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void wages_IncomeAndBenefits() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "earned.wages").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Wages' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void self_Employment_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "earned.selfemp").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Self-Employment' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void workStudy_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "earned.workstudy").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Work Study' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void sSI_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.ssi").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'SSI (Supplemental Security Income)' successfully in '" + title
							+ "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void rSDI_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.rsdi").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'RSDI (Retirement, Survivors, and Disability Insurence)' successfully in '"
							+ title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void unemployment_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.unemp").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Unemployment' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void childSupport_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.childsupport").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Child Support' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void pension_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.pension").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Pension' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void veteransBenefits_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.vbenefits").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Veterans Benefits' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void rentalIncome_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.rentalincome").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Rental Income' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void workersCompensation_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.workerscomp").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Workers Compensation' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void other_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.other").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Other' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Income---FNMAE LNAME(DD.MMM.YYYY)---FNMAE LNAME(DD.MMM.YYYY)'s income and benefits_2***************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void incomeAndBenefitsValidationMsg_2() {
		try {
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed '" + title + "' page successfully");
			initializer.wait(2);
			commonFunctions.getElement(driver, "generic.getstarted").click();
			initializer.wait(2);
			try {
				String hhincome = commonFunctions.getElement(driver, "fs.hhincomeamountvalidation").getText();

				initializer.successCallwithSnapShot("Household income amount field validation: " + hhincome);

				Assert.assertEquals(hhincome, hhincomeamountvalidation);

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			initializer.successCallwithSnapShot("'" + title + "' Page Require field validate successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void add_Wage() {
		try {
			commonFunctions.getElement(driver, "wages.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "wages.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_SelfEmployment() {
		try {
			commonFunctions.getElement(driver, "selfemp.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "selfemp.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_WorkStudy() {
		try {
			commonFunctions.getElement(driver, "workstudy.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "workstudy.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_SSI() {
		try {
			commonFunctions.getElement(driver, "ssi.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "ssi.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_RSDI() {
		try {
			commonFunctions.getElement(driver, "rsdi.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "rsdi.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_Unemployment() {
		try {
			commonFunctions.getElement(driver, "unemp.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "unemp.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_ChildSupport() {
		try {
			commonFunctions.getElement(driver, "childsupport.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "childsupport.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_Pension() {
		try {
			commonFunctions.getElement(driver, "pension.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "pension.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_VeteransBenefits() {
		try {
			commonFunctions.getElement(driver, "vetern.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "vetern.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_RentalIncome() {
		try {
			commonFunctions.getElement(driver, "rentalIncome.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "rentalIncome.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_RentalHours() {
		try {
			commonFunctions.getElement(driver, "rentalHours.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "rentalHours.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_WorkersCompensation() {
		try {
			commonFunctions.getElement(driver, "workerscomp.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "workerscomp.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_Other() {
		try {
			commonFunctions.getElement(driver, "other.addbutton").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "other.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void remove_One_Amount2() {
		try {
			commonFunctions.getElement(driver, "one.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_One_Amount3() {
		try {
			commonFunctions.getElement(driver, "one.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_One_Amount4() {
		try {
			commonFunctions.getElement(driver, "one.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Two_Amount2() {
		try {
			commonFunctions.getElement(driver, "two.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Two_Amount3() {
		try {
			commonFunctions.getElement(driver, "two.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Two_Amount4() {
		try {
			commonFunctions.getElement(driver, "two.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Three_Amount2() {
		try {
			commonFunctions.getElement(driver, "three.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Three_Amount3() {
		try {
			commonFunctions.getElement(driver, "three.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Three_Amount4() {
		try {
			commonFunctions.getElement(driver, "three.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Four_Amount2() {
		try {
			commonFunctions.getElement(driver, "four.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Four_Amount3() {
		try {
			commonFunctions.getElement(driver, "four.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Four_Amount4() {
		try {
			commonFunctions.getElement(driver, "four.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Five_Amount2() {
		try {
			commonFunctions.getElement(driver, "five.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Five_Amount3() {
		try {
			commonFunctions.getElement(driver, "five.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Five_Amount4() {
		try {
			commonFunctions.getElement(driver, "five.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Six_Amount2() {
		try {
			commonFunctions.getElement(driver, "six.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Six_Amount3() {
		try {
			commonFunctions.getElement(driver, "six.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Six_Amount4() {
		try {
			commonFunctions.getElement(driver, "six.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Seven_Amount2() {
		try {
			commonFunctions.getElement(driver, "seven.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Seven_Amount3() {
		try {
			commonFunctions.getElement(driver, "seven.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Seven_Amount4() {
		try {
			commonFunctions.getElement(driver, "seven.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Eight_Amount2() {
		try {
			commonFunctions.getElement(driver, "eight.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Eight_Amount3() {
		try {
			commonFunctions.getElement(driver, "eight.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Eight_Amount4() {
		try {
			commonFunctions.getElement(driver, "eight.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Nine_Amount2() {
		try {
			commonFunctions.getElement(driver, "nine.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Nine_Amount3() {
		try {
			commonFunctions.getElement(driver, "nine.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Nine_Amount4() {
		try {
			commonFunctions.getElement(driver, "nine.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Ten_Amount2() {
		try {
			commonFunctions.getElement(driver, "ten.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Ten_Amount3() {
		try {
			commonFunctions.getElement(driver, "ten.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Ten_Amount4() {
		try {
			commonFunctions.getElement(driver, "ten.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Eleven_Amount2() {
		try {
			commonFunctions.getElement(driver, "eleven.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Eleven_Amount3() {
		try {
			commonFunctions.getElement(driver, "eleven.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Eleven_Amount4() {
		try {
			commonFunctions.getElement(driver, "eleven.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Twelve_Amount2() {
		try {
			commonFunctions.getElement(driver, "twelve.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Twelve_Amount3() {
		try {
			commonFunctions.getElement(driver, "twelve.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Twelve_Amount4() {
		try {
			commonFunctions.getElement(driver, "twelve.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Thirteen_Amount2() {
		try {
			commonFunctions.getElement(driver, "thirteen.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Thirteen_Amount3() {
		try {
			commonFunctions.getElement(driver, "thirteen.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void remove_Thirteen_Amount4() {
		try {
			commonFunctions.getElement(driver, "thirteen.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void one_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "one.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "one.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void one_Amount1_EveryTwoWeeks() {
		try {
			commonFunctions.getElement(driver, "one.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "one.frequency1").sendKeys("Every two weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void one_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "one.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "one.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void one_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "one.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "one.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void one_Amount2_EveryTwoWeeks() {
		try {
			commonFunctions.getElement(driver, "one.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "one.frequency2").sendKeys("Every two weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void one_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "one.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "one.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void one_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "one.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "one.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void one_Amount3_EveryTwoWeeks() {
		try {
			commonFunctions.getElement(driver, "one.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "one.frequency3").sendKeys("Every two weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void one_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "one.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "one.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void two_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "two.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "two.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void two_Amount1_EveryTwoWeeks() {
		try {
			commonFunctions.getElement(driver, "two.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "two.frequency1").sendKeys("Every two weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void two_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "two.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "two.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void two_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "two.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "two.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void two_Amount2_EveryTwoWeeks() {
		try {
			commonFunctions.getElement(driver, "two.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "two.frequency2").sendKeys("Every two weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void two_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "two.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "two.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void two_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "two.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "two.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void two_Amount3_EveryTwoWeeks() {
		try {
			commonFunctions.getElement(driver, "two.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "two.frequency3").sendKeys("Every two weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void two_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "two.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "two.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void three_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "three.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "three.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void three_Amount1_EverythreeWeeks() {
		try {
			commonFunctions.getElement(driver, "three.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "three.frequency1").sendKeys("Every three weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every three weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void three_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "three.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "three.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void three_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "three.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "three.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void three_Amount2_EverythreeWeeks() {
		try {
			commonFunctions.getElement(driver, "three.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "three.frequency2").sendKeys("Every three weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every three weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void three_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "three.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "three.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void three_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "three.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "three.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void three_Amount3_EverythreeWeeks() {
		try {
			commonFunctions.getElement(driver, "three.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "three.frequency3").sendKeys("Every three weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every three weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void three_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "three.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "three.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void four_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "four.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "four.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void four_Amount1_EveryfourWeeks() {
		try {
			commonFunctions.getElement(driver, "four.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "four.frequency1").sendKeys("Every four weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every four weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void four_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "four.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "four.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void four_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "four.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "four.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void four_Amount2_EveryfourWeeks() {
		try {
			commonFunctions.getElement(driver, "four.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "four.frequency2").sendKeys("Every four weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every four weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void four_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "four.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "four.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void four_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "four.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "four.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void four_Amount3_EveryfourWeeks() {
		try {
			commonFunctions.getElement(driver, "four.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "four.frequency3").sendKeys("Every four weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every four weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void four_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "four.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "four.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void five_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "five.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "five.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void five_Amount1_EveryfiveWeeks() {
		try {
			commonFunctions.getElement(driver, "five.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "five.frequency1").sendKeys("Every five weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every five weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void five_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "five.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "five.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void five_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "five.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "five.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void five_Amount2_EveryfiveWeeks() {
		try {
			commonFunctions.getElement(driver, "five.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "five.frequency2").sendKeys("Every five weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every five weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void five_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "five.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "five.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void five_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "five.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "five.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void five_Amount3_EveryfiveWeeks() {
		try {
			commonFunctions.getElement(driver, "five.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "five.frequency3").sendKeys("Every five weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every five weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void five_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "five.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "five.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void six_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "six.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "six.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void six_Amount1_EverysixWeeks() {
		try {
			commonFunctions.getElement(driver, "six.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "six.frequency1").sendKeys("Every six weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every six weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void six_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "six.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "six.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void six_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "six.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "six.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void six_Amount2_EverysixWeeks() {
		try {
			commonFunctions.getElement(driver, "six.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "six.frequency2").sendKeys("Every six weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every six weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void six_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "six.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "six.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void six_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "six.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "six.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void six_Amount3_EverysixWeeks() {
		try {
			commonFunctions.getElement(driver, "six.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "six.frequency3").sendKeys("Every six weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every six weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void six_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "six.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "six.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void seven_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "seven.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "seven.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void seven_Amount1_EverysevenWeeks() {
		try {
			commonFunctions.getElement(driver, "seven.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "seven.frequency1").sendKeys("Every seven weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every seven weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void seven_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "seven.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "seven.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void seven_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "seven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "seven.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "seven.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void seven_Amount2_EverysevenWeeks() {
		try {
			commonFunctions.getElement(driver, "seven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "seven.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "seven.frequency2").sendKeys("Every seven weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every seven weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void seven_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "seven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "seven.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "seven.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void seven_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "seven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "seven.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "seven.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void seven_Amount3_EverysevenWeeks() {
		try {
			commonFunctions.getElement(driver, "seven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "seven.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "seven.frequency3").sendKeys("Every seven weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every seven weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void seven_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "seven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "seven.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "seven.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void eight_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "eight.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eight.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eight_Amount1_EveryeightWeeks() {
		try {
			commonFunctions.getElement(driver, "eight.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eight.frequency1").sendKeys("Every eight weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every eight weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eight_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "eight.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eight.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eight_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "eight.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eight.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eight.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eight_Amount2_EveryeightWeeks() {
		try {
			commonFunctions.getElement(driver, "eight.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eight.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eight.frequency2").sendKeys("Every eight weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every eight weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eight_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "eight.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eight.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eight.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eight_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "eight.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eight.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eight.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eight_Amount3_EveryeightWeeks() {
		try {
			commonFunctions.getElement(driver, "eight.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eight.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eight.frequency3").sendKeys("Every eight weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every eight weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eight_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "eight.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eight.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eight.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void nine_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "nine.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "nine.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void nine_Amount1_EverynineWeeks() {
		try {
			commonFunctions.getElement(driver, "nine.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "nine.frequency1").sendKeys("Every nine weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every nine weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void nine_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "nine.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "nine.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void nine_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "nine.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "nine.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "nine.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void nine_Amount2_EverynineWeeks() {
		try {
			commonFunctions.getElement(driver, "nine.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "nine.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "nine.frequency2").sendKeys("Every nine weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every nine weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void nine_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "nine.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "nine.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "nine.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void nine_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "nine.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "nine.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "nine.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void nine_Amount3_EverynineWeeks() {
		try {
			commonFunctions.getElement(driver, "nine.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "nine.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "nine.frequency3").sendKeys("Every nine weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every nine weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void nine_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "nine.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "nine.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "nine.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void ten_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "ten.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "ten.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ten_Amount1_EverytenWeeks() {
		try {
			commonFunctions.getElement(driver, "ten.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "ten.frequency1").sendKeys("Every ten weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every ten weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ten_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "ten.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "ten.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ten_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "ten.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "ten.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "ten.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ten_Amount2_EverytenWeeks() {
		try {
			commonFunctions.getElement(driver, "ten.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "ten.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "ten.frequency2").sendKeys("Every ten weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every ten weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ten_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "ten.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "ten.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "ten.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ten_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "ten.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "ten.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "ten.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ten_Amount3_EverytenWeeks() {
		try {
			commonFunctions.getElement(driver, "ten.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "ten.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "ten.frequency3").sendKeys("Every ten weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every ten weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ten_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "ten.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "ten.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "ten.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void eleven_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "eleven.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eleven.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eleven_Amount1_EveryelevenWeeks() {
		try {
			commonFunctions.getElement(driver, "eleven.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eleven.frequency1").sendKeys("Every eleven weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every eleven weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eleven_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "eleven.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eleven.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eleven_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "eleven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eleven.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eleven.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eleven_Amount2_EveryelevenWeeks() {
		try {
			commonFunctions.getElement(driver, "eleven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eleven.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eleven.frequency2").sendKeys("Every eleven weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every eleven weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eleven_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "eleven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eleven.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eleven.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eleven_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "eleven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eleven.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eleven.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eleven_Amount3_EveryelevenWeeks() {
		try {
			commonFunctions.getElement(driver, "eleven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eleven.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eleven.frequency3").sendKeys("Every eleven weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every eleven weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eleven_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "eleven.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "eleven.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "eleven.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void twelve_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "twelve.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "twelve.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void twelve_Amount1_EverytwelveWeeks() {
		try {
			commonFunctions.getElement(driver, "twelve.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "twelve.frequency1").sendKeys("Every twelve weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every twelve weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void twelve_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "twelve.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "twelve.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void twelve_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "twelve.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "twelve.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "twelve.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void twelve_Amount2_EverytwelveWeeks() {
		try {
			commonFunctions.getElement(driver, "twelve.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "twelve.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "twelve.frequency2").sendKeys("Every twelve weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every twelve weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void twelve_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "twelve.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "twelve.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "twelve.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void twelve_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "twelve.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "twelve.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "twelve.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void twelve_Amount3_EverytwelveWeeks() {
		try {
			commonFunctions.getElement(driver, "twelve.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "twelve.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "twelve.frequency3").sendKeys("Every twelve weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every twelve weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void twelve_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "twelve.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "twelve.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "twelve.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void thirteen_Amount1_Month() {
		try {
			commonFunctions.getElement(driver, "thirteen.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "thirteen.frequency1").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void thirteen_Amount1_EverythirteenWeeks() {
		try {
			commonFunctions.getElement(driver, "thirteen.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "thirteen.frequency1").sendKeys("Every thirteen weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every thirteen weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void thirteen_Amount1_Week() {
		try {
			commonFunctions.getElement(driver, "thirteen.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "thirteen.frequency1").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void thirteen_Amount2_Month() {
		try {
			commonFunctions.getElement(driver, "thirteen.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "thirteen.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "thirteen.frequency2").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void thirteen_Amount2_EverythirteenWeeks() {
		try {
			commonFunctions.getElement(driver, "thirteen.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "thirteen.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "thirteen.frequency2").sendKeys("Every thirteen weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every thirteen weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void thirteen_Amount2_Week() {
		try {
			commonFunctions.getElement(driver, "thirteen.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "thirteen.amount2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "thirteen.frequency2").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void thirteen_Amount3_Month() {
		try {
			commonFunctions.getElement(driver, "thirteen.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "thirteen.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "thirteen.frequency3").sendKeys("Month");
			initializer.successCallwithSnapShot("Amount per month enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void thirteen_Amount3_EverythirteenWeeks() {
		try {
			commonFunctions.getElement(driver, "thirteen.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "thirteen.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "thirteen.frequency3").sendKeys("Every thirteen weeks");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per every thirteen weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void thirteen_Amount3_Week() {
		try {
			commonFunctions.getElement(driver, "thirteen.addbutton").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "thirteen.amount3").sendKeys(getAmount());
			commonFunctions.getElement(driver, "thirteen.frequency3").sendKeys("Week");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Income---Income summary****************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void edit_HHMember_One_IncomeSummary() {
		try {
			commonFunctions.getElement(driver, "edit.hh1.incomesummary").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot(
					"'Application navigate to FName LName(DD.MMM.YYYY)'s income and benefits' page successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void edit_HHMember_Two_IncomeSummary() {
		try {
			commonFunctions.getElement(driver, "edit.hh2.incomesummary").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot(
					"'Application navigate to FName LName(DD.MMM.YYYY)'s income and benefits' page successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void edit_HHMember_Three_IncomeSummary() {
		try {
			commonFunctions.getElement(driver, "edit.hh3.incomesummary").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot(
					"'Application navigate to FName LName(DD.MMM.YYYY)'s income and benefits' page successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void edit_HHMember_Four_IncomeSummary() {
		try {
			commonFunctions.getElement(driver, "edit.hh4.incomesummary").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot(
					"'Application navigate to FName LName(DD.MMM.YYYY)'s income and benefits' page successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_HHMember_IncomeSummary() {
		try {
			commonFunctions.getElement(driver, "add.hhmember").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "other.addbutton").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_HHMember_IncomeSummary_Active() {
		try {
			commonFunctions.getElement(driver, "add.hhmember.active").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "other.addbutton").getText();
			initializer.infoCall("Click " + name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_HHMember_One_IncomeSummary() {
		try {
			commonFunctions.getElement(driver, "add.hh1.incomesummary").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "other.addbutton").getText();
			initializer.infoCall("Click " + name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_HHMember_Two_IncomeSummary() {
		try {
			commonFunctions.getElement(driver, "add.hh2.incomesummary").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "other.addbutton").getText();
			initializer.infoCall("Click " + name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_HHMember_Three_IncomeSummary() {
		try {
			commonFunctions.getElement(driver, "add.hh3.incomesummary").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "other.addbutton").getText();
			initializer.infoCall("Click " + name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_HHMember_four_IncomeSummary() {
		try {
			commonFunctions.getElement(driver, "add.hh4.incomesummary").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "other.addbutton").getText();
			initializer.infoCall("Click " + name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Expenses---Almost there!***************************************************************************************************************

//	*********************Expenses---Housing and Utility Costs---Tell us about your housing costs_1**************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void heat_Utility_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "utility.heat").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select utility costs as 'Heat(oil, gas, electricity or propane, etc.)' successfully in '" + title
							+ " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void electricity_Utility_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "utility.ac").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select utility costs as 'Electricity for an air conditioner' successfully in '" + title
							+ " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void feeForAC_Utility_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "utility.feeac").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select utility costs as 'A fee to use an air conditioner' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void gas_Utility_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "utility.gas").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select utility costs as 'Electricity and / or gas' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void phone_Utility_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "utility.phone").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select utility costs as 'Phone or cell phone service' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void none_Utility_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "utility.none").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select utility costs as 'None' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void rent_Shelter_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "shelter.rent").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select shelter costs as 'Rent' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void mortgage_Shelter_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "shelter.mortgage").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select shelter costs as 'Mortgage' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void propertyTaxes_Shelter_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "shelter.propertytax").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select shelter costs as 'Property Taxes' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void homeInsurence_Shelter_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "shelter.homeinsurance").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select shelter costs as 'Home Insurence' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void condoFee_Shelter_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "shelter.condofee").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select shelter costs as 'Condo Fee' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void none_Shelter_HousingCosts() {
		try {
			commonFunctions.getElement(driver, "shelter.none").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select shelter costs as 'None' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Expenses---Housing and Utility Costs---Tell us about your housing costs_2**************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void add_Rent() {
		try {
			commonFunctions.getElement(driver, "fs.rentadd").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "fs.rentadd").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_Mortgage() {
		try {
			commonFunctions.getElement(driver, "fs.mortgageadd").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "fs.mortgageadd").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_PropertyTaxes() {
		try {
			commonFunctions.getElement(driver, "fs.propertyadd").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "fs.propertyadd").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_HomeInsurence() {
		try {
			commonFunctions.getElement(driver, "fs.hinsuranceadd").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "fs.hinsuranceadd").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void add_CondoFee() {
		try {
			commonFunctions.getElement(driver, "fs.condoadd").click();
			initializer.wait(2);
			String name = commonFunctions.getElement(driver, "fs.condoadd").getText();
			initializer.infoCall(name + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Expenses---Child or Adult Dependent Care Costs---Tell us about your Child or Adult Dependent Care costs********************************
	/**
	 * @author Santhosh Karra
	 */
	public void grantee_Transport_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "childcare.grantee1a").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select grantee successfully in ' " + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dependent_One_Transport_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "childcare.dep1a").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select child successfully in ' " + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void grantee_Pay_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "childcare.grantee1b").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select grantee successfully in ' " + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dependent_One_Pay_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "childcare.dep1b").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select child successfully in ' " + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Expenses---Child or Adult Dependent Care Costs---Child or Adult Dependent Care costs for FirstName LastName (DD.MMM.YYYY)**************
	/**
	 * @author Santhosh Karra
	 */
	public void providerCare_Amount_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "provider.amount").sendKeys(getAmount());
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("'Provider care' amount enter successfully in '" + title + "' page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void providerCare_Month_ChildAdultDependentCareCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "provider.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Provider care cost' select as 'Month' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void providerCare_Year_ChildAdultDependentCareCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "provider.frequency").sendKeys(Keys.ARROW_DOWN);
			initializer.wait(2);
			commonFunctions.getElement(driver, "provider.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Provider care cost' select as 'Year' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void providerCare_Week_ChildAdultDependentCareCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "provider.frequency").sendKeys(Keys.ARROW_DOWN);
			commonFunctions.getElement(driver, "provider.frequency").sendKeys(Keys.ARROW_DOWN);
			initializer.wait(2);
			commonFunctions.getElement(driver, "provider.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Provider care cost' select as 'Week' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void yes_Transportation_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "transportation.yes").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void no_Transportation_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "transportation.no").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void transportation_Amount_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "transportation.amount").sendKeys(getAmount());
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("'Transportation' amount enter successfully in '" + title + "' page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void transportation_Month_ChildAdultDependentCareCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "transportation.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Transportation cost' select as 'Month' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void transportation_Year_ChildAdultDependentCareCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "transportation.frequency").sendKeys(Keys.ARROW_DOWN);
			initializer.wait(2);
			commonFunctions.getElement(driver, "transportation.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Transportation cost' select as 'Year' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void transportation_Week_ChildAdultDependentCareCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "transportation.frequency").sendKeys(Keys.ARROW_UP);
			initializer.wait(2);
			commonFunctions.getElement(driver, "transportation.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Transportation cost' select as 'Week' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void yes_Drive_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "drive.yes").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot("Select option as 'YES' successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void no_Drive_ChildAdultDependentCareCosts() {
		try {
			commonFunctions.getElement(driver, "drive.no").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot("Select option as 'No' successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Expenses---Child or Adult Dependent Care Costs---FirstName LastName (DD.MMM.YYYY)'s Child or Adult Dependent Care address**************
	/**
	 * @author Santhosh Karra
	 */
	public void providerAddress_Trips() {
		try {
			commonFunctions.getElement(driver, "provideraddress.trips").sendKeys(getDay());
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("'Provider trips' enter successfully in '" + title + "' page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void providerAddress_Trips_Month() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "provideraddress.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Provider trips' select as 'Month' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void providerAddress_Trips_Year() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "provideraddress.frequency").sendKeys(Keys.ARROW_DOWN);
			initializer.wait(2);
			commonFunctions.getElement(driver, "provideraddress.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Provider trips' select as 'Year' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void providerAddress_Trips_Week() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "provideraddress.frequency").sendKeys(Keys.ARROW_UP);
			initializer.wait(2);
			commonFunctions.getElement(driver, "provideraddress.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Provider trips' select as 'Week' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Expenses---Child Support Costs---Tell us about your Child support costs****************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void grantee_Amount_ChildsupportCosts() {
		try {
			commonFunctions.getElement(driver, "grantee.childsupportcost").sendKeys(getAmount());
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer
					.successCallwithSnapShot("Grantee child support cost enter successfully in '" + title + "' page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void grantee_Month_ChildsupportCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "grantee.childsupportcost.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of child support cost select as 'Month' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void grantee_Year_ChildsupportCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "grantee.childsupportcost.frequency").sendKeys(Keys.ARROW_DOWN);
			initializer.wait(2);
			commonFunctions.getElement(driver, "grantee.childsupportcost.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of child support cost select as 'Year' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void grantee_Week_ChildsupportCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "grantee.childsupportcost.frequency").sendKeys(Keys.ARROW_UP);
			initializer.wait(2);
			commonFunctions.getElement(driver, "grantee.childsupportcost.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of child support cost select as 'Week' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dependent_One_Amount_ChildsupportCosts() {
		try {
			commonFunctions.getElement(driver, "dependent1.childsupportcost").sendKeys(getDay());
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Dependent child support cost enter successfully in '" + title + "' page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dependent_One_Month_ChildsupportCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "dependent1.childsupportcost.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of child support cost select as 'Month' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dependent_One_Year_ChildsupportCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "dependent1.childsupportcost.frequency").sendKeys(Keys.ARROW_DOWN);
			initializer.wait(2);
			commonFunctions.getElement(driver, "dependent1.childsupportcost.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of child support cost select as 'Year' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dependent_One_Week_ChildsupportCosts() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "dependent1.childsupportcost.frequency").sendKeys(Keys.ARROW_UP);
			initializer.wait(2);
			commonFunctions.getElement(driver, "dependent1.childsupportcost.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of child support cost select as 'Week' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Expenses---Medical Costs---Tell us about your medical costs****************************************************************************

//	*********************Expenses---Medical Costs---Medical costs for FirstName LastName (DD.MMM.YYYY)**********************************************************

//	*********************Expenses---Medical Costs---FirstName LastName (DD.MMM.YYYY)'s Medical appointment/Pharmacy Address*************************************
	/**
	 * @author Santhosh Karra
	 */
	public void pharmacy_Transportation_Amount() {
		try {
			commonFunctions.getElement(driver, "pharmacy.transportation.amount").sendKeys(getAmount());
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("'Transportation' amount enter successfully in '" + title + "' page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void pharmacy_Transportation_Month() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "pharmacy.transportation.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Transportation cost' select as 'Month' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void pharmacy_Transportation_Year() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "pharmacy.transportation.frequency").sendKeys(Keys.ARROW_DOWN);
			initializer.wait(2);
			commonFunctions.getElement(driver, "pharmacy.transportation.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Transportation cost' select as 'Year' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void pharmacy_Transportation_Week() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "pharmacy.transportation.frequency").sendKeys(Keys.ARROW_UP);
			initializer.wait(2);
//			commonFunctions.getElement(driver, "pharmacy.transportation.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of 'Transportation cost' select as 'Week' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************EBT Card---Do you need an EBT Card?****************************************************************************************************

//	*********************Sign & submit---Sign & Submit**********************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void signSubmitValidationMsg() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed " + title + " page successfully");
			initializer.wait(2);
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(2);
			try {

				String agree = commonFunctions.getElement(driver, "agree.validation").getText();
				initializer.successCallwithSnapShot("Agree check box - field validation: " + agree);
				Assert.assertEquals(agree, agreeValidation);

				String sign = commonFunctions.getElement(driver, "sign.validation").getText();
				initializer.successCallwithSnapShot("Sign your application by typing your full name below - field validation: " + sign);
				Assert.assertEquals(sign, signValidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			initializer.successCallwithSnapShot("'" + title + "' Page Required field validated successfully ");
			initializer.wait(2);
		} catch (Exception e) {

			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void signSubmit() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed " + title + " page successfully");
			commonFunctions.getElement(driver, "sign.agree").click();
			commonFunctions.getElement(driver, "sign.sign").sendKeys(getName());
			initializer.wait(3);
			initializer.successCallwithSnapShot("Client Agree and Signed successfully ");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(10);
			initializer.successCallwithSnapShot("Client clicked submit Signed successfully ");
			initializer.wait(15);
			String myNumber = commonFunctions.getElement(driver, "fsapplication.number").getText();
//			deleteRecord_DB(myNumber.split(":")[1].trim());
			commonFunctions.getElement(driver, "sebtapplication.download").click();
			initializer.wait(5);
			initializer.successCallwithSnapShot(myNumber + " and it was downloaded successfully!");
			initializer.successCallwithSnapShot(
					"Datasheet available at \\DTAConnect\\src\\test\\resources\\Results\\Downloads");
			initializer.wait(2);

		} catch (Exception e) {

			initializer.failureCallwithException("Exception occured in 'Sign & submit---Sign & Submit' Page", e);

		}
		
		}

	/**
	 * @author Larry Sweeney
	 */
	public void sebtSignSubmit() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed " + title + " page successfully");
			commonFunctions.getElement(driver, "sign.agree").click();
			commonFunctions.getElement(driver, "sign.sign").sendKeys(getName());
			initializer.wait(3);
			initializer.successCallwithSnapShot("Client Agree and Signed successfully ");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(10);
			initializer.successCallwithSnapShot("Client clicked submit Signed successfully ");
			initializer.wait(15);
			String myNumber = commonFunctions.getElement(driver, "fsapplication.number").getText();
//			deleteRecord_DB(myNumber.split(":")[1].trim());
			commonFunctions.getElement(driver, "sebtapplication.download").click();
			initializer.wait(5);
			initializer.successCallwithSnapShot(myNumber + " and it was downloaded successfully!");
			initializer.successCallwithSnapShot(
					"Datasheet available at \\DTAConnect\\src\\test\\resources\\Results\\Downloads");
			initializer.wait(2);

		} catch (Exception e) {

			initializer.failureCallwithException("Exception occured in 'Sign & submit---Sign & Submit' Page", e);

		}
	}
		/**
		 * @author Larry Sweeney
		 */
		public void sebtSignSubmit_NoDownload() {
			try {
				String title = commonFunctions.getElement(driver, "page.title").getText();
				initializer.successCallwithSnapShot("Displayed " + title + " page successfully");
				commonFunctions.getElement(driver, "sign.agree").click();
				commonFunctions.getElement(driver, "sign.sign").sendKeys(getName());
				initializer.wait(3);
				initializer.successCallwithSnapShot("Client Agree and Signed successfully ");
				commonFunctions.getElement(driver, "generic.continue").click();
				initializer.wait(10);
				initializer.successCallwithSnapShot("Client clicked submit Signed successfully ");
//				initializer.wait(15);
//				String myNumber = commonFunctions.getElement(driver, "fsapplication.number").getText();
//				deleteRecord_DB(myNumber.split(":")[1].trim());
//				commonFunctions.getElement(driver, "sebtapplication.download").click();
//				initializer.wait(5);
//				initializer.successCallwithSnapShot(myNumber + " and it was downloaded successfully!");
//				initializer.successCallwithSnapShot(
//						"Datasheet available at \\DTAConnect\\src\\test\\resources\\Results\\Downloads");
//				initializer.wait(2);

			} catch (Exception e) {

				initializer.failureCallwithException("Exception occured in 'Sign & submit---Sign & Submit' Page", e);

			}
		}
	public void signSubmit_ES() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed " + title + " page successfully");
			commonFunctions.getElement(driver, "sign.agree").click();
			commonFunctions.getElement(driver, "sign.sign").sendKeys(getName());
			initializer.wait(3);
			initializer.successCallwithSnapShot("Client Agree and Signed successfully ");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(10);
			initializer.successCallwithSnapShot("Client clicked submit and Signed successfully ");
			initializer.wait(15);
			String myNumber = commonFunctions.getElement(driver, "fsapplication.number.es").getText();
			initializer.infoCall("number" + myNumber);
//			deleteRecord_DB(myNumber.split(":")[1].trim());
			commonFunctions.getElement(driver, "fsapplication.download.es").click();
			initializer.wait(15);
			initializer.successCallwithSnapShot(myNumber + " and it was downloaded successfully!");
			initializer.successCallwithSnapShot(
					"Datasheet available at \\DTAConnect\\src\\test\\resources\\Results\\Downloads");
			initializer.wait(2);

		} catch (Exception e) {

			initializer.failureCallwithException("Exception occured in 'Sign & submit---Sign & Submit' Page", e);

		}
	}
//  *********************SNAP Eligibility Screener**********************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void SNAP_Eligibility_Button() {
		try {
			initializer.wait(5);
			commonFunctions.getElement(driver, "fs.snapEligibility").click();
			initializer.infoCall("Clicking 'Find out if you may eligible' button sucessfully.");
			initializer.wait(5);
			initializer.successCallwithSnapShot("'Am I eligible for SNAP' application is launched successfully");
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_Seniors_Yes() {
		try {
			commonFunctions.getElement(driver, "eligibility.seniors.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_Seniors_No() {
		try {
			commonFunctions.getElement(driver, "eligibility.seniors.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_Disabilities_Yes() {
		try {
			commonFunctions.getElement(driver, "eligibility.disabilities.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_Disabilities_No() {
		try {
			commonFunctions.getElement(driver, "eligibility.disabilities.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_Amount() {
		try {
			commonFunctions.getElement(driver, "eligibility.income.amount").sendKeys(getAmount());
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Total gross income for household enter successfully in '" + title + "' page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_Month() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "eligibility.income.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of household grossincome select as 'Month' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_Year() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "eligibility.income.frequency").sendKeys(Keys.ARROW_DOWN);
			initializer.wait(2);
			commonFunctions.getElement(driver, "eligibility.income.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of household grossincome select as 'Year' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_Week() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "eligibility.income.frequency").sendKeys(Keys.ARROW_UP);
			initializer.wait(2);
			commonFunctions.getElement(driver, "eligibility.income.frequency").sendKeys(Keys.ENTER);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Frequency of household grossincome select as 'Week' successfully in '" + title + "' page.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_handyDocs() {
		try {

			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Displayed '" + title + "' page successfully");
			String handy = commonFunctions.getElement(driver, "eligibility.text").getText();

			initializer.successCallwithSnapShot(handy);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_people_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "eligibility.validation.people").getText();
				initializer.infoCall("Household size validation: " + val);
				Assert.assertEquals(val, elgibility_people);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "eligibility.people").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_seniors_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "eligibility.validation.seniors").getText();
				initializer.infoCall("Household seniors validation: " + val);
				Assert.assertEquals(val, elgibility_seniors);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "eligibility.seniors").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_disabilities_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "eligibility.validation.disabilities").getText();
				initializer.infoCall("Household disabilities validation: " + val);
				Assert.assertEquals(val, elgibility_disabilities);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "eligibility.disabilities").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void eligibility_income_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "eligibility.validation.income").getText();
				initializer.infoCall("Household income validation: " + val);
				Assert.assertEquals(val, elgibility_income);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "eligibility.income").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}
//**************************************Summer EBT************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void phoneNotificationY() {
		try {
			commonFunctions.getElement(driver, "sebt.phoneNotificationY").click();
			initializer.infoCall("Allow Phone Notifications Yes button clicked successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	/**
	 * @author Santhosh Karra
	 */
	public void phoneNotificationN() {
		try {
			commonFunctions.getElement(driver, "sebt.phoneNotificationN").click();
			initializer.infoCall("Allow Phone Notifications No button clicked successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	/**
	 * @author Santhosh Karra
	 */
	public void emailNotificationY() {
		try {
			commonFunctions.getElement(driver, "sebt.emailNotificationY").click();
			initializer.infoCall("Allow Email Notifications Yes button clicked successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	/**
	 * @author Santhosh Karra
	 */
	public void emailNotificationN() {
		try {
			commonFunctions.getElement(driver, "sebt.emailNotificationN").click();
			initializer.infoCall("Allow Email Notifications No button clicked successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	
	/**
	 * @author Larry Sweeney
	 */
	public void typeSchoolPublicChart() {
		try {
			commonFunctions.getElement(driver, "sebt.typeSchoolPublicChart").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select school type as 'Public/Charter' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
		
	}
		
		/**
		 * @author Larry Sweeney
		 */
		public void typeSchoolPrivate() {
			try {
				commonFunctions.getElement(driver, "sebt.typeSchoolPrivate").click();
				String title = commonFunctions.getElement(driver, "page.title").getText();
				initializer.successCallwithSnapShot("Select school type as 'Private' successfully in '" + title + " 'page.");
			} catch (Exception e) {
				String title = commonFunctions.getElement(driver, "page.title").getText();
				initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		
		}
	

	/**
	 * @author Larry Sweeney
	 */
	public void typeSchoolNotEnrolled() {
		try {
			commonFunctions.getElement(driver, "sebt.typeSchoolNotEnrolled").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select school type as 'Not enrolled' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}


	/**
	 * @author Larry Sweeney
	 */
	public void schoolDistrict() {
		try {
			initializer.wait(1);
			selectDropDown(commonFunctions.getElement(driver, "sebt.schoolDistrict"), getSnapData("SchoolDistrict"));
			initializer.infoCall("School District selected sucessfully.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
		
	}
	/**
	 * @author Larry Sweeney
	 */
	public void schoolName() {
		try {
			initializer.wait(1);
			selectDropDown(commonFunctions.getElement(driver, "sebt.schoolName"), getSnapData("SchoolName"));
			initializer.infoCall("School Name selected sucessfully.");
			initializer.wait(2);
	} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
		
	}


	/**
	 * @author Larry Sweeney
	 */
	public void schoolCity() {
		try {
			initializer.wait(1);
			selectDropDown(commonFunctions.getElement(driver, "sebt.schoolCity"), getSnapData("SchoolCity"));
			initializer.infoCall("School City selected sucessfully.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}


	/**
	 * @author Larry Sweeney
	 */
	public void pvtSchoolName() {
		try {
			initializer.wait(1);
			selectDropDown(commonFunctions.getElement(driver, "sebt.pvtSchoolName"), getSnapData("PvtSchoolName"));
			initializer.infoCall("Private School Name selected sucessfully.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
		
		
	}
	/**
	 * @author Larry Sweeney
	 */
	public void studentIDNumber() {
		try {
		commonFunctions.getElement(driver, "sebt.studentIDNumber").sendKeys(getSnapData("SASID"));
		initializer.infoCall("Student ID Number inserted successfully");
		initializer.wait(2);
	} catch (Exception e) {
		String title = commonFunctions.getElement(driver, "page.title").getText();
		initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
	   }
	}
	
	/**
	 * @author Larry Sweeney
	 */
	public void snapBenefitsY() {
		try {
			commonFunctions.getElement(driver, "sebt.snapBenefitsY").click();
			initializer.infoCall("Do you receive SNAP Benefits Yes button clicked successfully.");
	} catch (Exception e) {
		String title = commonFunctions.getElement(driver, "page.title").getText();
		initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
		
	}
		/**
		 * @author Larry Sweeney
		 */
		public void snapBenefitsN() {
			try {
				commonFunctions.getElement(driver, "sebt.snapBenefitsN").click();
				initializer.infoCall("Do you receive SNAP Benefits No button clicked successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		}
		/**
		 * @author Larry Sweeney
		 */
		public void tafdcBenefitsY() {
			try {
				commonFunctions.getElement(driver, "sebt.tafdcBenefitsY").click();
				initializer.infoCall("Do you receive TAFDC Benefits Yes button clicked successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
		
	}
	/**
	 * @author Larry Sweeney
	 */
	public void agencyID() {
		try {
		commonFunctions.getElement(driver, "sebt.agencyID").sendKeys(getSnapData("AgencyID"));
		initializer.infoCall("Agency ID inserted successfully");
		initializer.wait(2);
	} catch (Exception e) {
		String title = commonFunctions.getElement(driver, "page.title").getText();
		initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
	   }
	}
		
	/**
	* @author Larry Sweeney
	*/
	public void massHealthY() {
		try {
		commonFunctions.getElement(driver, "sebt.massHealthY").click();
		initializer.infoCall("Do you receive Mass Health Benefits Yes button clicked successfully.");
	} catch (Exception e) {
		String title = commonFunctions.getElement(driver, "page.title").getText();
		initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	/**
	* @author Larry Sweeney
	*/
	public void massHealthN() {
		try {
		commonFunctions.getElement(driver, "sebt.massHealthN").click();
		initializer.infoCall("Do you receive Mass Health Benefits No button clicked successfully.");
	} catch (Exception e) {
		String title = commonFunctions.getElement(driver, "page.title").getText();
		initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	/**
	 * @author Larry Sweeney
	 */
	public void massHealthID() {
		try {
		commonFunctions.getElement(driver, "sebt.massHealthID").sendKeys(getSnapData("MassHealthID"));
		initializer.infoCall("Mass Health ID Number inserted successfully");
		initializer.wait(2);
	} catch (Exception e) {
		String title = commonFunctions.getElement(driver, "page.title").getText();
		initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
	   }
	   
	}

	/**
	 * @author Larry Sweeney
	 */
	public void exitWithoutUploading() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "sebt.exitWithoutUploading").click();
			initializer.wait(2);
			initializer.infoCall("Click link Exit without uploading documents successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	
	}
	/**
	 * @author Larry Sweeney
	 */
	public void add_Another_HHMember_Income() {
		try {
			commonFunctions.getElement(driver, "add.hhmember").click();
			/*initializer.wait(2);
			String name = commonFunctions.getElement(driver, "income.addbutton").getText();
			initializer.infoCall("Click " + name + " successfully.");*/
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	/**
	 * @author Larry Sweeney
	 */
	public void select_Another_HHMember_Income() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "interim.childsupport").click();
			/*initializer.wait(2);
			String name = commonFunctions.getElement(driver, "interim.childsupport").getText();
			initializer.infoCall("Click " + name + " successfully.");*/
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void loginButton() {
		try {
			initializer.wait(5);
			commonFunctions.getElement(driver, "login.homebutton").click();
			initializer.infoCall("Clicking 'Login' button sucessfully.");
			initializer.wait(5);
			initializer.successCallwithSnapShot("'Login' application is launched successfully");
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
		
	public void loginEmail() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "login.email").sendKeys(getSnapData("LogInEmail"));
			initializer.wait(2);
			initializer.infoCall("Enter Login Email successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void loginPassword() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "login.password").sendKeys(getSnapData("LogInPassword"));
			initializer.wait(2);
			initializer.infoCall("Enter Login Password successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void loginFormButton() {
		try {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.successCallwithSnapShot("Enter all '" + title + "' Page information successfully.");
			commonFunctions.getElement(driver, "login.continue").click();
			initializer.wait(2);
			initializer.infoCall("Click 'Login' button sucessfully.");
			initializer.wait(3);
			initializer.successCallwithSnapShot("Navigate to Login form launched succuessfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void connectToMyDTA() {
		try {
			initializer.wait(5);
			commonFunctions.getElement(driver, "account.connectDTA").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "consumer.birthDay").sendKeys(getSnapData("Day2"));
			selectDropDown(commonFunctions.getElement(driver, "consumer.birthMonth"), getSnapData("Month2"));
			commonFunctions.getElement(driver, "consumer.birthYear").sendKeys(getSnapData("Year2"));
			commonFunctions.getElement(driver, "provider.agencyIdRadio").click();
			initializer.wait(2);
			commonFunctions.getElement(driver, "account.textbox").sendKeys(getSnapData("AgencyID"));
			commonFunctions.getElement(driver, "account.connect").click();
			initializer.wait(4);
			initializer.successCallwithSnapShot("Your case is now linked to your DTA Connect account successfully");
			initializer.wait(5);
			commonFunctions.getElement(driver, "account.ok").click();
			initializer.wait(2);

		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'Connect to my DTA info.'", e);
		}
	}
}
	
