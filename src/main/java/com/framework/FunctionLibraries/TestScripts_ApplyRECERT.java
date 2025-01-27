package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_ApplyRECERT extends BaseClass {
	public ExtentTest test = null;
	private Apply_RECERT_ApplicationFunctions recert = null;
//	private CommonFunctions commonFunctions = null;
//	private Stage stage = null;
	private Initializer initializer = null;

		public TestScripts_ApplyRECERT(CommonFunctions commonFunctions, Apply_RECERT_ApplicationFunctions recert,Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.recert = recert;
		this.initializer = initializer;
		this.stage = stage;
	}
	public void init(ExtentTest test) {
		this.test = test;
	}
	
	public void Apply_RECERT() {
		recert.launchURL(initializer.GetValue("app.url"));
		recert.loginButton();
	//	recert.emailPassword_ValidationMsg();
		recert.emailPassword();
		recert.loginFormButton();
		
		recert.startRecertificationButton();

	}
}