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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class Apply_ProviderLOGIN_ApplicationFunctions {
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

	public Apply_ProviderLOGIN_ApplicationFunctions() {
		/*
		 * This condition gets enableSnapshots from Jenkins/pom to enable or disable
		 * screenshots.
		 *
		 * By default enableSnapshots will be true and screenshots will capture
		 * 
		 * @author sushmitha
		 */
		if (System.getProperty("enableSnapshots").equalsIgnoreCase("NO")) {
			enableSnapshots = false;
		}

		/*
		 * This condition is to handle upload pop up for browsers
		 * 
		 * @author sushmitha
		 */
		if (System.getProperty("Browser").equalsIgnoreCase("CHROME")) {
			objTitle = "Open";
		} else if (System.getProperty("Browser").equalsIgnoreCase("FIREFOX")) {
			objTitle = "File Upload";
		} else if (System.getProperty("Browser").equalsIgnoreCase("IE")) {
			objTitle = "Choose File to Upload";
		}
	}

	public Apply_ProviderLOGIN_ApplicationFunctions(CommonFunctions commonFunctions, Initializer initializer, Stage stage,
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
	 * @author sushmitha
	 */
	public void init(ExtentTest test) {
		this.test = test;
	}

	/**
	 * This method initialize the driver object in Application functions library
	 *
	 * @param driver This is webdriver object
	 * @author sushmitha
	 */
	public void init(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Function Name : launchURL Description : This function will launch URL *
	 *
	 * @param strURL , URL to launch application
	 * @throws SQLException
	 * @author sushmitha
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
	 * @author sushmitha
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
	 * @author sushmitha
	 */
	public void waitUntil(By ele) {
		WebDriverWait oWait = new WebDriverWait(driver, 30);
		oWait.until(ExpectedConditions.presenceOfElementLocated(ele));
	}

	/**
	 * @author sushmitha
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
	 * @author sushmitha
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
	String emailvalidation = "Please enter a valid Email";
	String zipvalidation_tafdc = "This TAFDC application is for Massachussetts residents. If you live in Massachussetts, please enter your Zip Code. If you do not live in Massachussetts, you must apply in your state.";
	String zipvalidation_eaedc = "This EAEDC application is for Massachussetts residents. If you live in Massachussetts, please enter your Zip Code. If you do not live in Massachussetts, you must apply in your state.";
	String householdvalidation = "Please choose no. of household";
	String agreeValidation = "Please agree to the terms";
	String signValidation = "Please sign your application";
	String passwordvalidation = "Please enter a password";
			
	

	/**
	 * @author sushmitha
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
	 * @author sushmitha
	 */
	public String getMinorYear() {
		Year dob = Year.now().minusYears(10);
		String s = String.valueOf(dob);
		return s;
	}

	/**
	 * @author sushmitha
	 */
	public String getMajorYear() {
		Year dob = Year.now().minusYears(30);
		String s = String.valueOf(dob);
		return s;
	}

	/**
	 * @author sushmitha
	 */
	public String getSeniorYear() {
		Year dob = Year.now().minusYears(65);
		String s = String.valueOf(dob);
		return s;
	}

	/**
	 * @author sushmitha
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
	 * @author sushmitha
	 */
	public String getDay() {
		int max = 28;
		int min = 1;
		Random rn = new Random();
		int i = rn.nextInt(max - min) + min;
		System.out.println("Random No : " + i);
		String s = Integer.toString(i);
		return s;
	}

	/**
	 * @author sushmitha
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
	 * @author sushmitha
	 */
	public String getWrongZip() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 4, num.length());
		String wrongZip = "9" + number;
		return wrongZip;
	}

	/**
	 * @author sushmitha
	 */
	public String getZipCode() {
		String zipCode = "02111";
		return zipCode;
	}

	/**
	 * @author sushmitha
	 */
	public String getStreetName() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 4, num.length());
		String streetName = number + " Street Name";
		return streetName;
	}

	public String getPhoneNumber() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 10, num.length());
		String phoneNumber = number;
		return phoneNumber;
	}

	/**
	 * @author sushmitha
	 */
	public String getSSN() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 9, num.length());
		String SSN = number;
		return SSN;
	}

	/**
	 * @author sushmitha
	 */
	public String getAPID() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 7, num.length());
		String APID = number;
		return APID;
	}

	/**
	 * @author sushmitha
	 */
	public String getWrongSSN() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 5, num.length());
		String SSN = number;
		return SSN;
	}

	/**
	 * @author sushmitha
	 */
	public String getMonth_Frequency() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 5, num.length());
		String SSN = number;
		return SSN;
	}

//	*********************Apply CASH Applications! Generic methods************************************************************************************************************

	public String getEmailDevPvr1() {
		String email = "consupusr1@eotss.odc";
		return email;
	}
	
	public String getEmailDevPvr2() {
		String email = "consupusr1@eotss.odc";
		return email;
	}

	public String getPassword() {
		String password = "Test@123";
		return password;
	}
	public String getEmail() {
		String email = "Susenjohn@gmail.com";
		return email;
	}
	
	public String getWrongEmail() {
		String email = "username_@test.com";
		return email;
	}
	public String getWrongPassword() {
		String password = "test@1234";
		return password;
	}
	
	public String getAgencyId() {
		String id = "6454796";
		return id;
	}
	public String getSSNno() {
		String id = "202011101";
		return id;
	}
	public String getWebAppNo() {
		String id = "11529644";
		return id;
	}
	
	public String getEbtcardNo() {
		String id = "34343435";
		return id;
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
	
	public void emailPassword_ValidationMsg() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "generic.continue").click();
//			commonFunctions.getElement(driver, "login.email").sendKeys(getWrongEmail());
//			commonFunctions.getElement(driver, "login.password").sendKeys(getWrongPassword());
			try {
				String email = commonFunctions.getElement(driver, "login.emailvalidation").getText();
				String password  = commonFunctions.getElement(driver, "login.passwordvalidation").getText();
				initializer.infoCall("Email validation: " + email);
				initializer.infoCall("Password Validation: " + password);
				Assert.assertEquals(email, emailvalidation);
				Assert.assertEquals(password, passwordvalidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.successCallwithSnapShot("All '" + title + "' Page Required fields validated successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void emailPassword() {
		try {
			initializer.infoCall("emailPassword Function: ");
			commonFunctions.getElement(driver, "login.email").clear();
			commonFunctions.getElement(driver, "login.password").clear();
			String email = getEmailDevPvr1();
			String password = getPassword();
			initializer.wait(1);
			commonFunctions.getElement(driver, "login.email").sendKeys(email);
			initializer.wait(2);
			commonFunctions.getElement(driver, "login.password").sendKeys(password);
			initializer.wait(2);
			initializer.successCallwithSnapShot("Enter client email address as " + email + "and password as " + password + " successfully.");			
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void chooseAgencyId() {
		try{
			initializer.wait(3);
			commonFunctions.getElement(driver, "provider.agencyIdRadio").click();
		
			try{
				
				initializer.infoCall("AgencyId Function");
				String agencyId = getAgencyId();
				initializer.wait(1);
				commonFunctions.getElement(driver, "provider.agencyId").sendKeys(agencyId);
				initializer.wait(2);
				commonFunctions.getElement(driver, "provider.search").click();
				initializer.successCallwithSnapShot("Enter client agencyId " + agencyId+" successfully.");	
				initializer.wait(5);

			} catch (Exception e) {
				String title = commonFunctions.getElement(driver, "login.title").getText();
				initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	
	public void chooseSSN() {
		try{
			initializer.wait(3);
			commonFunctions.getElement(driver, "provider.ssnRadio").click();
		
			try{
				
				initializer.infoCall("ssn Function");
				String ssn = getSSNno();
				initializer.wait(1);
				commonFunctions.getElement(driver, "provider.ssn").sendKeys(ssn);
				initializer.wait(2);
				commonFunctions.getElement(driver, "provider.search").click();
				initializer.successCallwithSnapShot("Enter client ssn " + ssn+" successfully.");	
				initializer.wait(5);

			} catch (Exception e) {
				String title = commonFunctions.getElement(driver, "login.title").getText();
				initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	
	public void chooseWebApplication() {
		try{
			initializer.wait(5);
			commonFunctions.getElement(driver, "provider.webAppNumRadio").click();
		
			try{
				
				initializer.infoCall("WebApplication Function");
				String webAppno = getWebAppNo();
				initializer.wait(1);
				commonFunctions.getElement(driver, "provider.webAppno").sendKeys(webAppno);
				initializer.wait(2);
				commonFunctions.getElement(driver, "provider.search").click();
				initializer.successCallwithSnapShot("Enter client web app number " + webAppno+" successfully.");	
				initializer.wait(5);

			} catch (Exception e) {
				String title = commonFunctions.getElement(driver, "login.title").getText();
				initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	
	public void chooseEBTcard() {
		try{
			initializer.wait(3);
			commonFunctions.getElement(driver, "provider.ebtRadio").click();
		
			try{
				
				initializer.infoCall("WebApplication Function");
				String ebt = getEbtcardNo();
				initializer.wait(1);
				commonFunctions.getElement(driver, "provider.ebt").sendKeys(ebt);
				initializer.wait(2);
				commonFunctions.getElement(driver, "provider.search").click();
				initializer.successCallwithSnapShot("Enter client ebt number " + ebt+" successfully.");	
				initializer.wait(5);

			} catch (Exception e) {
				String title = commonFunctions.getElement(driver, "login.title").getText();
				initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void superProviderEmailPassword() {
		try {
			initializer.infoCall("emailPassword Function: ");
			commonFunctions.getElement(driver, "login.email").clear();
			commonFunctions.getElement(driver, "login.password").clear();
			String email = getEmailDevPvr2();
			String password = getPassword();
			initializer.wait(1);
			commonFunctions.getElement(driver, "login.email").sendKeys(email);
			initializer.wait(2);
			commonFunctions.getElement(driver, "login.password").sendKeys(password);
			initializer.wait(2);
			initializer.successCallwithSnapShot("Enter client email address as " + email + "and password as " + password + " successfully.");			
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void providerLogout() {
		try{
			initializer.wait(2);
			commonFunctions.getElement(driver, "provider.name").click();
		
			try{
				initializer.infoCall("logout Function");
				initializer.wait(2);
				commonFunctions.getElement(driver, "provider.logoutButton").click();
				initializer.successCallwithSnapShot("Logout successfully.");	
				
			} catch (Exception e) {
				String title = commonFunctions.getElement(driver, "login.title").getText();
				initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void providerLogout1() {
		try{
			initializer.wait(2);
			commonFunctions.getElement(driver, "provider.name1").click();
		
			try{
				initializer.infoCall("logout Function");
				initializer.wait(2);
				commonFunctions.getElement(driver, "provider.logoutButton").click();
				initializer.successCallwithSnapShot("Logout successfully.");	
				
			} catch (Exception e) {
				String title = commonFunctions.getElement(driver, "login.title").getText();
				initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
}
