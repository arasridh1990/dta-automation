package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_CreateONLINE {
	public ExtentTest test = null;
	private Create_ONLINE_ApplicationFunctions online = null;
	private CommonFunctions commonFunctions = null;
	private Stage stage = null;
	private Initializer initializer = null;

	public TestScripts_CreateONLINE(CommonFunctions commonFunctions,  Create_ONLINE_ApplicationFunctions online,
			Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.online = online;
		this.initializer = initializer;
		this.stage = stage;
	}

	/**
	 * This method initialize the test object in testScripts library
	 *
	 * @param test This is extentTest object, ExtentTest is used for report
	 *             generation.
	 * @author Santhosh Karra
	 */
	public void init(ExtentTest test) {
		this.test = test;
	}

	/**
	 * @author Santhosh Karra
	 */
	
	public void Create_ONLINE() {
		online.launchURL(initializer.GetValue("app.url"));
		online.createOnlineButton();
		online.signUp();
		online.email_ValidationMsg();
	}
}
