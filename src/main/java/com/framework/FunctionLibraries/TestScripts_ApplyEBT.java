package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_ApplyEBT extends BaseClass {
	public ExtentTest test = null;
	private Apply_EBT_ApplicationFunctions ebt = null;
//	private CommonFunctions commonFunctions = null;
//	private Stage stage = null;
	private Initializer initializer = null;

	public TestScripts_ApplyEBT() {
	}

	public TestScripts_ApplyEBT(CommonFunctions commonFunctions, Apply_EBT_ApplicationFunctions ebt,Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.ebt = ebt;
		this.initializer = initializer;
		this.stage = stage;
	}
	public void init(ExtentTest test) {
		this.test = test;
	}
	
	public void Apply_EBT() {
		ebt.launchURL(initializer.GetValue("app.url"));
		ebt.applyEBTButton();
		
		ebt.change_pebt();
		ebt.continue_Option();
		ebt.enter_ebt();
		ebt.continue_Option();
		ebt.goBack_Option();
		
		ebt.replace_pebt();
		ebt.continue_Option();

		ebt.first_Last_Name();
		ebt.middle_Name();
		ebt.dateOfBirth_Minor();

		ebt.Address_Phone_ValidationMsg();
		ebt.address();
		ebt.phone();
		ebt.message_alert_Yes();
		ebt.continue_Option();
		ebt.close_page();
//------------ original test data
		ebt.replace_pebt();
		ebt.continue_Option();

		ebt.first_Last_Name1();
		ebt.middle_Name1();
		ebt.dateOfBirth_Minor1();
		
		ebt.Address_Phone_ValidationMsg1();
		ebt.address1();
		ebt.phone1();
		ebt.message_alert_No1();
		ebt.continue_Option();
		ebt.close_page();
		
	}
}