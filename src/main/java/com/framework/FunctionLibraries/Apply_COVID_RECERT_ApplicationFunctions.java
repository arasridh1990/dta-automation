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


public class Apply_COVID_RECERT_ApplicationFunctions {

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

	public Apply_COVID_RECERT_ApplicationFunctions() {
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
	public Apply_COVID_RECERT_ApplicationFunctions(CommonFunctions commonFunctions, Initializer initializer, Stage stage,
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
	
	String emailvalidation = "Please enter a valid Email";
	String passwordvalidation = "Please enter a password";
	
	String agreeValidation = "Please agree to the terms";
	String signValidation = "Please sign your COVID Recertification";
	
	
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
		String email = "conconsumer1@eotss.odc";
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
//	*********************covidrecert Report  Application! Generic methods************************************************************************************************************

	public String getcovidrecertData(String param) {
		return commonFunctions.getTestData(stage.getTestName(), param, "file.covidrecertDataFilePath");
	}
	
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
			commonFunctions.getElement(driver, "covidrecert.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
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
			
				String email = commonFunctions.getElement(driver, "login.emailvalidation").getText();
				String password  = commonFunctions.getElement(driver, "login.passwordvalidation").getText();
				initializer.wait(2);
				initializer.infoCall("Email validation: " + email);
				initializer.infoCall("Password Validation: " + password);
				Assert.assertEquals(email, emailvalidation);
				Assert.assertEquals(password, passwordvalidation);
			
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
	
	public void startcovidrecertButton() {
		
		try {
		    initializer.wait(10);
		    commonFunctions.getElement(driver, "covidrecert.start").click();
		    initializer.infoCall("Clicking 'Start covidrecert Report' button sucessfully.");
		    initializer.wait(2);
		    initializer.successCallwithSnapShot("'covidrecert Report Application' is launched successfully");
		    String title = commonFunctions.getElement(driver, "page.title").getText();
		    initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
	   } catch (Exception e) {
		String title = commonFunctions.getElement(driver, "covidrecert.title").getText();
		initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
	   }
		
	}
// *************** Contact info **************
	public void covidrecertContact_Yes() {
		try {
			commonFunctions.getElement(driver, "covidrecert.contact.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void covidrecertContactEdit1() {
		try {
			commonFunctions.getElement(driver, "covidrecert.contact.edit1").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void covidrecerthomehousehold_Yes() {
		try {
			commonFunctions.getElement(driver, "covidrecert.homehousehold.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void covidrecerthomehousehold_No() {
		try {
			commonFunctions.getElement(driver, "covidrecert.homehousehold.no ").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void covidrecertContactDone1() {
		try {
			commonFunctions.getElement(driver, "covidrecert.contact.done1").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void covidrecertContactEdit2() {
		try {
			commonFunctions.getElement(driver, "covidrecert.contact.edit2").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	
	public void covidrecertPhone() {
		try {
			String phone = getPhoneNumber();
			commonFunctions.getElement(driver, "covidrecert.phone").clear();
			initializer.wait(2);
			commonFunctions.getElement(driver, "covidrecert.phone").sendKeys(getcovidrecertData("Phone"));
			initializer.infoCall("Enter phone number :" + phone + " successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void covidrecert_message_alert_Yes() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "covidrecert.message.alert.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void covidrecert_message_alert_No() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "covidrecert.message.alert.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void covidrecertContactDone2() {
		try {
			commonFunctions.getElement(driver, "covidrecert.contact.done2").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void covidrecertContactEdit3() {
		try {
			commonFunctions.getElement(driver, "covidrecert.contact.edit3").click();
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
			commonFunctions.getElement(driver, "covidrecert.email").clear();
			commonFunctions.getElement(driver, "covidrecert.email").sendKeys("2010$#@%");
			initializer.wait(2);
			initializer.infoCall("Please enter a valid email address ");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void covidrecert_email_notification_Yes() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "covidrecert.emailnotification.yes").click();
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.infoCall("Please enter a valid email address ");
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void email1() {
		try {
			commonFunctions.getElement(driver, "covidrecert.email").clear();
			String email = getEmail();
			commonFunctions.getElement(driver, "covidrecert.email").sendKeys(email);
			initializer.infoCall("Enter client email address as " + email + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void covidrecert_email_notification_No() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "covidrecert.emailnotification.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void covidrecertContactDone3() {
		try {
			commonFunctions.getElement(driver, "covidrecert.contact.done3").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void covidrecertContact_No() {
		try {
			
			commonFunctions.getElement(driver, "covidrecert.contact.no").click();
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
			commonFunctions.getElement(driver, "covidrecert.household.yes").click(); 
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addhouseholdmember() {
		try {
			commonFunctions.getElement(driver, "covidrecert.addhouseholdmember").click();
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
				String fname = commonFunctions.getElement(driver, "covidrecert.fnvalidation").getText();
				String lname = commonFunctions.getElement(driver, "covidrecert.lnvalidation").getText();
				String dob = commonFunctions.getElement(driver, "covidrecert.dobvalidation").getText();
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
			commonFunctions.getElement(driver, "covidrecert.fname").sendKeys(getcovidrecertData("FName"));
			commonFunctions.getElement(driver, "covidrecert.lname").sendKeys(getcovidrecertData("LName"));
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void middle_Name() {
		try {
			commonFunctions.getElement(driver, "covidrecert.mname").sendKeys(getcovidrecertData("MName"));
			initializer.infoCall("Enter middle name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void dateOfBirth() {
		try {
			commonFunctions.getElement(driver, "covidrecert.day").sendKeys(getcovidrecertData("Day"));
			selectDropDown(commonFunctions.getElement(driver, "covidrecert.month"), getcovidrecertData("Month"));
			commonFunctions.getElement(driver, "covidrecert.year").sendKeys(getcovidrecertData("Year"));
			initializer.infoCall("Enter date of birth under senior group age.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void suffix_Name() {
		try {
			getDropDownValue(commonFunctions.getElement(driver, "covidrecert.household.suffix"));
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
			commonFunctions.getElement(driver, "covidrecert.nossn").click();
			initializer.infoCall("Click 'I don't have one' button sucessfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void sSN_Option() {
		try {
			commonFunctions.getElement(driver, "covidrecert.ssn").clear();
			String SSN = getSSN();
			commonFunctions.getElement(driver, "household.ssn").sendKeys(getcovidrecertData("SSN"));
			initializer.infoCall("Enter SSN number :" + SSN + " successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void spouse_AboutMember() {
		try {
			commonFunctions.getElement(driver, "covidrecert.spouse").click();
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
			commonFunctions.getElement(driver, "covidrecert.mother").click();
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
			commonFunctions.getElement(driver, "covidrecert.father").click();
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
			commonFunctions.getElement(driver, "covidrecert.daughter").click();
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
			commonFunctions.getElement(driver, "covidrecert.son").click();
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
			commonFunctions.getElement(driver, "covidrecert.brother").click();
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
			commonFunctions.getElement(driver, "covidrecert.sister").click();
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
			commonFunctions.getElement(driver, "covidrecert.grandmother").click();
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
			commonFunctions.getElement(driver, "covidrecert.grandfather").click();
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
			commonFunctions.getElement(driver, "covidrecert.granddaughter").click();
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
			commonFunctions.getElement(driver, "covidrecert.grandson").click();
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
			commonFunctions.getElement(driver, "covidrecert.other").click();
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
			commonFunctions.getElement(driver, "covidrecert.male").click();
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
			commonFunctions.getElement(driver, "covidrecert.female").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select gender as 'Female' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void household_No() {
		try {
			
			commonFunctions.getElement(driver, "covidrecert.household.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void uscitizen_Yes() {
		try {
			commonFunctions.getElement(driver, "covidrecert.uscitizen.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void uscitizen_No() {
		try {
			
			commonFunctions.getElement(driver, "covidrecert.uscitizen.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void food_Yes() {
		try {
			commonFunctions.getElement(driver, "covidrecert.food.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as food 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void food_No() {
		try {
			
			commonFunctions.getElement(driver, "covidrecert.food.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void income_Yes() {
		try {
			commonFunctions.getElement(driver, "covidrecert.income.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as income 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void income_No() {
		try {
			
			commonFunctions.getElement(driver, "covidrecert.income.no").click();
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
			commonFunctions.getElement(driver, "covidrecert.earning.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void covidrecertEarnedEdit() {
		try {
			commonFunctions.getElement(driver, "covidrecert.earned.edit").click();
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
			commonFunctions.getElement(driver, "covidrecert.wages").click();
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
			commonFunctions.getElement(driver, "covidrecert.selfemp").click();
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
			commonFunctions.getElement(driver, "covidrecert.workstudy").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select income and benefits type as 'Work Study' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void americanojt() {
		try {
			commonFunctions.getElement(driver, "covidrecert.americanojt").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"De-Select income and benefits type as 'American OJT' successfully in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addanotherwages() {
		try {
			commonFunctions.getElement(driver, "covidrecert.addanotherwage").click();
			initializer.wait(2);
			initializer.infoCall(" Add another wages clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount1_Wages_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.employer1").sendKeys(getcovidrecertData("Wagesmonthlyname1"));
			commonFunctions.getElement(driver, "covidrecert.wages.frequency1").sendKeys(getcovidrecertData("Wagesmonthlyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.wages.amount1").sendKeys(getcovidrecertData("Wagesmonthlyamount1"));
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount1_Wages_Quarterly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.employer1").sendKeys(getcovidrecertData("Wagesquarterlyname1"));
			commonFunctions.getElement(driver, "covidrecert.wages.frequency1").sendKeys(getcovidrecertData("Wagesquarterlyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.wages.amount1").sendKeys(getcovidrecertData("Wagesquarterlyamount1"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount1_Wages_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.employer1").sendKeys(getcovidrecertData("Wagesweeklyname1"));
			commonFunctions.getElement(driver, "covidrecert.wages.frequency1").sendKeys(getcovidrecertData("Wagesweeklyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.wages.amount1").sendKeys(getcovidrecertData("Wagesweeklyamount1"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_Wages_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.employer2").sendKeys(getcovidrecertData("Wagesmonthlyname2"));
			commonFunctions.getElement(driver, "covidrecert.wages.frequency2").sendKeys(getcovidrecertData("Wagesmonthlyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.wages.amount2").sendKeys(getcovidrecertData("Wagesmonthlyamount2"));
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_Wages_Quarterly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.employer2").sendKeys(getcovidrecertData("Wagesquarterlyname2"));
			commonFunctions.getElement(driver, "covidrecert.wages.frequency2").sendKeys(getcovidrecertData("Wagesquarterlyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.wages.amount2").sendKeys(getcovidrecertData("Wagesquarterlyamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount per quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_Wages_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.employer2").sendKeys(getcovidrecertData("Wagesweeklyname2"));
			commonFunctions.getElement(driver, "covidrecert.wages.frequency2").sendKeys(getcovidrecertData("Wagesweeklyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.wages.amount2").sendKeys(getcovidrecertData("Wagesweeklyamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount3_Wages_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.employer2").sendKeys(getcovidrecertData("Wagesmonthlyname3"));
			commonFunctions.getElement(driver, "covidrecert.wages.frequency3").sendKeys(getcovidrecertData("Wagesmonthlyfrequency3"));
			commonFunctions.getElement(driver, "covidrecert.wages.amount3").sendKeys(getcovidrecertData("Wagesmonthlyamount3"));
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount3_Wages_Quarterly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.employer3").sendKeys(getcovidrecertData("Wagesquarterlyname3"));
			commonFunctions.getElement(driver, "covidrecert.wages.frequency3").sendKeys(getcovidrecertData("Wagesquarterlyfrequency3"));
			commonFunctions.getElement(driver, "covidrecert.wages.amount3").sendKeys(getcovidrecertData("Wagesquarterlyamount3"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount3_Wages_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.employer3").sendKeys(getcovidrecertData("Wagesweeklyname3"));
			commonFunctions.getElement(driver, "covidrecert.wages.frequency3").sendKeys(getcovidrecertData("Wagesweeklyfrequency3"));
			commonFunctions.getElement(driver, "covidrecert.wages.amount3").sendKeys(getcovidrecertData("Wagesweeklyamount3"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_Wages_Amount1() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.remove1").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_Wages_Amount2() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_Wages_Amount3() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_Wages_Amount4() {
		try {
			commonFunctions.getElement(driver, "covidrecert.wages.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
//	************** add self employment
	
	public void addanother_Selfemployment() {
		try {
			commonFunctions.getElement(driver, "covidrecert.addanotherselfemp").click();
			initializer.wait(2);
			initializer.infoCall(" Add another self-employment clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void two_Amount1_Selfemp_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.employer1").sendKeys(getcovidrecertData("Selfempmonthlyname1"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.frequency1").sendKeys(getcovidrecertData("Selfempmonthlyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.amount1").sendKeys(getcovidrecertData("Selfempmonthlyamount1"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount1_Selfemp_Semiannual() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.employer1").sendKeys(getcovidrecertData("Selfempsemiannualname1"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.frequency1").sendKeys(getcovidrecertData("Selfempsemiannualfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.amount1").sendKeys(getcovidrecertData("Selfempsemiannualamount1"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Semiannual (twice a year) enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void two_Amount1_Selfemp_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.employer1").sendKeys(getcovidrecertData("Selfempweeklyname1"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.frequency1").sendKeys(getcovidrecertData("Selfempweeklyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.amount1").sendKeys(getcovidrecertData("Selfempweeklyamount1"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void two_Amount2_Selfemp_Bimonthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.employer2").sendKeys(getcovidrecertData("SelfempBimonthlyname2"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.frequency2").sendKeys(getcovidrecertData("SelfempBimonthlyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.amount2").sendKeys(getcovidrecertData("SelfempBimonthlyamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Bimonthly (every two months) enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount2_Selfemp_Annual() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.employer2").sendKeys(getcovidrecertData("Selfempannualname2"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.frequency2").sendKeys(getcovidrecertData("Selfempquarterlyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.amount2").sendKeys(getcovidrecertData("Selfempannualamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount2_Selfemp_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.employer2").sendKeys(getcovidrecertData("Selfempweeklyname2"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.frequency2").sendKeys(getcovidrecertData("Selfempweeklyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.amount2").sendKeys(getcovidrecertData("Selfempweeklyamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount3_Selfemp_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.employer3").sendKeys(getcovidrecertData("Selfempmonthlyname3"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.frequency3").sendKeys(getcovidrecertData("Selfempmonthlyfrequency3"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.amount3").sendKeys(getcovidrecertData("Selfempmonthlyamount3"));
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount3_Selfemp_Quarterly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.employer3").sendKeys(getcovidrecertData("Selfempquarterlyname3"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.frequency3").sendKeys(getcovidrecertData("Selfempquartelyfrequency3"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.amount3").sendKeys(getcovidrecertData("Selfempquarterlyamount3"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void two_Amount3_Selfemp_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.employer3").sendKeys(getcovidrecertData("Selfempweeklyname3"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.frequency3").sendKeys(getcovidrecertData("Selfempweeklyfrequency3"));
			commonFunctions.getElement(driver, "covidrecert.selfemp.amount3").sendKeys(getcovidrecertData("Selfempweeklyamount3"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_selfemp_Amount1() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.remove1").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_selfemp_Amount2() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_selfemp_Amount3() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_selfemp_Amount4() {
		try {
			commonFunctions.getElement(driver, "covidrecert.selfemp.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
//******************************************* work study
	
	public void addanother_Workstudy() {
		try {
			commonFunctions.getElement(driver, "covidrecert.addanotherworkstudy").click();
			initializer.wait(2);
			initializer.infoCall(" Add another work study clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void three_Amount1_Workstudy_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.employer1").sendKeys(getcovidrecertData("Workstudymonthlyname1"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.frequency1").sendKeys(getcovidrecertData("Workstudymonthlyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.amount1").sendKeys(getcovidrecertData("Workstudymonthlyamount1"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount1_Workstudy_Semiannual() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.employer1").sendKeys(getcovidrecertData("Workstudysemiannualname1"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.frequency1").sendKeys(getcovidrecertData("Workstudysemiannualfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.amount1").sendKeys(getcovidrecertData("Workstudysemiannualamount1"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Semiannual (twice a year) enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void three_Amount1_Workstudy_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.employer1").sendKeys(getcovidrecertData("Workstudyweeklyname1"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.frequency1").sendKeys(getcovidrecertData("Workstudyweeklyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.amount1").sendKeys(getcovidrecertData("Workstudyweeklyamount1"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void three_Amount2_Workstudy_Bimonthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.employer2").sendKeys(getcovidrecertData("Workstudybimonthlyname2"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.frequency2").sendKeys(getcovidrecertData("Workstudybimonthlyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.amount2").sendKeys(getcovidrecertData("Workstudybimonthlyamount2"));
			initializer.successCallwithSnapShot("Amount  Bimonthly (every two months) enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount2_Workstudy_Annual() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.employer2").sendKeys(getcovidrecertData("Workstudyannualname2"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.frequency2").sendKeys(getcovidrecertData("Workstudyannualfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.amount2").sendKeys(getcovidrecertData("Workstudyannualamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  every two weeks enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount2_Workstudy_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.employer2").sendKeys(getcovidrecertData("Workstudyweeklyname2"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.frequency2").sendKeys(getcovidrecertData("Workstudyweeklyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.amount2").sendKeys(getcovidrecertData("Workstudyweeklyamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount3_Workstudy_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.employer3").sendKeys(getcovidrecertData("Workstudymonthlyname3"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.frequency3").sendKeys(getcovidrecertData("Workstudymonthlyfrequency3"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.amount3").sendKeys(getcovidrecertData("Workstudymonthlyamount3"));
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount3_Workstudy_Quarterly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.employer3").sendKeys(getcovidrecertData("Workstudyquarterlyname3"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.frequency3").sendKeys(getcovidrecertData("Workstudyquarterlyfrequency3"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.amount3").sendKeys(getcovidrecertData("Workstudyquarterlyamount3"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void three_Amount3_Workstudy_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.employer3").sendKeys(getcovidrecertData("Workstudyweeklyname3"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.frequency3").sendKeys(getcovidrecertData("Workstudyweeklyfrequency3"));
			commonFunctions.getElement(driver, "covidrecert.workstudy.amount3").sendKeys(getcovidrecertData("Workstudyweeklyamount3"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  weekly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_workstudy_Amount1() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.remove1").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void remove_workstudy_Amount2() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.remove2").click();
			initializer.infoCall("Amount2 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_workstudy_Amount3() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.remove3").click();
			initializer.infoCall("Amount3 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void remove_workstudy_Amount4() {
		try {
			commonFunctions.getElement(driver, "covidrecert.workstudy.remove4").click();
			initializer.infoCall("Amount4 remove button click successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void earning_No() {
		try {
			commonFunctions.getElement(driver, "covidrecert.earning.no").click();
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
			commonFunctions.getElement(driver, "covidrecert.unearned.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addincome() {
		try {
			commonFunctions.getElement(driver, "covidrecert.addincome").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "covidrecert.addincome").getText();
			initializer.successCallwithSnapShot("clicked '" + title + "' button successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addnameunearned() {
		try {
			commonFunctions.getElement(driver, "covidrecert.nameunearned").click();
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
			commonFunctions.getElement(driver, "covidrecert.addanotherunemployment").click();
			initializer.wait(2);
			initializer.infoCall(" Add another unemployment clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount1_Unemployment_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.unemp.frequency1").sendKeys(getcovidrecertData("Unemploymentmonthlyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.unemp.amount1").sendKeys(getcovidrecertData("Unemploymentmonthlyamount1"));
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_Unemployment_Annual() {
		try {
			commonFunctions.getElement(driver, "covidrecert.unemp.frequency1").sendKeys(getcovidrecertData("Unemploymentannualfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.unemp.amount1").sendKeys(getcovidrecertData("Unemploymentannualamount1"));
			initializer.successCallwithSnapShot("Amount Annual enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount2_Unemployment_Quarterly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.unemp.frequency2").sendKeys(getcovidrecertData("Unemploymentquarterlyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.unemp.amount2").sendKeys(getcovidrecertData("Unemploymentquarterlyamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_Unemployment_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.unemp.frequency2").sendKeys(getcovidrecertData("Unemploymentweeklyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.unemp.amount2").sendKeys(getcovidrecertData("Unemploymentweeklyamount2"));
			initializer.wait(10);
			initializer.successCallwithSnapShot("Amount  week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
//	******** pension
	public void addanotherpension() {
		try {
			commonFunctions.getElement(driver, "covidrecert.addanotherpension").click();
			initializer.wait(2);
			initializer.infoCall(" Add another wages clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_pension_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.pension.frequency1").sendKeys(getcovidrecertData("Pensionbenefitmonthlyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.pension.amount1").sendKeys(getcovidrecertData("Pensionbenefitmonthlyamount1"));
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_pension_Annual() {
		try {
			commonFunctions.getElement(driver, "covidrecert.pension.frequency1").sendKeys(getcovidrecertData("Pensionbenefitannualfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.pension.amount1").sendKeys(getcovidrecertData("Pensionbenefitannualamount1"));
			initializer.successCallwithSnapShot("Amount Annual enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount2_pension_Quarterly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.pension.frequency2").sendKeys(getcovidrecertData("Pensionbenefitquarterlyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.pension.amount2").sendKeys(getcovidrecertData("Pensionbenefitquarterlyamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_pension_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.pension.frequency2").sendKeys(getcovidrecertData("Pensionbenefitweeklyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.pension.amount2").sendKeys(getcovidrecertData("Pensionbenefitweeklyamount2"));
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
			commonFunctions.getElement(driver, "covidrecert.addanotherveteran").click();
			initializer.wait(2);
			initializer.infoCall(" Add another veteran's benifits clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_veteranbenefit_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.veteran.frequency1").sendKeys(getcovidrecertData("Veteranbenefitmonthlyfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.veteran.amount1").sendKeys(getcovidrecertData("Veteranbenefitmonthlyamount1"));
			initializer.successCallwithSnapShot("Amount monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void one_Amount1_veteranbenefit_Annual() {
		try {
			commonFunctions.getElement(driver, "covidrecert.veteran.frequency1").sendKeys(getcovidrecertData("Veteranbenefitannualfrequency1"));
			commonFunctions.getElement(driver, "covidrecert.veteran.amount1").sendKeys(getcovidrecertData("Veteranbenefitannualamount1"));
			initializer.successCallwithSnapShot("Amount Annual enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void one_Amount2_veteranbenefit_Quarterly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.veteran.frequency2").sendKeys(getcovidrecertData("Veteranbenefitquarterlyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.veteran.amount2").sendKeys(getcovidrecertData("Veteranbenefitquarterlyamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  Quarterly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void one_Amount2_veteranbenefit_Weekly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.veteran.frequency2").sendKeys(getcovidrecertData("Veteranbenefitweeklyfrequency2"));
			commonFunctions.getElement(driver, "covidrecert.unveteran.amount2").sendKeys(getcovidrecertData("Veteranbenefitweeklyamount2"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  week enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void unearned_No() {
		try {
			
			commonFunctions.getElement(driver, "covidrecert.unearned.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void childSupport_Yes() {
		try {
			commonFunctions.getElement(driver, "covidrecert.childSupport.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addchildsuppot() {
		try {
			commonFunctions.getElement(driver, "covidrecert.childsupport").click();
			initializer.wait(2);
			initializer.infoCall(" Add another household member's child support clicked successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addnamechildsupport() {
		try {
			commonFunctions.getElement(driver, "covidrecert.namechildsupport").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void covidrecertchildsupportEdit() {
		try {
			commonFunctions.getElement(driver, "covidrecert.child.edit").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
			}
		}
	
	
	
	public void one_Amount1_child_Monthly() {
		try {
			commonFunctions.getElement(driver, "covidrecert.child.amount").sendKeys(getcovidrecertData("Childsupportmonthlyamount1"));
			commonFunctions.getElement(driver, "covidrecert.child.frequency").sendKeys(getcovidrecertData("Childsupportmonthlyfrequency1"));
			initializer.wait(2);
			initializer.successCallwithSnapShot("Amount  monthly enter successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void covidrecertchilddone() {
		try {
			commonFunctions.getElement(driver, "covidrecert.child.done").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void covidrecertchildremove() {
		try {
			commonFunctions.getElement(driver, "covidrecert.child.remove").click();
			initializer.wait(2);
			initializer.successCallwithSnapShot("Removed child expenses successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void childSupport_No() {
		try {
			
			commonFunctions.getElement(driver, "covidrecert.childSupport.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void otherinfo_No() {
		try {
			commonFunctions.getElement(driver, "covidrecert.otherinfo.no").click();
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

				String agree = commonFunctions.getElement(driver, "covidrecert.agree.validation").getText();
				initializer.successCallwithSnapShot("Agree check box - field validation: " + agree);
				Assert.assertEquals(agree, agreeValidation);

				String sign = commonFunctions.getElement(driver, "covidrecert.sign.validation").getText();
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
			commonFunctions.getElement(driver, "covidrecert.sign.agree").click();
			commonFunctions.getElement(driver, "covidrecert.sign.sign").sendKeys(getName());
			initializer.wait(5);
			initializer.successCallwithSnapShot("Client Agree and Signed successfully ");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(5);
			initializer.successCallwithSnapShot("Client clicked submit covidrecert report successfully ");
			initializer.wait(5);
			String fileName = commonFunctions.getElement(driver, "covidrecertreport.download").getText();
//			String myNumber = commonFunctions.getElement(driver, "covidrecertapplication.number").getText();
//			deleteRecord_DB(myNumber.split(":")[1].trim());
			commonFunctions.getElement(driver, "covidrecertreport.download").click();
			initializer.wait(5);
			initializer.successCallwithSnapShot(fileName + " successfull!");
			initializer.successCallwithSnapShot( "covidrecert report datasheet available at \\DTAConnect\\src\\test\\resources\\Results\\Downloads");
			initializer.wait(5);

		} catch (Exception e) {

			initializer.failureCallwithException("Exception occured in 'Sign & submit---Sign & Submit' Page", e);

		}
	}
	
}
