package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_ApplyRECERT extends BaseClass {
	public ExtentTest test = null;
//	private Apply_RECERT_ApplicationFunctions recert = null;
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
	
	public void Apply_RECERT_NO_TC() {
		initializer.infoCall("RECERT NO TC___________________.");
		recert.launchURL(initializer.GetValue("app.url"));
		recert.loginButton();
		recert.emailPassword_ValidationMsg();
		recert.emailPassword();
		recert.loginFormButton();
		
		recert.startRecertificationButton();
		recert.startRecertificationButton();
		
		recert.recertContact_No();
		recert.continue_Option();
		
		recert.recertHousehold_No();
		recert.continue_Option();
		recert.yes_Option();
		recert.continue_Option();
		recert.student_None();
		recert.continue_Option();
			
		recert.sheltercost_None();
		recert.continue_Option();
		
		recert.utilitycost_None();
		recert.continue_Option();
		
		recert.signSubmitValidationMsg();
		recert.signSubmit();
	}
	
	
	public void Apply_RECERT_YES_TC() {
		initializer.infoCall("RECERT YES TC___________________.");
		recert.launchURL(initializer.GetValue("app.url"));
		recert.loginButton();
		recert.emailPassword_ValidationMsg();
		recert.emailPassword();
		recert.loginFormButton();
		
		recert.startRecertificationButton();
		recert.startRecertificationButton();
		
		recert.recertContact_Yes();
		recert.recertContactEdit2();
		recert.recertPhone();
		recert.recert_message_alert_Yes();
		
		recert.recertContactEdit3();
		recert.email();
		recert.recert_email_notification_Yes();

		
		recert.continue_Option();
		
		recert.recertHousehold_Yes();
		recert.addhouseholdmember();
		recert.continue_Option();
		recert.fnlndob_ValidationMsg();
		recert.firstLast_Name();
		recert.middle_Name();
		recert.suffix_Name();
		recert.dateOfBirth();
		recert.sSN_Option();
		recert.son_AboutMember();
		recert.male_Option();
		recert.uscitizen_Yes();
		recert.food_Yes();
		recert.income_Yes();		
		recert.continue_Option();
		recert.yes_Option();
		recert.continue_Option();
		
		recert.yes_Option();
		recert.continue_Option();
		
		recert.student();
		recert.continue_Option();
		recert.student_dtl();
		recert.continue_Option();

		recert.dependent();
		recert.continue_Option();
		recert.dependent_dtl();
		recert.continue_Option();
		recert.Address_Phone_ValidationMsg();
		recert.address();
		recert.continue_Option();
		recert.Address_Phone_ValidationMsg();
		recert.address();
		recert.continue_Option();
		
		recert.sheltercost();
		recert.continue_Option();
		recert.continue_Option();
		
		recert.utilitycost();
		recert.continue_Option();
		
//		recert.medicalcost();
//		recert.continue_Option();		
//		recert.medicalcost_dtl1();
//		recert.continue_Option();
//		recert.Address_Phone_ValidationMsg();
//		recert.address();
//		recert.medicalcost_dtl2();
		
		recert.recertSubmitButton();
		recert.signSubmitValidationMsg();
		recert.signSubmit();
	}
	
	public void Apply_RECERT_LANG_TC() {
		initializer.infoCall("RECERT LANG TC___________________.");
		recert.launchURL(initializer.GetValue("app.url"));
		recert.loginButton();
		recert.emailPassword_ValidationMsg();
		recert.emailPassword();
		recert.loginFormButton();
		recert.languageSelect();
		
		recert.portugueseLanguage();
		recert.portugueseLanguage();
		
		recert.pr_recertContact_No();
		recert.continue_Option();
		
		recert.pr_recertHousehold_No();
		recert.continue_Option();
		
		recert.pr_no_Option();
		recert.continue_Option();
		
		recert.pr_student_None();
		recert.continue_Option();
			
		recert.continue_Option();
		
		recert.continue_Option();
		
		recert.signSubmit_PR();
	}
}