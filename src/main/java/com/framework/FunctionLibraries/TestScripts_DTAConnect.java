package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_DTAConnect extends BaseClass {
	public ExtentTest test = null;
	private DTAConnect_ApplicationFunctions dta = null;
//	private CommonFunctions commonFunctions = null;
//	private Stage stage = null;
	private Initializer initializer = null;

	public TestScripts_DTAConnect() {
	}

	public TestScripts_DTAConnect(CommonFunctions commonFunctions, DTAConnect_ApplicationFunctions dta,
			Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.dta = dta;
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

	public void Online_Account() {
		dta.launchURL(initializer.GetValue("app.url"));
		dta.createYourOnlineAccountButton();
		dta.continue_Option();
		dta.email_ValidationMsg();
		dta.firstName_ValidationMsg();
		dta.lastName_ValidationMsg();
		dta.dateofbirth_ValidationMsg();
		dta.email();
		dta.firstLast_Name();
		dta.dateOfBirth();
		dta.createAccountButton();
	}
}


