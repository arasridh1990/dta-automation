package com.framework.helpers;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class Initializer {
	private Stage stage = null;
	public Properties configProps = null;
	public ExtentTest test = null;

	public Initializer() {
	}

	public Initializer(Stage stage) {
		this.stage = stage;
	}

	/**
	 * This method initialize the test object in Application functions library
	 *
	 * @param test
	 *            This is ExtentTest object, ExtentTest object is used for report
	 *            generation.
	 *            @author Santhosh Karra
	 */
	public void init(ExtentTest test) {
		this.test = test;
	}

	/**
	 * Function Name : loadProps Description : This function will read the
	 * properties files for the selected environment
	 * @author Santhosh Karra
	 */
	public void loadProps() {
		String configPropsPath = System.getProperty("user.dir")
				+ "//src//test//resources//properties//config.properties";
		try {
			configProps = new Properties();
			// Load Global environment properties
			FileInputStream propFile = new FileInputStream(configPropsPath);
			configProps.load(propFile);

			// Load environment config properties
			propFile = new FileInputStream(System.getProperty("user.dir")
					+ "//src//test//resources//properties//config." + System.getProperty("testenv") + ".properties");
			configProps.load(propFile);
			// Load OR properties
			propFile = new FileInputStream(
					System.getProperty("user.dir") + "//src//test//resources//properties//or.properties");
			configProps.load(propFile);

		} catch (IOException e) {
			 log("Issue on loading the properties file : " + e.getMessage());
		}
	}

	/**
	 * Function Name : GetValue Description : This function fetch the value for
	 * input param
	 * @param strInput
	 *            Input parameter to retrieve value
	 * @return returns the value of the parameter
	 * @author Santhosh Karra
	 */
	public String GetValue(String strInput) {
		String strOutput = null;
		String strMethodCalling = Thread.currentThread().getStackTrace()[2].getMethodName();
		if (configProps == null) {
			loadProps();
		}
		strOutput = configProps.getProperty(strInput);
		if (strOutput.contains("::")) {
			if (!strMethodCalling.equalsIgnoreCase("GetType")) {
				strOutput = strOutput.split("::")[0].toString();
			}
		}
		if (strOutput == null) {
			System.out.println("Property '" + strInput + "' is not avaiable in the property file");
		}
		return strOutput;
	}

	/**
	 * Function Name : GetType Description : This function fetch the value for input
	 * param
	 * @param strInput
	 *            WebObject property
	 * @return returns the value of the parameter
	 * @author Santhosh Karra
	 */
	public String GetType(String strInput) {
		String strOutput = null;
		strOutput = GetValue(strInput);
		int index = strOutput.lastIndexOf("::");
		strOutput = strOutput.substring(index + 2, strOutput.length());
		return strOutput;
	}

	/**
	 * Function Name : failureCall Description : This function reports the failure
	 * to extent reports and logs in log file
	 * @param strDescription
	 *            Step description to report in result
	 *            @author Santhosh Karra
	 */
	public void failureCall(String strDescription) {
		log(strDescription);
		try {
			test.fail(strDescription,
					MediaEntityBuilder
							.createScreenCaptureFromPath(
									funTakeScreenshot(Thread.currentThread().getStackTrace()[1].getMethodName()))
							.build());
			wait(1);
		} catch (Exception e1) {
			log("Issue in recording snapshot error is : " + e1.getMessage());
		}
		stage.setStatus(false);
	}

	/**
	 * Function Name : failureCall Description : This function reports the failure
	 * to extent reports and logs in log file
	 * @param strDescription
	 *            Step description to report in result
	 *            @author Santhosh Karra
	 */
	public void failureCallWithOutSnapShot(String strDescription) {
		log(strDescription);
		try {
			test.fail(strDescription);
			wait(1);
		} catch (Exception e1) {
			log("Issue in recording snapshot error is : " + e1.getMessage());
		}
		stage.setStatus(false);
	}

	/**
	 * Function Name : failureCallwithException Description : This function reports
	 * the failure and exception to extent reports and logs in log file
	 * @param strDescription
	 *            Step description to report in result
	 * @param e
	 *            throws Exception if any issues
	 *            @author Santhosh Karra
	 */
	public void failureCallwithException(String strDescription, Exception e) {
		log(strDescription + ". Error is: " + e.getMessage());
		try {
			test.fail(strDescription + ". Error is: " + e.getMessage(), MediaEntityBuilder
					.createScreenCaptureFromPath(
							funTakeScreenshot(Thread.currentThread().getStackTrace()[1].getMethodName()))
					.build());
			wait(1);
		} catch (Exception e1) {
			log("Issue in recording snapshot, error is : " + e1.getMessage());
		}
		stage.setStatus(false);
		finalizeResults();
	}

	/**
	 * Function Name : failureCallwithExceptionAssertion Description : This function reports
	 * the AssertionError failure and exception to extent reports and logs in log file
	 * @param strDescription
	 *            Step description to report in result
	 * @param e
	 *            throws Exception if any issues
	 *            @author Santhosh Karra
	 */

	public void failureCallwithExceptionAssertion(String strDescription, AssertionError error) {
		log(strDescription + ". Error is: " + error.getMessage());
		try {
			test.fail(strDescription + ". Error is: " + error.getMessage(), MediaEntityBuilder
					.createScreenCaptureFromPath(
							funTakeScreenshot(Thread.currentThread().getStackTrace()[1].getMethodName()))
					.build());
			wait(1);
		} catch (Exception e1) {
			log("Issue in recording snapshot, error is : " + e1.getMessage());
		}
		stage.setStatus(false);
		finalizeResults();
	}


	/**
	 * Function Name : infoCall Description : This function reports the information
	 * to extent reports and logs in log file, This handles the screenshot to be
	 * captured or not using enableSnapshots from Jenkins/pom.xml
	 * @param strDescription
	 *            Step description to report
	 *            @author Santhosh Karra
	 */
	public void infoCall(String strDescription) {
		log(strDescription);
		test.info(strDescription);
	}

	/**
	 * Function Name : successCall Description : This function reports the success
	 * to extent reports and logs in log file, This handles the screenshot to be
	 * captured or not using enableSnapshots from Jenkins/pom.xml
	 * @param strDescription
	 *            This is description of the step to report
	 *            @author Santhosh Karra
	 */
	public void successCallWithOutSnapShot(String strDescription) {
		log(strDescription);
		test.pass(strDescription);
	}

	/**
	 * Function Name : successCallwithSnapShot Description : This function reports
	 * the success to extent reports and logs in log file, This handles the
	 * screenshot to be captured or not using enableSnapshots from Jenkins/pom.xml
	 *
	 * @param strDescription
	 *            This is description of the step to report
	 *            @author Santhosh Karra
	 */
	public void successCallwithSnapShot(String strDescription) {
		log(strDescription);
		// enableSnapshots will enable / disable screenshot feature based on
		// YES/NO from Jenkins/pom
		if (System.getProperty("enableSnapshots").equals("YES")) {
			try {
				test.pass(strDescription,
						MediaEntityBuilder
								.createScreenCaptureFromPath(
										funTakeScreenshot(Thread.currentThread().getStackTrace()[1].getMethodName()))
								.build());
				wait(1);
			} catch (Exception e) {
				log("Issue in recording snapshot error is : " + e.getMessage());
			}
		} else {
			test.pass(strDescription);
		}
	}

	/**
	 * Function Name : wait Description : This function makes execution to wait for
	 * number of seconds
	 * @param intSeconds
	 *            , wait time
	 *            @author Santhosh Karra
	 */
	public void wait(int intSeconds) {
		intSeconds = intSeconds * 1000;
		long waittime = 0;
		waittime = System.currentTimeMillis() + intSeconds;
		while (waittime > System.currentTimeMillis()) {
			// this loop will wait for n secs
		}
	}

	/**
	 * Function Name : log Description : This function reports message into Log file
	 *
	 * @param strMessage
	 *            Message to report on log file
	 *            @author Santhosh Karra
	 */
	public void log(String strMessage) {
		// Getting function name calling from
		String strMethodCalling = Thread.currentThread().getStackTrace()[2].getMethodName();
		if (strMessage.toUpperCase().contains("EXCEPTION")) {
			if (strMessage.substring(strMessage.indexOf("Exception"), strMessage.length()).contains("Exception")) {
				strMessage = strMessage + "NULL";
			}
		}
		if (strMessage.toUpperCase().contains("ISSUE")) {
			stage.getLog().error(strMethodCalling + " : " + strMessage);
		} else {
			stage.getLog().info(strMethodCalling + " : " + strMessage);
		}

		System.out.println(strMethodCalling + " : " + strMessage);
	}

	/**
	 * Function Name : funTakeScreenshot Description : This function captures the
	 * screenshot and returns the image path
	 * @param strImgName
	 *            This refers the screenshot name
	 * @return Returns the image path
	 * @author Santhosh Karra
	 */
	public String funTakeScreenshot(String strImgName) {
		String strFileName = null;
		try {
			Robot robot = new Robot();
			SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy hh mm ss");
			Calendar now = Calendar.getInstance();
			BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			strFileName = strImgName + formatter.format(now.getTime()) + ".jpg";
			strFileName = strFileName.replace(" ", "");
			ImageIO.write(image, "jpg", new File(GetValue("java.error.path") + strFileName));
			// strFileName = new File(initializer.GetValue(
			// "java.error.path")
			// + strFileName).getAbsolutePath();
			strFileName = "../Results/Images/" + strFileName;
		} catch (Exception e) {
			log("Issue on taking snapshot. Exception : " + e.getMessage());
		}
		return strFileName;
	}

	/**
	 * Function Name : finalizeResults Description : finalize the results on failure
	 * step and terminate the execution
	 * @author Santhosh Karra
	 */
	public void finalizeResults() {
		boolean bExpected = true;
		Assert.assertEquals(stage.getStatus(), bExpected);
	}

}
