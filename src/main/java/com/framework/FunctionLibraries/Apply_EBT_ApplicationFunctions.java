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

public class Apply_EBT_ApplicationFunctions {

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

	public Apply_EBT_ApplicationFunctions() {
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
	public Apply_EBT_ApplicationFunctions(CommonFunctions commonFunctions, Initializer initializer, Stage stage,
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
	
//	*********************Apply EBT Application! Generic methods************************************************************************************************************

	public String getPebtData(String param) {
		return commonFunctions.getTestData(stage.getTestName(), param, "file.pebtDataFilePath");
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
	
	public void applyEBTButton() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "ebt.applyEBT").click();
			initializer.infoCall("Clicking 'Pandemic P-EBT!' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'Pandemic P-EBT' application is launched successfully");
			String title = commonFunctions.getElement(driver, "ebt.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		}
		catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'DTA Connect home' Page", e);
		}
	}
	public void change_pebt() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "ebt.pebtcaseinfo").click();
			initializer.infoCall("Clicking 'Check P-EBT Case Information !' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'Check P-EBT Case Information' page is launched successfully");
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		}
		catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'Check P-EBT Case Information' Page", e);
		}
	}
	
	public void enter_ebt() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "ebt.casenumber").sendKeys(getPebtData("Cardnumber"));
			initializer.successCallwithSnapShot("'Check P-EBT Case Information' is verified successfully");
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		}
		catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'Check P-EBT Case Information' Page", e);

		}
	}
	public void replace_pebt() {
		try {
			initializer.wait(2);
			commonFunctions.getElement(driver, "ebt.replacepebt").click();
			initializer.infoCall("Clicking 'Request Pandemic P-EBT card !' button sucessfully.");
			initializer.wait(2);
			initializer.successCallwithSnapShot("'Request Pandemic P-EBT card' application is launched successfully");
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' page successfully");
		}
		catch (Exception e) {
			initializer.failureCallwithException("Exception occured in 'Replace P-EBT card' Page", e);
		}
	}
	public void continue_Option() {

		try {
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.successCallwithSnapShot("Enter/Select all '" + title + "' Page information successfully.");
			commonFunctions.getElement(driver, "generic.continue").click();
			initializer.wait(2);
			initializer.infoCall("Click 'Continue' button sucessfully.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void goBack_Option() {

		try {
			initializer.wait(5);
			commonFunctions.getElement(driver, "generic.goback").click();
			initializer.infoCall("Click 'Go Back' button sucessfully.");
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "ebt.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' Page successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}
	public void close_page() {

		try {
			initializer.wait(5);
			commonFunctions.getElement(driver, "ebt.close").click();
			initializer.infoCall("Click 'close' button sucessfully.");
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "ebt.title").getText();
			initializer.successCallwithSnapShot("Navigate to '" + title + "' Page successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}
	public void noOne_Option() {
		try {
			commonFunctions.getElement(driver, "generic.noone").click();
			initializer.wait(2);
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.infoCall("Select 'No one' option successfully in ' " + title + " 'page.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in (Disability) '" + title + "' Page", e);
		}
	}

	public void yes_Option() {
		try {
			commonFunctions.getElement(driver, "generic.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void no_Option() {
		try {
			commonFunctions.getElement(driver, "generic.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void fnlndob_ValidationMsg() {
		try {
			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String fname = commonFunctions.getElement(driver, "ebt.fnvalidation").getText();
				String lname = commonFunctions.getElement(driver, "ebt.lnvalidation").getText();
				String dob = commonFunctions.getElement(driver, "ebt.dobvalidation").getText();
				initializer.infoCall("First Name validation: " + fname);
				initializer.infoCall("Last Name validation: " + lname);
				initializer.infoCall("Date of Birth validation: " + dob);
				Assert.assertEquals(fname, FValidation);
				Assert.assertEquals(lname, LValidation);
				Assert.assertEquals(dob, DOBValidation);
			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.successCallwithSnapShot(
					"First Name, Last Name and Date of birth fields validated successfully in '" + title + "' Page.");

		} catch (Exception e) {
			String title1 = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title1 + "' Page", e);
		}
	}

	public void first_Last_Name() {
		try {
			commonFunctions.getElement(driver, "ebt.fname").sendKeys(getPebtData("FName"));
			commonFunctions.getElement(driver, "ebt.lname").sendKeys(getPebtData("LName"));
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}	
	
	public void first_Last_Name1() {
		try {
			commonFunctions.getElement(driver, "ebt.fname").sendKeys(getPebtData("FName1"));
			commonFunctions.getElement(driver, "ebt.lname").sendKeys(getPebtData("LName1"));
			initializer.wait(2);
			initializer.infoCall("Enter First name and Last name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void middle_Name() {
		try {
			commonFunctions.getElement(driver, "ebt.mname").sendKeys(getPebtData("MName"));
			initializer.infoCall("Enter middle name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}	
	
	public void middle_Name1 () {
		try {
			commonFunctions.getElement(driver, "ebt.mname").sendKeys(getPebtData("MName1"));
			initializer.infoCall("Enter middle name successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void dateOfBirth_Minor() {
		try {
			commonFunctions.getElement(driver, "ebt.day").sendKeys(getPebtData("Day"));
			selectDropDown(commonFunctions.getElement(driver, "ebt.month"), getPebtData("Month"));
			commonFunctions.getElement(driver, "ebt.year").sendKeys(getPebtData("Year"));
			initializer.infoCall("Enter date of birth under minor group age.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void dateOfBirth_Minor1() {
		try {
			commonFunctions.getElement(driver, "ebt.day").sendKeys(getPebtData("Day1"));
			selectDropDown(commonFunctions.getElement(driver, "ebt.month"), getPebtData("Month1"));
			commonFunctions.getElement(driver, "ebt.year").sendKeys(getPebtData("Year1"));
			initializer.infoCall("Enter date of birth under minor group age.");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void Address_Phone_ValidationMsg() {
		try {

			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String addr = commonFunctions.getElement(driver, "ebt.addvalidation").getText();
				String phone = commonFunctions.getElement(driver, "ebt.phnvalidation").getText();
				
				initializer.infoCall("Address validation: " + addr);
				initializer.infoCall("Phone number validation: " + phone);
				Assert.assertEquals(addr, addressvalidation);
				Assert.assertEquals(phone, phonevalidation);

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.successCallwithSnapShot("All '" + title + "' Page Required fields validated successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void Address_Phone_ValidationMsg1() {
		try {

			commonFunctions.getElement(driver, "generic.continue").click();
			try {
				String addr = commonFunctions.getElement(driver, "ebt.addvalidation").getText();
				String phone = commonFunctions.getElement(driver, "ebt.phnvalidation").getText();
				
				initializer.infoCall("Address validation: " + addr);
				initializer.infoCall("Phone number validation: " + phone);
				Assert.assertEquals(addr, addressvalidation);
				Assert.assertEquals(phone, phonevalidation);

			} catch (AssertionError e) {
				initializer.failureCallwithExceptionAssertion("The Failed", e);
			}
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.successCallwithSnapShot("All '" + title + "' Page Required fields validated successfully ");
			initializer.wait(2);
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void address() {
		try {
			commonFunctions.getElement(driver, "ebt.street").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "ebt.street").sendKeys(Keys.DELETE);
			initializer.wait(2);
			commonFunctions.getElement(driver, "ebt.street").sendKeys(getPebtData("StreetName"));
			commonFunctions.getElement(driver, "ebt.zip").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "ebt.zip").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "ebt.zip").sendKeys(getPebtData("Zipcode"));
			commonFunctions.getElement(driver, "ebt.state").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "ebt.state").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "ebt.state").sendKeys("MA");
			initializer.wait(2);
			initializer.infoCall("Enter client address successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void address1() {
		try {
			commonFunctions.getElement(driver, "ebt.street").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "ebt.street").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "ebt.street").sendKeys(getPebtData("StreetName1"));
			commonFunctions.getElement(driver, "ebt.zip").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "ebt.zip").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "ebt.zip").sendKeys(getPebtData("Zipcode1"));
			commonFunctions.getElement(driver, "ebt.state").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
			commonFunctions.getElement(driver, "ebt.state").sendKeys(Keys.DELETE);
			commonFunctions.getElement(driver, "ebt.state").sendKeys("MA");
			initializer.wait(2);
			initializer.infoCall("Enter client address successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void phone() {
		try {
			commonFunctions.getElement(driver, "ebt.phone").clear();
			commonFunctions.getElement(driver, "ebt.phone").sendKeys(getPebtData("Phone"));
			initializer.infoCall("Entered phone number successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '"+title+"' Page", e);
		}
	}

	
	public void phone1() {
		try {
			commonFunctions.getElement(driver, "ebt.phone").clear();
			commonFunctions.getElement(driver, "ebt.phone").sendKeys(getPebtData("Phone1"));
			initializer.infoCall("Entered phone number successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '"+title+"' Page", e);
		}
	}
	public void message_alert_Yes() {
		try {
			commonFunctions.getElement(driver, "ebt.message.alert.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void message_alert_Yes1() {
		try {
			commonFunctions.getElement(driver, "ebt.message.alert.yes").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'YES' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void message_alert_No() {
		try {
			commonFunctions.getElement(driver, "ebt.message.alert.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void message_alert_No1() {
		try {
			commonFunctions.getElement(driver, "ebt.message.alert.no").click();
			initializer.wait(2);
			initializer.infoCall("Select option as 'No' successfully");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void childcasenumber() {
		try {
			commonFunctions.getElement(driver, "ebt.childcasenumber").sendKeys(getPebtData("Casenumber"));
			initializer.infoCall("Enter child case number successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	
	public void childcasenumber1() {
		try {
			commonFunctions.getElement(driver, "ebt.childcasenumber").sendKeys(getPebtData("Casenumber1"));
			initializer.infoCall("Enter child case number successfully.");
		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebt.card.title").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);
		}
	}
	public void exit() {
		try {
			String title = commonFunctions.getElement(driver, "ebt.title").getText();
			initializer.successCallwithSnapShot("Displayed '" + title + "' page successfully.");
			commonFunctions.getElement(driver, "ebt.exit").click();
			initializer.successCallwithSnapShot("Exit the application successfully. ");

		} catch (Exception e) {
			String title = commonFunctions.getElement(driver, "ebtititle").getText();
			initializer.failureCallwithException("Exception occured in '" + title + "' Page", e);

		}
	}

}
