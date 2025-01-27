package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_ApplyCovidRECERT extends BaseClass {
	public ExtentTest test = null;
	private Apply_COVID_RECERT_ApplicationFunctions covidrecert = null;
	private CommonFunctions commonFunctions = null;
	private Stage stage = null;
	private Initializer initializer = null;

		public TestScripts_ApplyCovidRECERT(CommonFunctions commonFunctions, Apply_COVID_RECERT_ApplicationFunctions covidrecert,Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.covidrecert = covidrecert;
		this.initializer = initializer;
		this.stage = stage;
	}
	public void init(ExtentTest test) {
		this.test = test;
	}
	
	public void Apply_COVIDRECERT_NO_TC() {
		covidrecert.launchURL(initializer.GetValue("app.url"));
		covidrecert.loginButton();
		covidrecert.emailPassword_ValidationMsg();
		covidrecert.emailPassword();
		covidrecert.loginFormButton();
		
		covidrecert.startcovidrecertButton();
		covidrecert.startcovidrecertButton();
		
		covidrecert.covidrecertContact_No();
		covidrecert.continue_Option();
		
		covidrecert.household_No();
		covidrecert.continue_Option();
		
		/*
		 * covidrecert.student_None(); covidrecert.continue_Option();
		 * 
		 * covidrecert.sheltercost_None(); covidrecert.continue_Option();
		 * 
		 * covidrecert.utilitycost_None(); covidrecert.continue_Option();
		 * 
		 * covidrecert.medicalcost_None(); covidrecert.continue_Option();
		 * 
		 */covidrecert.continue_Option();
		covidrecert.signSubmitValidationMsg();
		covidrecert.signSubmit();
	}
	
	
	public void Apply_COVIDRECERT() {
		covidrecert.launchURL(initializer.GetValue("app.url"));
		covidrecert.loginButton();
		covidrecert.emailPassword_ValidationMsg();
		covidrecert.emailPassword();
		covidrecert.loginFormButton();
		covidrecert.startcovidrecertButton(); 
                  
        covidrecert.continue_Option();
		  
        covidrecert.covidrecertContact_Yes();
        covidrecert.covidrecertContactEdit2();
        covidrecert.covidrecertPhone();
        covidrecert.covidrecert_message_alert_Yes();
        covidrecert.covidrecertContactDone2();
        
        covidrecert.covidrecertContactEdit3();
        covidrecert.email();
        covidrecert.covidrecert_email_notification_Yes();
        covidrecert.email1();
        covidrecert.covidrecertContactDone3();
        
        covidrecert.continue_Option();

        covidrecert.household_Yes();
        covidrecert.addhouseholdmember();
        covidrecert.continue_Option();
	    covidrecert.fnlndob_ValidationMsg();
        covidrecert.firstLast_Name();
        covidrecert.middle_Name();
        covidrecert.suffix_Name();
        covidrecert.dateOfBirth();
        covidrecert.sSN_Option();
        covidrecert.son_AboutMember();
        covidrecert.male_Option();
        covidrecert.uscitizen_Yes();
        covidrecert.food_Yes();
        covidrecert.income_Yes();
        covidrecert.continue_Option();
                
        covidrecert.continue_Option();
        covidrecert.earned_Yes();
        covidrecert.covidrecertEarnedEdit();
                
        covidrecert.self_Employment();
        covidrecert.workStudy();
        covidrecert.americanojt();
        covidrecert.continue_Option();
                
        covidrecert.addanother_Selfemployment();
        covidrecert.two_Amount1_Selfemp_Semiannual();
        covidrecert.addanother_Selfemployment();
        covidrecert.two_Amount2_Selfemp_Annual();
        covidrecert.addanother_Selfemployment();
        covidrecert.two_Amount3_Selfemp_Quarterly();

        covidrecert.addanother_Workstudy();
        covidrecert.three_Amount1_Workstudy_Monthly();
        covidrecert.addanother_Workstudy();
        covidrecert.three_Amount2_Workstudy_Weekly();
        covidrecert.addanother_Workstudy();
        covidrecert.three_Amount3_Workstudy_Quarterly();
        covidrecert.continue_Option();   
	      
        covidrecert.continue_Option(); 
	      
        covidrecert.unearned_Yes();
        covidrecert.addincome();
        covidrecert.addnameunearned();
        covidrecert.unemployment_IncomeAndBenefits();
        covidrecert.pension_IncomeAndBenefits();
        covidrecert.veteransBenefits_IncomeAndBenefits();
        covidrecert.continue_Option();
        
        covidrecert.one_Amount1_Unemployment_Annual();
        covidrecert.addanotherunemployment();
        covidrecert.one_Amount2_Unemployment_Weekly();
        
        covidrecert.one_Amount1_pension_Monthly();
        covidrecert.addanotherpension();
        covidrecert.one_Amount2_pension_Quarterly();
        
        covidrecert.one_Amount1_veteranbenefit_Annual();
        covidrecert.continue_Option();
     
        covidrecert.continue_Option();
          
        covidrecert.childSupport_Yes();    
        covidrecert.addchildsuppot();
        covidrecert.addnamechildsupport();
        covidrecert.one_Amount1_child_Monthly();
        covidrecert.covidrecertchilddone();
        covidrecert.continue_Option();
        covidrecert.otherinfo_No();
        covidrecert.continue_Option();
        covidrecert.signSubmitValidationMsg();
        covidrecert.signSubmit();
	}
}