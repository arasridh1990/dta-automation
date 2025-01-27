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

public class Apply_RECERT_ApplicationFunctions {

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

	public Apply_RECERT_ApplicationFunctions() {
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
	public Apply_RECERT_ApplicationFunctions(CommonFunctions commonFunctions, Initializer initializer, Stage stage,
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

	String FValidation = "Please enter the child's first name";
	String LValidation = "Please enter the child's last name";
	String FNValidation = "Please enter first name";
	String LSValidation = "Please enter last name";

	String emailvalidation = "Please enter a valid Email";
	String passwordvalidation = "Please enter a password";
	String agreeValidation = "Please agree to the terms";
	String signValidation = "Please sign your Recertification";
	
	String DOBValidation = "Please enter a valid date of birth";
	String addressvalidation = "Please enter a valid address";
	String phonevalidation = "Please enter a valid phone number";
	String selectoptionvalidation = "Please select an option below";

	
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
	public String getSASID() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		System.out.println(num);
		String number = num.substring(num.length() - 10, num.length());
		System.out.println(number);
		String SASID = number;
		return SASID;
	}
	public String getTripCount() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 5, num.length());
		String SSN = number;
		return SSN;
	}
	public String getNumber() {
		String num = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		String number = num.substring(num.length() - 10, num.length());
		return number;
	}
	public String getAmount() {
		float max = 1000;
		float min = 100;
		Random rn = new Random();
		float i = rn.nextFloat() * (max - min) + min;
		String s = Float.toString(i);
		return s;
	}

	public String getEmailDev2() {
		String email = "conconsumer3@eotss.odc";
		return email;
	}

	public String getWrongEmail() {
		String email = "username_@test.com";
		return email;
	}
	
	public String getPassword() {
		String password = "Test@123";
		return password;
	}

	public String getWrongPassword() {
		String password = "test@1234";
		return password;
	}
	
	public String getEmail() {
		String email = "Susenjohn@gmail.com";
		return email;
	}
	
//	*********************Apply RECERTIFICATION Application! Generic methods************************************************************************************************************

	public String getRecertData(String param) {
		return commonFunctions.getTestData(stage.getTestName(), param, "file.recertDataFilePath");
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
	
	public void continue_Option() {

		try {
			initializer.wait(5);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Enter/Select all '" + title + "' Page information successfully.");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(5);
			initializer.infoCall("Click 'Continue' button sucessfully.");
			initializer.wait(5);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void goBack_Option() {

		try {
			initializer.wait(5);
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
			initializer.wait(5);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
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
	
	public void pr_no_Option() {
		try {
			commonFunctions.getElement(driver, "generic.no.pr").click();
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
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "login.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void emailPassword() {
		try {
			commonFunctions.getElement(driver, "login.email").clear();
			commonFunctions.getElement(driver, "login.password").clear();
			String email = getEmailDev2();
			String password = getPassword();
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
	
	public void startRecertificationButton() {
		
		try {
		    initializer.wait(10);
		    commonFunctions.getElement(driver, "recert.start").click();
		    initializer.infoCall("Clicking 'Start Recertification' button sucessfully.");
		    initializer.wait(2);
		    initializer.successCallwithSnapShot("'Recertification Application' is launched successfully");
		    String title = commonFunctions.getElement(driver, "page.title").getText();
		    initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
	    } catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
	    }
		
	}
	
     public void portugueseLanguage() {
		
		try {
		    initializer.wait(10);
		    commonFunctions.getElement(driver, "pr.recert.start").click();
		    initializer.infoCall("Clicking 'Iniciar Recertificação' button sucessfully.");
		    initializer.wait(2);
		    initializer.successCallwithSnapShot("'Recertificação aplicación' is launched successfully");
		    String title = commonFunctions.getElement(driver, "page.title").getText();
		    initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
	    } catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
	    }
		
	}
	public void languageSelect() {
		try {
			initializer.wait(5);
			selectDropDown(commonFunctions.getElement(driver, "language.select"), getRecertData("Language-Portuguese"));
			initializer.infoCall("Language selected sucessfully.");
			initializer.wait(5);
			} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}
	
	// *************** Contact info **************
	public void recertContact_Yes() {
		try {
			commonFunctions.getElement(driver, "recert.contact.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void recertContact_No() {
		try {
			commonFunctions.getElement(driver, "recert.contact.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void pr_recertContact_No() {
		try {
			commonFunctions.getElement(driver, "pr.recert.contact.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void recertContactEdit2() {
		try {
			commonFunctions.getElement(driver, "recert.contact.edit2").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void recertContactEdit3() {
		try {
			commonFunctions.getElement(driver, "recert.contact.edit3").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
			} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void recertPhone() {
		try {
			String phone = getPhoneNumber();
			commonFunctions.getElement(driver, "recert.phone").clear();
			initializer.wait(2);
			commonFunctions.getElement(driver, "recert.phone").sendKeys(getRecertData("Phone"));
			initializer.infoCall("Enter phone number :" + phone + " successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void recert_message_alert_Yes() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "recert.message.alert.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void email() {
		try {
			commonFunctions.getElement(driver, "recert.email").clear();
			commonFunctions.getElement(driver, "recert.email").sendKeys(getRecertData("Email"));
			initializer.infoCall("Please enter a valid email address ");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void recert_email_notification_Yes() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "recert.emailnotification.yes").click();
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
			commonFunctions.getElement(driver, "recert.email").clear();
			String email = getEmail();
			commonFunctions.getElement(driver, "recert.email").sendKeys(getRecertData("Email"));
			initializer.infoCall("Enter client email address as " + email + " successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void recert_email_notification_No() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "recert.emailnotification.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void recertHousehold_No() {
		try {
			commonFunctions.getElement(driver, "recert.household.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'NO' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void pr_recertHousehold_No() {
		try {
			commonFunctions.getElement(driver, "pr.recert.household.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'Não' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void recertHousehold_Yes() {
		try {
			commonFunctions.getElement(driver, "recert.household.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as '?YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void addhouseholdmember() {
		try {
			commonFunctions.getElement(driver, "recert.addhouseholdmember").click();
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
				String fname = commonFunctions.getElement(driver, "recert.fnvalidation").getText();
				String lname = commonFunctions.getElement(driver, "recert.lnvalidation").getText();
				String dob = commonFunctions.getElement(driver, "recert.dobvalidation").getText();
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
			commonFunctions.getElement(driver, "recert.fname").sendKeys(getRecertData("FName"));
			commonFunctions.getElement(driver, "recert.lname").sendKeys(getRecertData("LName"));
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void middle_Name() {
		try {
			commonFunctions.getElement(driver, "recert.mname").sendKeys(getRecertData("MName"));
			initializer.infoCall("Enter middle name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void dateOfBirth() {
		try {
			commonFunctions.getElement(driver, "recert.day").sendKeys(getRecertData("Day"));
			selectDropDown(commonFunctions.getElement(driver, "recert.month"), getRecertData("Month"));
			commonFunctions.getElement(driver, "recert.year").sendKeys(getRecertData("Year"));
			initializer.infoCall("Enter date of birth under senior group age.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void suffix_Name() {
		try {
			getDropDownValue(commonFunctions.getElement(driver, "recert.household.suffix"));
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
			commonFunctions.getElement(driver, "recert.nossn").click();
			initializer.infoCall("Click 'I don't have one' button sucessfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}

	public void sSN_Option() {
		try {
			commonFunctions.getElement(driver, "recert.ssn").clear();
			String SSN = getSSN();
			commonFunctions.getElement(driver, "household.ssn").sendKeys(getRecertData("SSN"));
			initializer.infoCall("Enter SSN number :" + SSN + " successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void son_AboutMember() {
		try {
			commonFunctions.getElement(driver, "recert.son").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Select person's relationship as 'Son' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}	
	
	public void male_Option() {
		try {
			commonFunctions.getElement(driver, "recert.male").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("Select gender as 'Male' successfully in '" + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void uscitizen_Yes() {
		try {
			commonFunctions.getElement(driver, "recert.uscitizen.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void food_Yes() {
		try {
			commonFunctions.getElement(driver, "recert.food.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as food 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void income_Yes() {
		try {
			commonFunctions.getElement(driver, "recert.income.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as income 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void nonCitizen_Yes() {
		try {
			initializer.infoCall("Noncitizen");
			commonFunctions.getElement(driver, "recert.citienshipchanged.yes").click();
			initializer.wait(5);
			initializer.infoCall("Select option as noncitizen 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("+++++++Exception occured in '" + title + "' Page", e);
		}
	}
	public void student_None() {
		try {
			commonFunctions.getElement(driver, "recert.student.checkbox.none").click();
			initializer.infoCall("Checkbox done");
			initializer.wait(5);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("title done");
			initializer.successCallwithSnapShot(
					"If anyone in the household is attending high school or college, please select their name(s) in '" + title + "' Page.");
			initializer.log("snapshot");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void pr_student_None() {
		try {
			commonFunctions.getElement(driver, "pr.recert.student.checkbox.none").click();
			initializer.infoCall("Checkbox done");
			initializer.wait(5);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("title done");
			initializer.successCallwithSnapShot(
					"If anyone in the household is attending high school or college, please select their name(s) in '" + title + "' Page.");
			initializer.log("snapshot");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void student() {
		try {
			commonFunctions.getElement(driver, "recert.student.checkbox").click();
			initializer.wait(5);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"If anyone in the household is attending high school or college, please select their name(s) in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void student_dtl() {
		try {
			commonFunctions.getElement(driver, "recert.student.dtl.chkbx").click();
			commonFunctions.getElement(driver, "recert.student.schl.name").sendKeys(getRecertData("School"));
			commonFunctions.getElement(driver, "recert.student.sasid").sendKeys(getRecertData("SASID"));
			initializer.wait(2);	
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Enter school details in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	
	
	public void dependent_None() {
		try {
			commonFunctions.getElement(driver, "recert.dependent.chkbx.none").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your dependent care costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void dependent() {
		try {
			commonFunctions.getElement(driver, "recert.dependent.checkbox").click();
			commonFunctions.getElement(driver, "recert.dpdt.carefor.chkbx").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your dependent care costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void dependent_dtl() {
		try {			
			commonFunctions.getElement(driver, "recert.dpdt.pay.amt").sendKeys(getRecertData("DpdtPayAmount"));
			commonFunctions.getElement(driver, "recert.dpdt.pay.period").sendKeys(getRecertData("DpdtPayPeriod"));
			commonFunctions.getElement(driver, "recert.dpdt.transport.yes").click();
			commonFunctions.getElement(driver, "recert.dpdt.transport.amt").sendKeys("DpdtTrnsrtAmount");
			commonFunctions.getElement(driver, "recert.dpdt.transport.period").sendKeys("DpdtTrnsrtPeriod");
			initializer.wait(2);
			commonFunctions.getElement(driver, "recert.dpdt.drive.yes").click();
			commonFunctions.getElement(driver, "recert.dpdt.hwmny.provider").click();			
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Dependent care costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void Address_Phone_ValidationMsg() {
		try {

			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String addr = commonFunctions.getElement(driver, "recert.addvalidation").getText();
				
				initializer.infoCall("Address validation: " + addr);
				Assert.assertEquals(addr, addressvalidation);

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("All '" + title + "' Page Required fields validated successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void address() {
		try {
			commonFunctions.getElement(driver, "recert.street").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "recert.street").sendKeys(Keys.DELETE);
			initializer.wait(2);
			commonFunctions.getElement(driver, "recert.street").sendKeys(getRecertData("StreetName"));
			commonFunctions.getElement(driver, "recert.zip").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "recert.zip").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "recert.zip").sendKeys(getRecertData("Zipcode"));
			commonFunctions.getElement(driver, "recert.city").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "recert.city").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "recert.city").sendKeys(getRecertData("City"));
//			initializer.wait(2);
//			commonFunctions.getElement(driver, "recert.state").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
//			commonFunctions.getElement(driver, "recert.state").sendKeys(Keys.DELETE);
//			commonFunctions.getElement(driver, "recert.state").sendKeys("MA");
			initializer.wait(2);
			initializer.infoCall("Enter address successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void dependent_Care_Cost() {
		try {
			initializer.wait(5);
			commonFunctions.getElement(driver, "recert.dpdt.oftn.drive").sendKeys(getRecertData("DpdtOftnDrive"));
			commonFunctions.getElement(driver, "recert.dpdt.oftn.period").sendKeys(getRecertData("DpdtOftnPeriod"));
			initializer.wait(2);			
			initializer.infoCall("Enter/Select dependent care cost successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void sheltercost_None() {
		try {
			commonFunctions.getElement(driver, "recert.sheltercost.chkbx.none").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your shelter costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void sheltercost() {
		try {
			commonFunctions.getElement(driver, "recert.shelter.cost.checkbox").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your shelter costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void utilitycost_None() {
		try {
			commonFunctions.getElement(driver, "recert.utilitycost.chkbx.none").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your utility costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void utilitycost() {
		try {
			commonFunctions.getElement(driver, "recert.utility.cost.checkbox").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your utility costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
		
	public void medicalcost_None() {
		try {
			commonFunctions.getElement(driver, "recert.medicalcost.chkbx.none").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your medical costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void medicalcost() {
		try {
			commonFunctions.getElement(driver, "recert.medical.cost.checkbox").click();
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your medical costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void medicalcost_dtl1() {
		try {
			
			commonFunctions.getElement(driver, "recert.medical.cost.type").click();
			commonFunctions.getElement(driver, "recert.medical.total.cost").sendKeys(getRecertData("MedicalTotalCost"));
			commonFunctions.getElement(driver, "recert.medical.aptmt.yes").click();
			commonFunctions.getElement(driver, "recert.medical.hwmny.aptmt").click();			
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your medical costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void medicalcost_dtl2() {
		try {
			commonFunctions.getElement(driver, "recert.medical.oftn.drive").sendKeys(getRecertData("MedicalOftnDrive"));
			commonFunctions.getElement(driver, "recert.medical.oftn.period").sendKeys(getRecertData("MedicalOftnPeriod"));
			commonFunctions.getElement(driver, "recert.medical.trnspt.amt").sendKeys(getRecertData("MedicalTrnsptAmt"));
			commonFunctions.getElement(driver, "recert.medical.trnspt.period").sendKeys(getRecertData("MedicalTrnsptPeriod"));
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot(
					"Tell us about your medical costs in '" + title + "' Page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	

	public void recertSubmitButton() {
		
		try {
		    initializer.wait(5);
		    commonFunctions.getElement(driver, "recert.submit").click();
		    initializer.wait(5);
		    String title = commonFunctions.getElement(driver, "page.title").getText();
		    initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
	   } catch (Exception e) {
		String title = commonFunctions.getElement(driver, "recert.title").getText();
		initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
	   }
		
	}
	
	public void signSubmitValidationMsg() {
		try {
			initializer.infoCall("sign submit");
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.infoCall("sign submit getElement");
			initializer.successCallwithSnapShot("Displayed " + title + " page successfully");
			initializer.wait(5);
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(5);
			try {

				String agree = commonFunctions.getElement(driver, "recert.agreeValidation").getText();
				initializer.successCallwithSnapShot("Agree check box - field validation: " + agree);
				Assert.assertEquals(agree, agreeValidation);

				String sign = commonFunctions.getElement(driver, "recert.signatureValidation").getText();
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
			initializer.wait(2);
			commonFunctions.getElement(driver, "recert.submit.chkbx").click();
			commonFunctions.getElement(driver, "recert.signature").sendKeys(getRecertData("Signature"));
			initializer.wait(3);
			initializer.successCallwithSnapShot("Client Agree and Signed successfully ");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(5);
			initializer.successCallwithSnapShot("Client clicked submit recertification report successfully ");
			initializer.wait(5);
			String fileName = commonFunctions.getElement(driver, "recert.download").getText();
			commonFunctions.getElement(driver, "recert.download").click();
			initializer.wait(5);
			initializer.successCallwithSnapShot(fileName + " successfull!");
			initializer.successCallwithSnapShot( "Recertification report datasheet available at \\DTAConnect\\src\\test\\resources\\Results\\Downloads");
			initializer.wait(2);

		} catch (Exception e) {

			initializer.failureCallwithException("Exception occured in 'Sign & submit---Sign & Submit' Page", e);

		}
	}
	
	public void signSubmit_PR() {
		try {
			String title = commonFunctions.getElement(driver, "page.title").getText();
			initializer.successCallwithSnapShot("Displayed " + title + " page successfully");
			initializer.wait(2);
			commonFunctions.getElement(driver, "pr.recert.submit.chkbx").click();
			commonFunctions.getElement(driver, "pr.recert.signature").sendKeys(getRecertData("Signature"));
			initializer.wait(3);
			initializer.successCallwithSnapShot("Client Agree and Signed successfully ");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(5);
			initializer.successCallwithSnapShot("Client clicked baixar seu relatório provisório concluído report successfully ");
			initializer.wait(5);
			String fileName = commonFunctions.getElement(driver, "pr.recert.download").getText();
			commonFunctions.getElement(driver, "pr.recert.download").click();
			initializer.wait(5);
			initializer.successCallwithSnapShot(fileName + " successfull!");
			initializer.successCallwithSnapShot( "baixar seu relatório provisório concluído datasheet available at \\DTAConnect\\src\\test\\resources\\Results\\Downloads");
			initializer.wait(2);

		} catch (Exception e) {

			initializer.failureCallwithException("Exception occured in 'Sign & submit---Sign & Submit' Page", e);

		}
	}
	
}
