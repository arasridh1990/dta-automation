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

public class Create_ONLINE_ApplicationFunctions {

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

	public Create_ONLINE_ApplicationFunctions() {
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

	public Create_ONLINE_ApplicationFunctions(CommonFunctions commonFunctions, Initializer initializer, Stage stage,
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
	 * @param test
	 *            This is ExtentTest object, ExtentTest object is used for report
	 *            generation.
	 * @author Santhosh Karra
	 */
	public void init(ExtentTest test) {
		this.test = test;
	}

	/**
	 * This method initialize the driver object in Application functions library
	 *
	 * @param driver
	 *            This is webdriver object
	 * @author Santhosh Karra
	 */
	public void init(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Function Name : launchURL Description : This function will launch URL *
	 *
	 * @param strURL
	 *            , URL to launch application
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
	 * @throws Exception
	 *             , throws an exception if any while closing the browser
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
	String emailvalidation = "Please enter a valid email address";
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
		// String Small_chars = "abcdefghijklmnopqrstuvwxyz";
		// String values = Small_chars+Capital_chars;
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
		// Random objGenerator = new Random();
		// int randomNumber = objGenerator.nextInt(25);
		// System.out.println("Random No : " + randomNumber);
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
	
	public void createOnlineButton() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "account.homebutton").click();
			initializer.infoCall("Clicking 'Create Your Online Account' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'Create Online' application is launched successfully");
			// String title = commonFunctions.getElement(driver, "page.title").getText();
			// initializer.successCallwithSnapShot("Navigate to '" + title + "' page
			// successfully");
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);

		}
	}

	public void signUp() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "account.online.signup").click();
			initializer.infoCall("Clicking 'Account signup' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'Account signup' application is launched successfully");
			// String title = commonFunctions.getElement(driver, "page.title").getText();
			// initializer.successCallwithSnapShot("Navigate to '" + title + "' page
			// successfully");
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);

		}

	}

	public void email_ValidationMsg() {
		try {

			commonFunctions.getElement(driver, "account.online.email").sendKeys(getEmail());
			commonFunctions.getElement(driver, "account.online.Verification").click();
			try {
				String email = commonFunctions.getElement(driver, "account.emailvalidation").getText();
				initializer.infoCall("Email validation: " + email);
				Assert.assertEquals(email, emailvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "online.title").getText();
			initializer.successCallwithSnapShot("All '" + title + "' Page Required fields validated successfully ");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "online.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}


//	public void sendVerificationCode() {
//		try {
//			initializer.wait(2);
//			commonFunctions.getElement(driver, "account.signup").click();
//			initializer.infoCall("Clicking 'Account signup' button sucessfully.");
//			initializer.wait(2);
//			initializer.successCallwithSnapShot("'Account signup' application is launched successfully");
//			// String title = commonFunctions.getElement(driver, "page.title").getText();
//			// initializer.successCallwithSnapShot("Navigate to '" + title + "' page
//			// successfully");
//		} catch (Exception e) {
//			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
//
//		}
//
//	}
}
