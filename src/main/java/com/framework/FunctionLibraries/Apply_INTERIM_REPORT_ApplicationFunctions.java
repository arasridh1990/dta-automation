package com.framework.FunctionLibraries;

import java.awt.Robot;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.lang.CharSequence;
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


public class Apply_INTERIM_REPORT_ApplicationFunctions {

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
	@SuppressWarnings("unused")
	private Stage stage = null;
	@SuppressWarnings("unused")
	private DB dB = null;

	public Apply_INTERIM_REPORT_ApplicationFunctions() {
		/*
		 * This condition gets enableSnapshots from Jenkins/pom to enable or disable
		 * screenshots.
		 *
		 * By default enableSnapshots will be true and screenshots will capture
		 * 
		 * @author Kishore B
		 */
		if (System.getProperty("enableSnapshots").equalsIgnoreCase("NO")) {
			enableSnapshots = false;
		}

		/*
		 * This condition is to handle upload pop up for browsers
		 * 
		 * @author Kishore B
		 */
		if (System.getProperty("Browser").equalsIgnoreCase("CHROME")) {
			objTitle = "Open";
		} else if (System.getProperty("Browser").equalsIgnoreCase("FIREFOX")) {
			objTitle = "File Upload";
		} else if (System.getProperty("Browser").equalsIgnoreCase("IE")) {
			objTitle = "Choose File to Upload";
		}
	}
	public Apply_INTERIM_REPORT_ApplicationFunctions(CommonFunctions commonFunctions, Initializer initializer, Stage stage,
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
	 * @author Kishore B
	 */
	public void init(ExtentTest test) {
		this.test = test;
	}

	/**
	 * This method initialize the driver object in Application functions library
	 *
	 * @param driver This is webdriver object
	 * @author Kishore B
	 */
	public void init(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Function Name : launchURL Description : This function will launch URL *
	 *
	 * @param strURL , URL to launch application
	 * @throws SQLException
	 * @author Kishore B
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
			initializer.log("Issue on launching Apply EBT! Application. Exception : " + e.getMessage());
		}
	}

	/**
	 * Function Name : quitBrowser Description : This function Quits the browser
	 * after execution
	 *
	 * @throws Exception , throws an exception if any while closing the browser
	 * @author Kishore B
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
	 * @author Kishore B
	 */
	public void waitUntil(By ele) {
		WebDriverWait oWait = new WebDriverWait(driver, 30);
		oWait.until(ExpectedConditions.presenceOfElementLocated(ele));
	}

	/**
	 * @author Kishore B
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
	 * @author Kishore B
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

	String FNValidation = "Please enter first name";
	String LSValidation = "Please enter last name";
	String DOBValidation = "Please enter a valid date of birth";
	
	String emailvalidation = "Please enter your Email";
	String passwordvalidation = "Please enter a password";
	
	String agreeValidation = "Please agree to the terms";
	String signValidation = "Please sign your Interim Report";
	
	
//	String addressvalidation = "Please enter a valid address";
//	String phonevalidation = "Please enter a valid phone number";
//	String selectoptionvalidation = "Please select an option below";

	
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

	
	public String getMinorYear() {
		Year dob = Year.now().minusYears(10);
		String s = String.valueOf(dob);
		return s;
	}
	
	
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

	public String getAmount() {
		float max = 1000;
		float min = 100;
		Random rn = new Random();
		float i = rn.nextFloat() * (max - min) + min;
		String s = Float.toString(i);
		return s;
	}
	
	public String getWrongZip() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 4, num.length());
		String wrongZip = "9" + number;
		return wrongZip;
	}

	/**
	 * @author Kishore B
	 */
	public String getZipCode() {
		String zipCode = "02111";
		return zipCode;
	}
	/**
	 * @author Kishore B
	 */
    public String getStateCode() {
	String stateCode = "MA";
	return stateCode;
	}
	/**
	 * @author Kishore B
	 */
	public String getStreetName() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 4, num.length());
		String streetName = number + " Street Name";
		return streetName;
	}

	/**
	 * @author Kishore B
	 */
	public String getPhoneNumber() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 10, num.length());
		String phoneNumber = number;
		return phoneNumber;
	}

	/**
	 * @author Kishore B
	 */
	public String getMonth_Frequency() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 5, num.length());
		String SSN = number;
		return SSN;
	}
	public String getSSN() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 9, num.length());
		String SSN = number;
		return SSN;
	}
	public String getEmailDev1() {
		String email = "dccmr_usr4@eotss-dev";
		return email;
	}
	
	public String getEmailDev2() {
		String email = "dccmr_usr1@eotss-dev";
		return email;
	}
	
	public String getEmailDevPvr() {
		String email = "dcpvr@eotss-dev";
		return email;
	}
	public String getEmailDevSpr() {
		String email = "dccmr_usr1@eotss-dev";
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
//	*********************Interim Report  Application! Generic methods************************************************************************************************************

	public void app_Down() {

		try {
			initializer.wait(2);
			String title1 = commonFunctions.getElement(driver, "appdown").getText();
			String title2 = commonFunctions.getElement(driver, "appdownback").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title1 + " " + title2 + "' Page successfully.");
			initializer.wait(2);

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}
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
		
	public void goBack_Option() {

		try {

			commonFunctions.getElement(driver, "generic.goback").click();
			initializer.infoCall("Click 'Go Back' button sucessfully.");
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "recert.title ").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' Page successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}

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
	public void no_Option() {
		try {
			commonFunctions.getElement(driver, "interim.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void loginButton() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "login.homebutton").click();
			initializer.infoCall("Clicking 'Login' button sucessfully.");
			initializer.wait(2);
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
				initializer.wait(2);
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
			commonFunctions.getElement(driver, "login.email").clear();
			commonFunctions.getElement(driver, "login.password").clear();
			String email = getEmailDev1();
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
	
	public void startInterimButton() {
		
		try {
		    initializer.wait(10);
		    commonFunctions.getElement(driver, "interim.start").click();
		    initializer.infoCall("Clicking 'Start Interim Report' button sucessfully.");
		    initializer.wait(2);
		    initializer.successCallwithSnapShot("'Interim Report Application' is launched successfully");
		    String title = commonFunctions.getElement(driver, "page.title").getText();
		    initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
	   } catch (Exception e) {
		String title = commonFunctions.getElement(driver, "interim.title").getText();
		initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
	   }
		
	}
// *************** Contact info **************
	public void interimContact_Yes() {
		try {
			commonFunctions.getElement(driver, "interim.contact.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void interimContactEdit1() {
		try {
			commonFunctions.getElement(driver, "interim.contact.edit1").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void interimhomehousehold_Yes() {
		try {
			commonFunctions.getElement(driver, "interim.homehousehold.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void interimhomehousehold_No() {
		try {
			commonFunctions.getElement(driver, "interim.homehousehold.no ").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void interimContactDone1() {
		try {
			commonFunctions.getElement(driver, "interim.contact.done1").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void interimContactEdit2() {
		try {
			commonFunctions.getElement(driver, "interim.contact.edit2").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void interimPhone() {
		try {
			String phone = getPhoneNumber();
			commonFunctions.getElement(driver, "interim.phone").clear();
			initializer.wait(2);
			commonFunctions.getElement(driver, "interim.phone").sendKeys(phone);
			initializer.infoCall("Enter phone number :" + phone + " successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void interim_message_alert_Yes() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "interim.message.alert.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void interim_message_alert_No() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "interim.message.alert.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void interimContactDone2() {
		try {
			commonFunctions.getElement(driver, "interim.contact.done2").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void interimContactEdit3() {
		try {
			commonFunctions.getElement(driver, "interim.contact.edit3").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void email() {
		try {
			commonFunctions.getElement(driver, "interim.email").clear();
			String email = getEmail();
			commonFunctions.getElement(driver, "interim.email").sendKeys(email);
			initializer.infoCall("Enter client email address as " + email + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void interim_email_notification_Yes() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "interim.emailnotification.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void interim_email_notification_No() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "interim.emailnotification.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void interimContactDone3() {
		try {
			commonFunctions.getElement(driver, "interim.contact.done3").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void interimContact_No() {
		try {
			
			commonFunctions.getElement(driver, "interim.contact.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

// **************Household & Homeless ******************
	public void household_Yes() {
		try {
			commonFunctions.getElement(driver, "interim.household.yes").click(); 
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addhouseholdmember() {
		try {
			commonFunctions.getElement(driver, "interim.addhouseholdmember").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void fnlndob_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String fname = commonFunctions.getElement(driver, "interim.fnvalidation").getText();
				String lname = commonFunctions.getElement(driver, "interim.lnvalidation").getText();
				String dob = commonFunctions.getElement(driver, "interim.dobvalidation").getText();
				initializer.infoCall("First Name validation: " + fname);
				initializer.infoCall("Last Name validation: " + lname);
				initializer.infoCall("Date of Birth validation: " + dob);
				Assert.assertEquals(fname, FNValidation);
				Assert.assertEquals(lname, LSValidation);
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
	
	public void firstLast_Name() {
		try {
			commonFunctions.getElement(driver, "interim.fname").sendKeys("John");
			commonFunctions.getElement(driver, "interim.lname").sendKeys("Carter");
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void middle_Name() {
		try {
			commonFunctions.getElement(driver, "interim.mname").sendKeys("X");
			initializer.infoCall("Enter middle name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void dateOfBirth() {
		try {
			commonFunctions.getElement(driver, "interim.day").sendKeys("7");
			selectDropDown(commonFunctions.getElement(driver, "interim.month"), "July");
			commonFunctions.getElement(driver, "interim.year").sendKeys("1992");
			initializer.infoCall("Enter date of birth under senior group age.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void suffix_Name() {
		try {
			getDropDownValue(commonFunctions.getElement(driver, "interim.household.suffix"));
			initializer.infoCall("Enter suffix successfully.");
//			selectDropDown(commonFunctions.getElement(driver, "household.suffix"),
//					(commonFunctions.getTestData(stage.getTestName(), "Suffix")));
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void noSSN_Option() {
		try {
			commonFunctions.getElement(driver, "interim.nossn").click();
			initializer.infoCall("Click 'I don't have one' button sucessfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void sSN_Option() {
		try {
			commonFunctions.getElement(driver, "interim.ssn").clear();
			String SSN = getSSN();
			commonFunctions.getElement(driver, "household.ssn").sendKeys(SSN);
			initializer.infoCall("Enter SSN number :" + SSN + " successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void spouse_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.spouse").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Spouse' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void mother_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.mother").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Mother' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void father_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.father").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Father' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void daughter_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.daughter").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Daughter' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void son_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.son").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Son' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void brother_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.brother").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Brother' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void sister_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.sister").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Sister' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void grandmother_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.grandmother").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Grandmother' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void grandfather_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.grandfather").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Grandfather' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void granddaughter_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.granddaughter").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Granddaughter' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void grandson_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.grandson").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Grandson' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void other_AboutMember() {
		try {
			commonFunctions.getElement(driver, "interim.other").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Other' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void male_Option() {
		try {
			commonFunctions.getElement(driver, "interim.male").click();
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
			commonFunctions.getElement(driver, "interim.female").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select gender as 'Female' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void household_No() {
		try {
			
			commonFunctions.getElement(driver, "interim.household.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void uscitizen_Yes() {
		try {
			commonFunctions.getElement(driver, "interim.uscitizen.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void uscitizen_No() {
		try {
			
			commonFunctions.getElement(driver, "interim.uscitizen.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void food_Yes() {
		try {
			commonFunctions.getElement(driver, "interim.food.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as food 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void food_No() {
		try {
			
			commonFunctions.getElement(driver, "interim.food.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void income_Yes() {
		try {
			commonFunctions.getElement(driver, "interim.income.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as income 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void income_No() {
		try {
			
			commonFunctions.getElement(driver, "interim.income.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

// *****************************************************
	
	
	public void earned_Yes() {
		try {
			commonFunctions.getElement(driver, "interim.earning.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void interimEarnedEdit() {
		try {
			commonFunctions.getElement(driver, "interim.earned.edit").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		}
	
	public void wages() {
		try {
			commonFunctions.getElement(driver, "interim.wages").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Wages' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}	
	
	public void self_Employment() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Self-Employment' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void workStudy() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Work Study' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addanotherwages() {
		try {
			commonFunctions.getElement(driver, "interim.addanotherwage").click();
			initializer.wait(2);
			initializer.infoCall(" Add another wages clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount1_Wages_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.wages.employer1").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.wages.frequency1").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.wages.amount1").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount1_Wages_Quarterly() {
		try {
			commonFunctions.getElement(driver, "interim.wages.employer1").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.wages.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "interim.wages.frequency1").sendKeys("Quarterly");
			commonFunctions.getElement(driver, "interim.wages.amount1").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount1_Wages_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.wages.employer1").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.wages.frequency1").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.wages.amount1").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_Wages_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.wages.employer2").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.wages.frequency2").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.wages.amount2").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_Wages_Quarterly() {
		try {
			commonFunctions.getElement(driver, "interim.wages.employer2").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.wages.frequency2").sendKeys("Quarterly");
			commonFunctions.getElement(driver, "interim.wages.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_Wages_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.wages.employer2").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.wages.frequency2").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.wages.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount3_Wages_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.wages.employer2").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.wages.frequency3").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.wages.amount3").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount3_Wages_Quarterly() {
		try {
			commonFunctions.getElement(driver, "interim.wages.employer3").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.wages.frequency3").sendKeys("Quarterly");
			commonFunctions.getElement(driver, "interim.wages.amount3").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount3_Wages_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.wages.employer3").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.wages.frequency3").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.wages.amount3").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_Wages_Amount1() {
		try {
			commonFunctions.getElement(driver, "interim.wages.remove1").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_Wages_Amount2() {
		try {
			commonFunctions.getElement(driver, "interim.wages.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_Wages_Amount3() {
		try {
			commonFunctions.getElement(driver, "interim.wages.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_Wages_Amount4() {
		try {
			commonFunctions.getElement(driver, "interim.wages.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
//	************** add self employment
	
	public void addanother_Selfemployment() {
		try {
			commonFunctions.getElement(driver, "interim.addanotherselfemp").click();
			initializer.wait(2);
			initializer.infoCall(" Add another self-employment clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void two_Amount1_Selfemp_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.employer1").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.selfemp.frequency1").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.selfemp.amount1").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount1_Selfemp_Semiannual() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.employer1").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.selfemp.frequency1").sendKeys("Semiannual (twice a year)");
			commonFunctions.getElement(driver, "interim.selfemp.amount1").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Semiannual (twice a year) enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void two_Amount1_Selfemp_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.employer1").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.selfemp.frequency1").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.selfemp.amount1").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void two_Amount2_Selfemp_Bimonthly() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.employer2").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.selfemp.frequency2").sendKeys("Bimonthly (every two months)");
			commonFunctions.getElement(driver, "interim.selfemp.amount2").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount  Bimonthly (every two months) enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount2_Selfemp_Annual() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.employer2").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.selfemp.frequency2").sendKeys("Annual");
			commonFunctions.getElement(driver, "interim.selfemp.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount2_Selfemp_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.employer2").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.selfemp.frequency2").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.selfemp.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount3_Selfemp_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.employer3").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.selfemp.frequency3").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.selfemp.amount3").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount3_Selfemp_Quarterly() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.employer3").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.selfemp.frequency3").sendKeys("Quarterly");
			commonFunctions.getElement(driver, "interim.selfemp.amount3").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount3_Selfemp_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.employer3").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.selfemp.frequency3").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.selfemp.amount3").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_selfemp_Amount1() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.remove1").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_selfemp_Amount2() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_selfemp_Amount3() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_selfemp_Amount4() {
		try {
			commonFunctions.getElement(driver, "interim.selfemp.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
//******************************************* work study
	
	public void addanother_Workstudy() {
		try {
			commonFunctions.getElement(driver, "interim.addanotherworkstudy").click();
			initializer.wait(2);
			initializer.infoCall(" Add another work study clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void three_Amount1_Workstudy_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.employer1").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.workstudy.frequency1").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.workstudy.amount1").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount1_Workstudy_Semiannual() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.employer1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "interim.workstudy.frequency1").sendKeys("Semiannual (twice a year)");
			commonFunctions.getElement(driver, "interim.workstudy.amount1").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Semiannual (twice a year) enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void three_Amount1_Workstudy_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.employer1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "interim.workstudy.frequency1").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.workstudy.amount1").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void three_Amount2_Workstudy_Bimonthly() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.employer2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "interim.workstudy.frequency2").sendKeys("Bimonthly (every two months)");
			commonFunctions.getElement(driver, "interim.workstudy.amount2").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount  Bimonthly (every two months) enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount2_Workstudy_Annual() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.employer2").sendKeys(getAmount());
			commonFunctions.getElement(driver, "interim.workstudy.frequency2").sendKeys("Annual");
			commonFunctions.getElement(driver, "interim.workstudy.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount2_Workstudy_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.employer2").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.workstudy.frequency2").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.workstudy.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount3_Workstudy_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.employer3").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.workstudy.frequency3").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.workstudy.amount3").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount3_Workstudy_Quarterly() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.employer3").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.workstudy.frequency3").sendKeys("Quarterly");
			commonFunctions.getElement(driver, "interim.workstudy.amount3").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount3_Workstudy_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.employer3").sendKeys(getName());
			commonFunctions.getElement(driver, "interim.workstudy.frequency3").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.workstudy.amount3").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_workstudy_Amount1() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.remove1").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_workstudy_Amount2() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_workstudy_Amount3() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_workstudy_Amount4() {
		try {
			commonFunctions.getElement(driver, "interim.workstudy.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void earning_No() {
		try {
			commonFunctions.getElement(driver, "interim.earning.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
//*********************************************
	public void unearned_Yes() {
		try {
			commonFunctions.getElement(driver, "interim.unearned.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addincome() {
		try {
			commonFunctions.getElement(driver, "interim.addincome").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "interim.addincome").getText();
			initializer.successCallwithSnapShot("clicked '" + title + "' button successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addnameunearned() {
		try {
			commonFunctions.getElement(driver, "interim.nameunearned").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void unemployment_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.unemp").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select income and benefits type as 'Unemployment' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void pension_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.pension").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select income and benefits type as 'Pension' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void veteransBenefits_IncomeAndBenefits() {
		try {
			commonFunctions.getElement(driver, "unearned.vbenefits").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Select income and benefits type as 'Veterans Benefits' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
//	*******unemployment
	public void addanotherunemployment() {
		try {
			commonFunctions.getElement(driver, "interim.addanotherunemployment").click();
			initializer.wait(2);
			initializer.infoCall(" Add another unemployment clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount1_Unemplyment_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.unemp.frequency1").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.unemp.amount1").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_Unemployment_Annual() {
		try {
			commonFunctions.getElement(driver, "interim.unemp.frequency1").sendKeys("Annual");
			commonFunctions.getElement(driver, "interim.unemp.amount1").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount Annual enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount2_Unemployment_Quarterly() {
		try {
			commonFunctions.getElement(driver, "interim.unemp.frequency2").sendKeys("Quarterly");
			commonFunctions.getElement(driver, "interim.unemp.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_Unemployment_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.unemp.frequency2").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.unemp.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
//	******** pension
	public void addanotherpension() {
		try {
			commonFunctions.getElement(driver, "interim.addanotherpension").click();
			initializer.wait(2);
			initializer.infoCall(" Add another wages clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_pension_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.pension.frequency1").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.pension.amount1").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_pension_Annual() {
		try {
			commonFunctions.getElement(driver, "interim.pension.frequency1").sendKeys("Annual");
			commonFunctions.getElement(driver, "interim.pension.amount1").sendKeys("456.33");
			initializer.successCallwithSnapShot("Amount Annual enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount2_pension_Quarterly() {
		try {
			commonFunctions.getElement(driver, "interim.pension.frequency2").sendKeys("Quarterly");
			commonFunctions.getElement(driver, "interim.pension.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_pension_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.pension.frequency2").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.pension.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
//	********veteran's benefit
	
	public void addanotherveteranbenefit() {
		try {
			commonFunctions.getElement(driver, "interim.addanotherveteran").click();
			initializer.wait(2);
			initializer.infoCall(" Add another veteran's benifits clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_veteranbenefit_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.veteran.frequency1").sendKeys("Monthly");
			commonFunctions.getElement(driver, "interim.veteran.amount1").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_veteranbenefit_Annual() {
		try {
			commonFunctions.getElement(driver, "interim.veteran.frequency1").sendKeys("Annual");
			commonFunctions.getElement(driver, "interim.veteran.amount1").sendKeys(getAmount());
			initializer.successCallwithSnapShot("Amount Annual enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount2_veteranbenefit_Quarterly() {
		try {
			commonFunctions.getElement(driver, "interim.veteran.frequency2").sendKeys("Quarterly");
			commonFunctions.getElement(driver, "interim.veteran.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_veteranbenefit_Weekly() {
		try {
			commonFunctions.getElement(driver, "interim.veteran.frequency2").sendKeys("Weekly");
			commonFunctions.getElement(driver, "interim.unveteran.amount2").sendKeys(getAmount());
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void unearned_No() {
		try {
			
			commonFunctions.getElement(driver, "interim.unearned.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void childSupport_Yes() {
		try {
			commonFunctions.getElement(driver, "interim.childSupport.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addchildsuppot() {
		try {
			commonFunctions.getElement(driver, "interim.childsupport").click();
			initializer.wait(2);
			initializer.infoCall(" Add another household member's child support clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addnamechildsupport() {
		try {
			commonFunctions.getElement(driver, "interim.namechildsupport").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "interim.namechildsupport").getText();
			initializer.successCallwithSnapShot("clicked '" + title + "' button successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void interimchildsupportEdit() {
		try {
			commonFunctions.getElement(driver, "interim.child.edit").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		}
	
	public void addanotherchild() {
		try {
			commonFunctions.getElement(driver, "interim.addanotherchild").click();
			initializer.wait(2);
			initializer.infoCall(" Add another child clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount1_child_Monthly() {
		try {
			commonFunctions.getElement(driver, "interim.child.amount1").sendKeys(getAmount());
			commonFunctions.getElement(driver, "interim.child.frequency1").sendKeys("Monthly");
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void interimchilddone() {
		try {
			commonFunctions.getElement(driver, "interim.child.done").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void childSupport_No() {
		try {
			
			commonFunctions.getElement(driver, "interim.childSupport.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	
	public void signSubmitValidationMsg() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed " + title + " page successfully");
			initializer.wait(2);
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(2);
			try {

				String agree = commonFunctions.getElement(driver, "interim.agree.validation").getText();
				initializer.successCallwithSnapShot("Agree check box - field validation: " + agree);
				Assert.assertEquals(agree, agreeValidation);

				String sign = commonFunctions.getElement(driver, "interim.sign.validation").getText();
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
	
	public void signSubmit() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed " + title + " page successfully");
			commonFunctions.getElement(driver, "interim.sign.agree").click();
			commonFunctions.getElement(driver, "interim.sign.sign").sendKeys(getName());
			initializer.wait(3);
			initializer.successCallwithSnapShot("Client Agree and Signed successfully ");
//			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(10);
			initializer.successCallwithSnapShot("Client clicked submit Signed successfully ");
			initializer.wait(15);
			String myNumber = commonFunctions.getElement(driver, "interimapplication.number").getText();
//			deleteRecord_DB(myNumber.split(":")[1].trim());
			commonFunctions.getElement(driver, "interimreport.download").click();
			initializer.wait(5);
			initializer.successCallwithSnapShot(myNumber + " and it was downloaded successfully!");
			initializer.successCallwithSnapShot(
					"Datasheet available at \\DTAConnect\\src\\test\\resources\\Results\\Downloads");
			initializer.wait(2);

		} catch (Exception e) {

			initializer.failureCallwithException("Exception occured in 'Sign & submit---Sign & Submit' Page", e);

		}
	}
}
