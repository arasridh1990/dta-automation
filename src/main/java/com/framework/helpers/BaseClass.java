package com.framework.helpers;

import java.io.File;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.FunctionLibraries.Apply_CASH_ApplicationFunctions;
import com.framework.FunctionLibraries.Apply_SNAP_ApplicationFunctions;
import com.framework.FunctionLibraries.Apply_SUMMEREBT_ApplicationFunctions;
import com.framework.FunctionLibraries.DTAConnect_ApplicationFunctions;
import com.framework.FunctionLibraries.Apply_INTERIM_REPORT_ApplicationFunctions;
import com.framework.FunctionLibraries.Apply_ProviderLOGIN_ApplicationFunctions;
import com.framework.FunctionLibraries.Apply_EBT_ApplicationFunctions;
import com.framework.FunctionLibraries.Apply_RECERT_ApplicationFunctions;
import com.framework.FunctionLibraries.CommonFunctions;

import com.framework.FunctionLibraries.Apply_COVID_RECERT_ApplicationFunctions;
import com.framework.FunctionLibraries.DB;
import com.framework.FunctionLibraries.TestScripts_ApplyCASH;
import com.framework.FunctionLibraries.TestScripts_ApplySNAP;
import com.framework.FunctionLibraries.TestScripts_ApplySUMMEREBT;
import com.framework.FunctionLibraries.TestScripts_DTAConnect;
import com.framework.FunctionLibraries.TestScripts_ApplyINTERIMREPORT;
import com.framework.FunctionLibraries.TestScripts_ApplyProviderLOGIN;
import com.framework.FunctionLibraries.TestScripts_ApplyEBT;
import com.framework.FunctionLibraries.TestScripts_ApplyRECERT;
import com.framework.FunctionLibraries.TestScripts_ApplyCovidRECERT;
import com.jacob.com.LibraryLoader;

import autoitx4java.AutoItX;

public class BaseClass {
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest test;
	public boolean iStatus = true;
	public String strTestCaseName = null;
	public String strLogFileName = null;
	public String strTestSuiteName = null;
	public AutoItX objAutoIT = null;
	public Initializer initializer = null;
	public Stage stage = null;
	public DB dB = null;
	public CommonFunctions commonFunctions = null;

	public DTAConnect_ApplicationFunctions dta = null;
	public TestScripts_DTAConnect ts_DTA = null;

	public Apply_SNAP_ApplicationFunctions snap = null;
	public TestScripts_ApplySNAP ts_FS = null;

	public Apply_CASH_ApplicationFunctions cash = null;
	public TestScripts_ApplyCASH ts_CASH = null;

	public Apply_EBT_ApplicationFunctions ebt = null;
	public TestScripts_ApplyEBT ts_EBT = null;
	
	public Apply_SUMMEREBT_ApplicationFunctions sebt = null;
	public TestScripts_ApplySUMMEREBT ts_SEBT = null;
	
	public Apply_INTERIM_REPORT_ApplicationFunctions intrep = null;
	public TestScripts_ApplyINTERIMREPORT ts_INTREP = null;
	
	public Apply_RECERT_ApplicationFunctions recert = null;
	public TestScripts_ApplyRECERT ts_RECERT = null;
	
	public Apply_ProviderLOGIN_ApplicationFunctions login = null;
	public TestScripts_ApplyProviderLOGIN ts_LOGIN= null;
	
	public Apply_COVID_RECERT_ApplicationFunctions covidrecert = null;
	public TestScripts_ApplyCovidRECERT ts_COVIDRECERT = null;
	
	public BaseClass() {
	}

	/**
	 * Function Name : beforeSuite(), Description : This function will perform
	 * health check. This will execute before the suite
	 *
	 * @throws ParseException Exception
	 * @author Santhosh Karra
	 */
	@BeforeTest
	public void beforeSuite() throws ParseException {
		// Below lines are commented out because these properties will help in debug

		System.setProperty("testenv", "QA");
		System.setProperty("enableSnapshots", "NO");
//		System.setProperty("Browser", "Firefox");
//		System.setProperty("Browser", "IE");
      	System.setProperty("Browser", "chrome");
		System.setProperty("suiteXmlFile", "ApplySNAP_TestNG.xml");

	}

	/**
	 * Function Name : setUp(), Description : This function initialize the required
	 * objects for suite. This will execute before class
	 * 
	 * @author Santhosh Karra
	 */
	@BeforeClass
	public void setUp() {
		try {
			stage = new Stage();
			initializer = new Initializer(stage);
			dB = new DB(initializer, stage);
			commonFunctions = new CommonFunctions(initializer, stage);

			dta = new DTAConnect_ApplicationFunctions(commonFunctions, initializer, stage, dB);
			ts_DTA = new TestScripts_DTAConnect(commonFunctions, dta, initializer, stage);

			snap = new Apply_SNAP_ApplicationFunctions(commonFunctions, initializer, stage, dB);
			ts_FS = new TestScripts_ApplySNAP(commonFunctions, snap, initializer, stage);

			cash = new Apply_CASH_ApplicationFunctions(commonFunctions, initializer, stage, dB);
			ts_CASH = new TestScripts_ApplyCASH(commonFunctions, cash, initializer, stage);
			
			ebt = new Apply_EBT_ApplicationFunctions(commonFunctions, initializer, stage, dB);
			ts_EBT = new TestScripts_ApplyEBT(commonFunctions, ebt, initializer, stage);
			
			sebt = new Apply_SUMMEREBT_ApplicationFunctions(commonFunctions, initializer, stage, dB);
			ts_SEBT = new TestScripts_ApplySUMMEREBT (commonFunctions, sebt, initializer, stage);
           
			intrep = new Apply_INTERIM_REPORT_ApplicationFunctions(commonFunctions, initializer, stage, dB);
			ts_INTREP = new TestScripts_ApplyINTERIMREPORT(commonFunctions, intrep, initializer, stage);
			
			recert = new Apply_RECERT_ApplicationFunctions(commonFunctions, initializer, stage, dB);
			ts_RECERT = new TestScripts_ApplyRECERT(commonFunctions, recert, initializer, stage);
			
			login = new Apply_ProviderLOGIN_ApplicationFunctions(commonFunctions, initializer, stage, dB);
			ts_LOGIN = new TestScripts_ApplyProviderLOGIN(commonFunctions, login, initializer, stage);
		
			covidrecert = new Apply_COVID_RECERT_ApplicationFunctions(commonFunctions, initializer, stage, dB);
			ts_COVIDRECERT = new TestScripts_ApplyCovidRECERT(commonFunctions, covidrecert, initializer, stage);

			// load props
			initializer.loadProps();

			// ========
			String strFullClassName = this.getClass().getName();
			strTestCaseName = strFullClassName.substring(strFullClassName.lastIndexOf('.') + 1);
			stage.setTestName(strTestCaseName);
			strLogFileName = strTestCaseName + "_"
					+ new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(Calendar.getInstance().getTime());
			// Initialize Extent Reports
			htmlReporter = new ExtentHtmlReporter(initializer.GetValue("java.results.path") + strLogFileName + ".html");
			// Create Log file
			strLogFileName = strTestCaseName + "_"
					+ new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(Calendar.getInstance().getTime());
			// Setting the Log file path in system variables
			System.setProperty("logFileName", strLogFileName);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
			htmlReporter.config().setChartVisibilityOnOpen(true);
			htmlReporter.config().setDocumentTitle("DTAConnect Test Automation");
			htmlReporter.config().setReportName(
					System.getProperty("testenv").toUpperCase() + "-" + System.getProperty("Browser") + "- Report");
			htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
			htmlReporter.config().setTheme(Theme.DARK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Function Name : beforeTestCase(), Description : This function initialize auto
	 * it and extent reports before each test case. This will execute before class *
	 *
	 * @param m This is test name
	 * @author Santhosh Karra
	 */
	@BeforeMethod
	public void beforeTestCase(Method m) {
		try {
			// ****************INITIAL SETUP********************
			stage.setStatus(iStatus);
			strTestCaseName = m.getName();
			stage.setTestName(strTestCaseName);
			commonFunctions.startTestCase(strTestCaseName);
			// ************* AUTOIT SETUP *************************
			File file = new File(initializer.GetValue("java.autoit.jacob"));
			System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
			objAutoIT = new AutoItX();
			test = extent.createTest(stage.getTestName());
			commonFunctions.init(objAutoIT);
//			snap.init(test);
//			snap.init(test);
			initializer.init(test);
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in BeforeMethod", e);
		}
	}

	/*
	 * Function Name : getResult() Description : This captures the resultsParameter
	 * : ITestResult
	 *
	 * @param result TestNG results
	 * 
	 * @author Santhosh Karra
	 */
	@AfterMethod
	public void getResult(ITestResult result) {
		try {
//			 applicationFunctions.quitBrowser();
		} catch (Exception e) {
			initializer.log("Issue in terminating the browser" + e.getMessage());
		}
		try {
			boolean flag = true;
			switch (result.getStatus()) {
			case 1: // Passed
				flag = true;
				break;
			case 2:// Failed
				flag = false;
				break;
			default:
				flag = false;
				break;
			}
			if (flag && stage.getStatus()) {
				stage.setStatus(true);
			} else {
				stage.setStatus(false);
			}
		} catch (Exception e) {
			initializer.failureCallwithException("Exception occured in AfterMethod", e);
		}
		commonFunctions.endTestCase(strTestCaseName, result);
	}

	/*
	 * Function Name : tearDown() Description : This will execute before each class
	 * and close objects
	 * 
	 * @author Santhosh Karra
	 */
	@AfterClass
	public void tearDown() {
		extent.flush();
	}

}
