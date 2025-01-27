package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_ApplyProviderLOGIN extends BaseClass{
	public ExtentTest test = null;
	private Apply_ProviderLOGIN_ApplicationFunctions login = null;
	private CommonFunctions commonFunctions = null;
	private Stage stage = null;
	private Initializer initializer = null;


	public TestScripts_ApplyProviderLOGIN(CommonFunctions commonFunctions, Apply_ProviderLOGIN_ApplicationFunctions login,
			Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.login = login;
		this.initializer = initializer;
		this.stage = stage;
	}


	/**
	 * This method initialize the test object in testScripts library
	 *
	 * @param test This is extentTest object, ExtentTest is used for report
	 *             generation.
	 * @author sushmitha
	 */
	public void init(ExtentTest test) {
		this.test = test;
	}

	/**
	 * @author sushmitha
	 */
	
	public void provider_LOGIN() {
		login.launchURL(initializer.GetValue("app.url"));
		login.loginButton();
		login.emailPassword_ValidationMsg();
		login.emailPassword();
		login.loginFormButton();
		login.chooseWebApplication();
		login.chooseAgencyId();
		login.chooseSSN();
		login.chooseEBTcard();
		login.providerLogout();
		login.loginButton();
		login.emailPassword_ValidationMsg();
		login.superProviderEmailPassword();
		login.loginFormButton();
		login.chooseWebApplication();
		login.chooseAgencyId();
		login.chooseSSN();
		login.chooseEBTcard();
		login.providerLogout1();
		
		
	}

}
