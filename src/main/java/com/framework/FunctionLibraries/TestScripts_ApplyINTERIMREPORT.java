package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_ApplyINTERIMREPORT extends BaseClass {
	public ExtentTest test = null;
	private Apply_INTERIM_REPORT_ApplicationFunctions intrep = null;
	private CommonFunctions commonFunctions = null;
	private Stage stage = null;
	private Initializer initializer = null;

		public TestScripts_ApplyINTERIMREPORT(CommonFunctions commonFunctions, Apply_INTERIM_REPORT_ApplicationFunctions intrep,Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.intrep = intrep;
		this.initializer = initializer;
		this.stage = stage;
	}
	public void init(ExtentTest test) {
		this.test = test;
	}
	
	public void Interim_REPORT_NO_TC() {
		initializer.infoCall("IR NO TC___________________.");
		intrep.launchURL(initializer.GetValue("app.url"));
		intrep.loginButton();
		intrep.emailPassword_ValidationMsg();
		intrep.emailPassword();
		intrep.loginFormButton();
		intrep.startInterimButton();
		
        intrep.continue_Option();
        
        intrep.interimContact_No();      
        intrep.continue_Option();
        
        intrep.household_No();
        intrep.continue_Option();
        
        intrep.earning_No();       
        intrep.continue_Option();
        
        intrep.unearned_No();
        intrep.continue_Option();
        
        intrep.childSupport_No();    
        intrep.continue_Option();
        
        intrep.otherinfo_No();
        intrep.continue_Option();
        
        intrep.signSubmitValidationMsg();
        intrep.signSubmit();

	}
	
	public void Interim_REPORT_YES_TC() {
		initializer.infoCall("IR YES TC___________________.");
		intrep.launchURL(initializer.GetValue("app.url"));
		intrep.loginButton();
		intrep.emailPassword_ValidationMsg();
		intrep.emailPassword();
		intrep.loginFormButton();
		intrep.startInterimButton(); 
                  
        intrep.continue_Option();
		  
        intrep.interimContact_Yes();
        intrep.interimContactEdit2();
        intrep.interimPhone();
        intrep.interim_message_alert_Yes();
        intrep.interimContactDone2();
        
        intrep.interimContactEdit3();
        intrep.email();
        intrep.interim_email_notification_Yes();
        intrep.email1();
        intrep.interimContactDone3();
        
        intrep.continue_Option();

        intrep.household_Yes();
        intrep.addhouseholdmember();
        intrep.continue_Option();
	    intrep.fnlndob_ValidationMsg();
        intrep.firstLast_Name();
        intrep.middle_Name();
        intrep.suffix_Name();
        intrep.dateOfBirth();
        intrep.sSN_Option();
        intrep.son_AboutMember();
        intrep.male_Option();
        intrep.uscitizen_Yes();
        intrep.food_Yes();
        intrep.income_Yes();
        intrep.continue_Option();
                
        intrep.continue_Option();
        intrep.earned_Yes();
        intrep.interimEarnedEdit();
                
        intrep.self_Employment();
        intrep.workStudy();
        intrep.continue_Option();
       
        intrep.addanotherwages();  
        intrep.one_Amount2_Wages_Quarterly();
        intrep.addanotherwages();
        intrep.one_Amount3_Wages_Weekly();
                
        intrep.addanother_Selfemployment();
        intrep.two_Amount1_Selfemp_Semiannual();
        intrep.addanother_Selfemployment();
        intrep.two_Amount2_Selfemp_Annual();
        intrep.addanother_Selfemployment();
        intrep.two_Amount3_Selfemp_Quarterly();

        intrep.addanother_Workstudy();
        intrep.three_Amount1_Workstudy_Monthly();
        intrep.addanother_Workstudy();
        intrep.three_Amount2_Workstudy_Weekly();
        intrep.addanother_Workstudy();
        intrep.three_Amount3_Workstudy_Quarterly();
        intrep.continue_Option();   
	      
        intrep.continue_Option(); 
	      
        intrep.unearned_Yes();
        intrep.addincome();
        intrep.addnameunearned();
        intrep.unemployment_IncomeAndBenefits();
        intrep.pension_IncomeAndBenefits();
        intrep.veteransBenefits_IncomeAndBenefits();
        intrep.continue_Option();
        
        intrep.one_Amount1_Unemployment_Annual();
        intrep.addanotherunemployment();
        intrep.one_Amount2_Unemployment_Weekly();
        
        intrep.one_Amount1_pension_Monthly();
        intrep.addanotherpension();
        intrep.one_Amount2_pension_Quarterly();
        
        intrep.one_Amount1_veteranbenefit_Annual();
        intrep.continue_Option();
     
        intrep.continue_Option();
          
        intrep.childSupport_Yes();    
        intrep.addchildsuppot();
        intrep.addnamechildsupport();
        intrep.one_Amount1_child_Monthly();
        intrep.interimchilddone();
        intrep.continue_Option();
        
        intrep.otherinfo_Yes();
        intrep.continue_Option();
        
        intrep.signSubmitValidationMsg();
        intrep.signSubmit();

	}
	
	public void Interim_REPORT_LANG() {
		initializer.infoCall("IR LANG TC___________________.");
		intrep.launchURL(initializer.GetValue("app.url"));
		intrep.loginButton();
		intrep.emailPassword_ValidationMsg();
		intrep.emailPassword();
		intrep.loginFormButton();
		intrep.languageSelect();
		intrep.spanishLanguage();
		
        intrep.continue_Option();
        
        intrep.interimContact_No();      
        intrep.continue_Option();
        
        intrep.household_No();
        intrep.continue_Option();
        
        intrep.earning_No();       
        intrep.continue_Option();
        
        intrep.unearned_No();
        intrep.continue_Option();
        
        intrep.childSupport_No();    
        intrep.continue_Option();
        
        intrep.otherinfo_No();
        intrep.continue_Option();

        intrep.signSubmit_ES();

	}
}
