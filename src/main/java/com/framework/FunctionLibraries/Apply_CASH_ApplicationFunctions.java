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

public class Apply_CASH_ApplicationFunctions {
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

	public Apply_CASH_ApplicationFunctions() {
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

	public Apply_CASH_ApplicationFunctions(CommonFunctions commonFunctions, Initializer initializer, Stage stage,
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
			initializer.log("Issue on launching Apply SNAP! Application. Exception : " + e.getMessage());
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
	String Languagevalidation = "Please choose language";
	String addressvalidation = "Please enter a valid address";
	String phonevalidation = "Please enter a valid phone number";
	String selectoptionvalidation = "Please select an option below";
	String emailvalidation = "Please enter a valid email";
	String zipvalidation_tafdc = "This TAFDC application is for Massachussetts residents. If you live in Massachussetts, please enter your Zip Code. If you do not live in Massachussetts, you must apply in your state.";
	String zipvalidation_eaedc = "This EAEDC application is for Massachussetts residents. If you live in Massachussetts, please enter your Zip Code. If you do not live in Massachussetts, you must apply in your state.";
	String householdvalidation = "Please choose no. of household";
	String agreeValidation = "Please agree to the terms";
	String signValidation = "Please sign your application";

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
	public String getAPID() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 7, num.length());
		String APID = number;
		return APID;
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

//	*********************Apply CASH Applications! Generic methods************************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	
	/**
	 * 
	 * @author Kishore B
	 * @return Test data file path reference
	 */
	public String getCashData(String param) {
		return commonFunctions.getTestData(stage.getTestName(), param, "file.cashDataFilePath");
	}
	
	public void app_Down() {

		try {
			initializer.wait(2);
			String title1 = commonFunctions.getElement(driver, "appdown").getText();
			String title2 = commonFunctions.getElement(driver, "appdownback").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title1 + " " + title2 + "' Page successfully.");
			initializer.wait(2);

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void applyTAFDCButton() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "cash.applyTAFDC").click();
			initializer.infoCall("Clicking 'Apply TAFDC!' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'Apply TAFDC!' application is launched successfully");
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}
	public void applyTAFDCButton_ES() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "cash.applyTAFDC.es").click();
			initializer.infoCall("Clicking 'Apply TAFDC!' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'Apply TAFDC!' application is launched successfully");
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}
	/**
	 * @author Santhosh Karra
	 */
	public void applyEAEDCButton() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "cash.applyEAEDC").click();
			initializer.infoCall("Clicking 'Apply EAEDC!' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'Apply EAEDC!' application is launched successfully");
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}
	public void applyEAEDCButton_ES() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "cash.applyEAEDC.es").click();
			initializer.infoCall("Clicking 'Apply EAEDC!' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'Apply EAEDC!' application is launched successfully");
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
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
			initializer.infoCall("Click 'Continue' button sucessfully.");
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
			String handy = commonFunctions.getElement(driver, "tafdc.handy").getText();
			String handy1 = commonFunctions.getElement(driver, "tafdc.handy1").getText();
			String handy2 = commonFunctions.getElement(driver, "tafdc.handy2").getText();
			String handy3 = commonFunctions.getElement(driver, "tafdc.handy3").getText();
			initializer.successCallwithSnapShot(handy + "<br>" + handy1 + "<br>" + handy2 + "<br>" + handy3);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//  *********************Initial Application for TAFDC**********************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void apply_CASH__ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String fname = commonFunctions.getElement(driver, "fs.fnvalidation").getText();
				String lname = commonFunctions.getElement(driver, "fs.lnvalidation").getText();
				String dob = commonFunctions.getElement(driver, "fs.dobvalidation").getText();
				String addr = commonFunctions.getElement(driver, "fs.addressvalidation").getText();
				String phone = commonFunctions.getElement(driver, "fs.phonevalidation").getText();
				String language = commonFunctions.getElement(driver, "cash.languagevalidation").getText();
				String children = commonFunctions.getElement(driver, "cash.childrenvalidation").getText();
				String money = commonFunctions.getElement(driver, "cash.moneyvalidation").getText();
				String income = commonFunctions.getElement(driver, "tafdc.incomevalidation").getText();
				initializer.infoCall("First Name validation: " + fname);
				initializer.infoCall("Last Name validation: " + lname);
				initializer.infoCall("Date of Birth validation: " + dob);
				initializer.infoCall("Address validation: " + addr);
				initializer.infoCall("Phone number validation: " + phone);
				initializer.infoCall("Language selection validation: " + language);
				initializer.infoCall("Date of Birth validation: " + children);
				initializer.infoCall("Address validation: " + money);
				initializer.infoCall("Phone number validation: " + income);
				Assert.assertEquals(fname, FValidation);
				Assert.assertEquals(lname, LValidation);
				Assert.assertEquals(dob, DOBValidation);
				Assert.assertEquals(addr, addressvalidation);
				Assert.assertEquals(phone, phonevalidation);
				Assert.assertEquals(language, Languagevalidation);
				Assert.assertEquals(children, selectoptionvalidation);
				Assert.assertEquals(money, selectoptionvalidation);
				Assert.assertEquals(income, selectoptionvalidation);

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"First Name, Last Name, Date of birth, Address, Phone and Select options fields validated successfully in '"
							+ title + "' Page.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void firstName_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "fs.fnvalidation").getText();
				initializer.infoCall("First name field validation: " + val);
				Assert.assertEquals(val, FValidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "first.name").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void lastName_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "fs.lnvalidation").getText();
				initializer.infoCall("Last name field validation: " + val);
				Assert.assertEquals(val, LValidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "last.name").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void dateofbirth_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "fs.dobvalidation").getText();
				initializer.infoCall("Date of Birth validation: " + val);
				Assert.assertEquals(val, DOBValidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "dob").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void address_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String addr = commonFunctions.getElement(driver, "fs.addressvalidation").getText();
				initializer.infoCall("Address validation: " + addr);
				Assert.assertEquals(addr, addressvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "address").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void phone_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String phone = commonFunctions.getElement(driver, "fs.phonevalidation").getText();
				initializer.infoCall("Phone number validation: " + phone);
				Assert.assertEquals(phone, phonevalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "cash.phone").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void language_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String language = commonFunctions.getElement(driver, "cash.languagevalidation").getText();
				initializer.infoCall("Language selection validation: " + language);
				Assert.assertEquals(language, Languagevalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "cash.language").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}
	public void languageSelect() {
		try {
			initializer.wait(5);
			selectDropDown(commonFunctions.getElement(driver, "language.select"), getCashData("Language"));
			initializer.infoCall("Language selected sucessfully.");
			initializer.wait(5);
			} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}
	/**
	 * @author Santhosh Karra
	 */
	public void sSN_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "household.ssn").sendKeys(getWrongSSN());
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
	public void children_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "cash.childrenvalidation").getText();
				initializer.infoCall("Required field validation: " + val);
				Assert.assertEquals(val, selectoptionvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "cash.children").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void money_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "cash.moneyvalidation").getText();
				initializer.infoCall("Required field validation: " + val);
				Assert.assertEquals(val, selectoptionvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "cash.money").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void incomeTAFDC_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "tafdc.incomevalidation").getText();
				initializer.infoCall("Required field validation: " + val);
				Assert.assertEquals(val, selectoptionvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "cash.income").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void incomeEAEDC_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "eaedc.incomevalidation").getText();
				initializer.infoCall("Required field validation: " + val);
				Assert.assertEquals(val, selectoptionvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "cash.income").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void disability_EAEDC_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String val = commonFunctions.getElement(driver, "eaedc.disabilityvalidation").getText();
				initializer.infoCall("Required field validation: " + val);
				Assert.assertEquals(val, selectoptionvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "cash.children").getText();
			initializer.successCallwithSnapShot("'" + title + "' field validatation successfully.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page.", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void email_ValidationMsg() {
		try {

			commonFunctions.getElement(driver, "fs.email").sendKeys(getWrongEmail());
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String email = commonFunctions.getElement(driver, "fs.emailvalidation").getText();
				initializer.infoCall("Email validation: " + email);
				Assert.assertEquals(email, emailvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("All '" + title + "' Page Required fields validated successfully ");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void addressZipCode_TAFDC_ValidationMsg() {
		try {

			initializer.wait(2);
			commonFunctions.getElement(driver, "fs.street").sendKeys(getStreetName());
			commonFunctions.getElement(driver, "fs.zip").sendKeys(getWrongZip());
			initializer.wait(2);
			commonFunctions.getElement(driver, "generic.continue").click();
			try {

				String zip = commonFunctions.getElement(driver, "cash.zipcodevalidation").getText();
				initializer.infoCall("Massachusetts residents Zip code validation: " + zip);
				Assert.assertEquals(zip, zipvalidation_tafdc);
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
	public void addressZipCode_EAEDC_ValidationMsg() {
		try {

			initializer.wait(2);
			commonFunctions.getElement(driver, "fs.street").sendKeys(getStreetName());
			commonFunctions.getElement(driver, "fs.zip").sendKeys(getWrongZip());
			initializer.wait(2);
			commonFunctions.getElement(driver, "generic.continue").click();
			try {

				String zip = commonFunctions.getElement(driver, "cash.zipcodevalidation").getText();
				initializer.infoCall("Massachusetts residents Zip code validation: " + zip);
				Assert.assertEquals(zip, zipvalidation_eaedc);
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

//	************************************************************************************************************************************************************

	/**
	 * @author Santhosh Karra
	 */
	public void firstLast_Name() {
		try {
			String fname = getCashData("FName");
			String lname = getCashData("LName");
			commonFunctions.getElement(driver, "household.fname").sendKeys(getCashData("FName"));
			commonFunctions.getElement(driver, "household.lname").sendKeys(getCashData("LName"));
			initializer.wait(2);
			initializer.infoCall("Enter First name as '" + fname + "' successfully.");
			initializer.infoCall("Enter Last name as '" + lname + "' successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void middle_Name() {
		try {
			String mname = getCashData("MName");
			commonFunctions.getElement(driver, "household.mname").sendKeys(getCashData("MName"));
			initializer.infoCall("Enter Middle name as '" + mname + "' successfully.");
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
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	************************************************************************************************************************************************************

	/**
	 * @author Santhosh Karra
	 */
	public void dateOfBirth_Minor() {
		try {
			commonFunctions.getElement(driver, "household.day").sendKeys(getDay());
			selectDropDown(commonFunctions.getElement(driver, "household.month"), getMonth());
			commonFunctions.getElement(driver, "household.year").sendKeys(getMinorYear());
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
			commonFunctions.getElement(driver, "household.day").sendKeys(getDay());
			selectDropDown(commonFunctions.getElement(driver, "household.month"), getMonth());
			commonFunctions.getElement(driver, "household.year").sendKeys(getMajorYear());
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
			commonFunctions.getElement(driver, "household.day").sendKeys(getDay());
			selectDropDown(commonFunctions.getElement(driver, "household.month"), getMonth());
			commonFunctions.getElement(driver, "household.year").sendKeys(getSeniorYear());
			initializer.infoCall("Enter date of birth under senior group age.");
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

//	************************************************************************************************************************************************************

	/**
	 * @author Santhosh Karra
	 */
	public void agencyID() {
		try {
			commonFunctions.getElement(driver, "cash.agencyID").clear();
			String APID = getAPID();
			commonFunctions.getElement(driver, "cash.agencyID").sendKeys(APID);
			initializer.infoCall("Enter Agency ID :" + APID + " successfully");
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
			initializer.wait(2);
			commonFunctions.getElement(driver, "fs.street").sendKeys(getStreetName());
			commonFunctions.getElement(driver, "fs.zip").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "fs.zip").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "fs.zip").sendKeys(getZipCode());
			initializer.infoCall("Enter client address successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void phone() {
		try {
			String phone = getPhoneNumber();
			commonFunctions.getElement(driver, "fs.phone").clear();
			commonFunctions.getElement(driver, "fs.phone").sendKeys(phone);
			initializer.infoCall("Enter phone number :" + phone + " successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void email() {
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

//	************************************************************************************************************************************************************

	/**
	 * @author Santhosh Karra
	 */
	public void pregnant_yes_Option() {
		try {
			commonFunctions.getElement(driver, "cash.pregnant.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select pregnant option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void pregnant_no_Option() {
		try {
			commonFunctions.getElement(driver, "cash.pregnant.no").click();
			initializer.wait(2);
			initializer.infoCall("Select pregnant option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void children_yes_Option() {
		try {
			commonFunctions.getElement(driver, "cash.children.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select children option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void children_no_Option() {
		try {
			commonFunctions.getElement(driver, "cash.children.no").click();
			initializer.wait(2);
			initializer.infoCall("Select children option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void money_yes_Option() {
		try {
			commonFunctions.getElement(driver, "cash.money.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select money option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void money_no_Option() {
		try {
			commonFunctions.getElement(driver, "cash.money.no").click();
			initializer.wait(2);
			initializer.infoCall("Select money option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void tty_yes_Option() {
		try {
			commonFunctions.getElement(driver, "cash.tty.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select tty option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void tty_no_Option() {
		try {
			commonFunctions.getElement(driver, "cash.tty.no").click();
			initializer.wait(2);
			initializer.infoCall("Select tty option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void disability_yes_Option() {
		try {
			commonFunctions.getElement(driver, "eaedc.disability.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select disability option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void disability_no_Option() {
		try {
			commonFunctions.getElement(driver, "eaedc.disability.no").click();
			initializer.wait(2);
			initializer.infoCall("Select disability option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void income_TAFDC_Yes_Option() {
		try {
			commonFunctions.getElement(driver, "tafdc.income.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select income option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void income_TAFDC_No_Option() {
		try {
			commonFunctions.getElement(driver, "tafdc.income.no").click();
			initializer.wait(2);
			initializer.infoCall("Select income option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void income_EAEDC_Yes_Option() {
		try {
			commonFunctions.getElement(driver, "eaedc.income.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select income option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void income_EAEDC_No_Option() {
		try {
			commonFunctions.getElement(driver, "eaedc.income.no").click();
			initializer.wait(2);
			initializer.infoCall("Select income option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void source_One() {
		try {

			getDropDownValue(commonFunctions.getElement(driver, "cash.source.one"));
			initializer.wait(2);
//			String value = commonFunctions.getElement(driver, "cash.source.one").getText();
			initializer.successCallwithSnapShot("Select income type successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void amount_One() {
		try {
			String amount = getAmount();
			commonFunctions.getElement(driver, "cash.amount.one").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Enter amount as'" + amount + " 'successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void source_Two() {
		try {

			getDropDownValue(commonFunctions.getElement(driver, "cash.source.two"));
			initializer.wait(2);
//			String value = commonFunctions.getElement(driver, "cash.source.one").getText();
			initializer.successCallwithSnapShot("Select income type successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void amount_Two() {
		try {
			String amount = getAmount();
			commonFunctions.getElement(driver, "cash.amount.two").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Enter amount as'" + amount + " 'successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void source_Three() {
		try {

			getDropDownValue(commonFunctions.getElement(driver, "cash.source.three"));
			initializer.wait(2);
//			String value = commonFunctions.getElement(driver, "cash.source.one").getText();
			initializer.successCallwithSnapShot("Select income type successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void amount_Three() {
		try {
			String amount = getAmount();
			commonFunctions.getElement(driver, "cash.amount.three").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Enter amount as'" + amount + " 'successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void source_Four() {
		try {

			getDropDownValue(commonFunctions.getElement(driver, "cash.source.four"));
			initializer.wait(2);
//			String value = commonFunctions.getElement(driver, "cash.source.one").getText();
			initializer.successCallwithSnapShot("Select income type successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void amount_Four() {
		try {
			String amount = getAmount();
			commonFunctions.getElement(driver, "cash.amount.four").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Enter amount as'" + amount + " 'successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void source_Five() {
		try {

			getDropDownValue(commonFunctions.getElement(driver, "cash.source.five"));
			initializer.wait(2);
//			String value = commonFunctions.getElement(driver, "cash.source.one").getText();
			initializer.successCallwithSnapShot("Select income type successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void amount_Five() {
		try {
			String amount = getAmount();
			commonFunctions.getElement(driver, "cash.amount.five").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Enter amount as'" + amount + " 'successully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ebt_TAFDC_Yes_Option() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "tafdc.ebt.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select ebt option as 'YES' successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ebt_TAFDC_No_Option() {
		try {
			commonFunctions.getElement(driver, "tafdc.ebt.no").click();
			initializer.wait(2);
			initializer.infoCall("Select ebt option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ebt_EAEDC_Yes_Option() {
		try {
			commonFunctions.getElement(driver, "tafdc.ebt.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select ebt option as 'YES' successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	/**
	 * @author Santhosh Karra
	 */
	public void ebt_EAEDC_No_Option() {
		try {
			commonFunctions.getElement(driver, "tafdc.ebt.no").click();
			initializer.wait(2);
			initializer.infoCall("Select ebt option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

//	*********************Sign & submit---Sign & Submit**********************************************************************************************************
	/**
	 * @author Santhosh Karra
	 */
	public void signSubmitValidationMsg() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed '" + title + "' page successfully.");
			initializer.wait(2);
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(2);
			try {

				String agree = commonFunctions.getElement(driver, "agree.validation").getText();
				initializer.successCallwithSnapShot("Agree check box - field validation: " + agree);
				Assert.assertEquals(agree, agreeValidation);

				String sign = commonFunctions.getElement(driver, "sign.validation").getText();
				initializer.successCallwithSnapShot(
						"Sign your application by typing your full name below - field validation: " + sign);
				Assert.assertEquals(sign, signValidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			initializer.successCallwithSnapShot("'" + title + "' Page Required field validated successfully. ");
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
			initializer.successCallwithSnapShot("Displayed '" + title + "' page successfully");
			commonFunctions.getElement(driver, "sign.agree").click();
			commonFunctions.getElement(driver, "sign.sign").sendKeys(getName());
			initializer.wait(3);
			initializer.successCallwithSnapShot("Client Agree and Signed successfully. ");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(10);
			initializer.successCallwithSnapShot("Client submit the application successfully. ");
			initializer.wait(5);
			String myNumber = commonFunctions.getElement(driver, "cash.application").getText();
			initializer.successCallwithSnapShot(myNumber);
			initializer.wait(2);

		} catch (Exception e) {

			initializer.failureCallwithException("Exception occured in 'Sign & submit---Sign & Submit' Page.", e);

		}
	}

//	************************************************************************************************************************************************************

	/**
	 * @author Santhosh Karra
	 */
	public void exit() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed '" + title + "' page successfully.");
			commonFunctions.getElement(driver, "cash.exit").click();
			initializer.successCallwithSnapShot("Exit the application successfully. ");

		} catch (Exception e) {

			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);

		}
	}
	public void exit_ES() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed '" + title + "' page successfully.");
			commonFunctions.getElement(driver, "cash.exit.es").click();
			initializer.successCallwithSnapShot("Exit the application successfully. ");

		} catch (Exception e) {

			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);

		}
	}
}
