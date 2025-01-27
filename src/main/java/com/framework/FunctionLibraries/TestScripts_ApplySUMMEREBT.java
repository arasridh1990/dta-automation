package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_ApplySUMMEREBT extends BaseClass {
	public ExtentTest test = null;
//	private DTAConnect_ApplicationFunctions dta = null;
	private Apply_SUMMEREBT_ApplicationFunctions sebt = null;
//	private Apply_CASH_ApplicationFunctions cash = null;

//	private CommonFunctions commonFunctions = null;
//	private Stage stage = null;
	private Initializer initializer = null;

	public TestScripts_ApplySUMMEREBT() {
	}

//	public TestScripts_ApplySNAP(CommonFunctions commonFunctions, DTAConnect_ApplicationFunctions dta,
	//Apply_SNAP_ApplicationFunctions snap,Apply_CASH_ApplicationFunctions cash,
//			Initializer initializer, Stage stage)
	public TestScripts_ApplySUMMEREBT(CommonFunctions commonFunctions, Apply_SUMMEREBT_ApplicationFunctions sebt,
			Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.sebt = sebt;
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

	public void ApplySUMMEREBT_PUBLIC_SCHOOL_TC() {

		sebt.launchURL(initializer.GetValue("app.url"));
		initializer.infoCall("SUMMEREBT PUBLIC SCHOOL TC___________________.");
		//sebt.app_Down();
		sebt.applySUMMEREBTButton();
		
		// *********************Before you start**************************************************************
 		
		sebt.continue_Option();
		
		// *********************About me*********************************************************************
		sebt.firstLast_Name();
		sebt.middle_Name();
		sebt.suffix_Name();
		sebt.dateOfBirth_Major();
		sebt.language();
		sebt.continue_Option();
		
		//*********************More about me****************************************************************
		sebt.female_Option();
		//sebt.male_Option();
		sebt.hispanic_MoreAboutMe();
		//sebt.non_Hispanic_MoreAboutMe();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
		
		//********************My contact info page 1****************************************************************
		sebt.no_Option();
		sebt.address();
		sebt.yes_MailAddress_MyContactInfo();
		sebt.continue_Option();
		
		//*******************My contact info page 2***********************************************************
		sebt.phone_MyContactInfo();
		sebt.phoneNotificationY();
		sebt.email_MyContactInfo();
		sebt.emailNotificationY();
		sebt.continue_Option();
		
		//******************About my household*********************************************************************
		sebt.continue_Option();
		sebt.one_Option();
		sebt.continue_Option();		
		//******************About Student 1********************************************************************
		sebt.firstLast_Name2();
		sebt.father_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		sebt.female_Option();
		sebt.hispanic_MoreAboutMe();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
		
		//*******************School enrollment for Student 1********************************************************
		sebt.typeSchoolPublicChart();
		sebt.schoolDistrict();
		sebt.schoolName();
		//sebt.typeSchoolPrivate();
		//sebt.typeSchoolNotEnrolled();
		sebt.studentIDNumber();
		sebt.continue_Option();
		
		//******************About my household*********************************************************************
		sebt.one_Option();
		sebt.continue_Option();
		
		//*****************About your household benefits and income information*************************************
		sebt.snapBenefitsY();
		sebt.agencyID();
		sebt.massHealthY();
		sebt.massHealthID();
		sebt.continue_Option();
		sebt.noOne_Option();
		sebt.continue_Option();
		
		//*****************Income summary*************************************
		sebt.continue_Option();
		
		//*****************Sign & Submit*************************************
		sebt.signSubmitValidationMsg();
		sebt.sebtSignSubmit();
		sebt.exitWithoutUploading();	
	}
	public void ApplySUMMEREBT_PRIVATE_SCHOOL_TC() {

		sebt.launchURL(initializer.GetValue("app.url"));
		// snap.app_Down();
		initializer.infoCall("SUMMEREBT PRIVATE SCHOOL TC___________________.");
		sebt.applySUMMEREBTButton();
		
		// *********************Before you start**************************************************************
 		sebt.continue_Option();
		// *********************About me*********************************************************************
		sebt.firstLast_Name();
		sebt.middle_Name();
		sebt.suffix_Name();
		sebt.dateOfBirth_Major();
		sebt.language();
		sebt.continue_Option();
		
		//*********************More about me****************************************************************
		sebt.female_Option();
		//sebt.male_Option();
		//sebt.hispanic_MoreAboutMe();
		sebt.non_Hispanic_MoreAboutMe();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
		
		//********************My contact info page 1****************************************************************
		sebt.no_Option();
		sebt.address();
		sebt.yes_MailAddress_MyContactInfo();
		sebt.continue_Option();
		
		//*******************My contact info page 2***********************************************************
		sebt.phone_MyContactInfo();
		sebt.phoneNotificationY();
		sebt.email_MyContactInfo();
		sebt.emailNotificationY();
		sebt.continue_Option();
		
		//******************About my household*********************************************************************
		sebt.continue_Option();
		sebt.one_Option();
		sebt.continue_Option();		
		sebt.firstLast_Name2();
		sebt.father_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		sebt.female_Option();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
		
		//*******************School enrollment for Abey Rosalas********************************************************
		//sebt.typeSchoolPublicChart();
		//sebt.schoolDistrict();
		//sebt.schoolName();
		sebt.typeSchoolPrivate();
		sebt.schoolCity();
		sebt.pvtSchoolName();
		//sebt.typeSchoolNotEnrolled();
		//sebt.studentIDNumber();
		sebt.continue_Option();
		
		//******************About my household*********************************************************************
		sebt.one_Option();
		sebt.continue_Option();
		
		//*****************About your household benefits and income information*************************************
		sebt.snapBenefitsY();
		sebt.agencyID();
		sebt.massHealthY();
		sebt.massHealthID();
		sebt.continue_Option();
		sebt.noOne_Option();
		sebt.continue_Option();
		
		//*****************Income summary*************************************
		sebt.continue_Option();
		
		//*****************Sign & Submit*************************************
		sebt.signSubmitValidationMsg();
		sebt.sebtSignSubmit();
		sebt.exitWithoutUploading();
	}

	public void ApplySUMMEREBT_NOT_ENROLLED_TC() {

		sebt.launchURL(initializer.GetValue("app.url"));
		// snap.app_Down();
		initializer.infoCall("SUMMEREBT NOT ENROLLED TC___________________.");
		sebt.applySUMMEREBTButton();
	
		// *********************Before you start**************************************************************
		sebt.continue_Option();
		// *********************About me*********************************************************************
		sebt.firstLast_Name();
		sebt.middle_Name();
		sebt.suffix_Name();
		sebt.dateOfBirth_Major();
		sebt.language();
		sebt.continue_Option();
	
		//*********************More about me****************************************************************
		//sebt.female_Option();
		sebt.male_Option();
		//sebt.hispanic_MoreAboutMe();
		sebt.non_Hispanic_MoreAboutMe();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
	
		//********************My contact info page 1****************************************************************
		sebt.no_Option();
		sebt.address();
		sebt.yes_MailAddress_MyContactInfo();
		sebt.continue_Option();
	
		//*******************My contact info page 2***********************************************************
		sebt.phone_MyContactInfo();
		sebt.phoneNotificationY();
		sebt.email_MyContactInfo();
		sebt.emailNotificationY();
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.continue_Option();
		sebt.one_Option();
		sebt.continue_Option();		
		sebt.firstLast_Name2();
		sebt.father_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		sebt.female_Option();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
	
		//*******************School enrollment for Abey Rosalas********************************************************
		//sebt.typeSchoolPublicChart();
		//sebt.schoolDistrict();
		//sebt.schoolName();
		//sebt.typeSchoolPrivate();
		//sebt.schoolCity();
		//sebt.pvtSchoolName();
		sebt.typeSchoolNotEnrolled();
		//sebt.studentIDNumber();
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.one_Option();
		sebt.continue_Option();
	
		//*****************About your household benefits and income information*************************************
		sebt.snapBenefitsY();
		sebt.agencyID();
		sebt.massHealthY();
		sebt.massHealthID();
		sebt.continue_Option();
		sebt.noOne_Option();
		sebt.continue_Option();
	
		//*****************Income summary*************************************
		sebt.continue_Option();
	
		//*****************Sign & Submit*************************************
		sebt.signSubmitValidationMsg();
		sebt.sebtSignSubmit();
		sebt.exitWithoutUploading();
	}
	public void ApplySUMMEREBT_INCLUDE_INCOME_TC() {

		sebt.launchURL(initializer.GetValue("app.url"));
		// snap.app_Down();
		initializer.infoCall("SUMMEREBT NOT ENROLLED TC___________________.");
		sebt.applySUMMEREBTButton();
	
		// *********************Before you start**************************************************************
		sebt.continue_Option();
		// *********************About me*********************************************************************
		sebt.firstLast_Name();
		sebt.middle_Name();
		sebt.suffix_Name();
		sebt.dateOfBirth_Major();
		sebt.language();
		sebt.continue_Option();
	
		//*********************More about me****************************************************************
		sebt.female_Option();
		sebt.male_Option();
		sebt.hispanic_MoreAboutMe();
		sebt.non_Hispanic_MoreAboutMe();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
	
		//********************My contact info page 1****************************************************************
		sebt.no_Option();
		sebt.address();
		sebt.yes_MailAddress_MyContactInfo();
		sebt.continue_Option();
	
		//*******************My contact info page 2***********************************************************
		sebt.phone_MyContactInfo();
		sebt.phoneNotificationY();
		sebt.email_MyContactInfo();
		sebt.emailNotificationY();
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.continue_Option();
		sebt.one_Option();
		sebt.continue_Option();		
		sebt.firstLast_Name2();
		sebt.father_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		sebt.female_Option();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
	
		//*******************School enrollment for Abey Rosalas********************************************************
		sebt.typeSchoolPublicChart();
		sebt.schoolDistrict();
		sebt.schoolName();
		//sebt.typeSchoolPrivate();
		//sebt.schoolCity();
		//sebt.pvtSchoolName();
		//sebt.typeSchoolNotEnrolled();
		sebt.studentIDNumber();
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.one_Option();
		sebt.continue_Option();
	
		//*****************About your household benefits and income information*************************************
		sebt.snapBenefitsY();
		sebt.agencyID();
		sebt.massHealthY();
		sebt.massHealthID();
		sebt.continue_Option();
		sebt.grantee_Option();
		//sebt.noOne_Option();
		sebt.continue_Option();
		sebt.wages_IncomeAndBenefits();
		sebt.veteransBenefits_IncomeAndBenefits();
		sebt.childSupport_IncomeAndBenefits();
		sebt.continue_Option();

		sebt.one_Amount1_EveryTwoWeeks();
		sebt.add_Wage();
		sebt.one_Amount2_Week();
		sebt.two_Amount1_EveryTwoWeeks();
		sebt.three_Amount1_Week();
		sebt.continue_Option();
		
		//*****************Income summary*************************************
		sebt.continue_Option();
	
		//*****************Sign & Submit*************************************
		sebt.signSubmitValidationMsg();
		sebt.sebtSignSubmit();
		//sebt.exitWithoutUploading();
	}
	public void ApplySUMMEREBT_MANDATORY_FIELDS_ONLY_TC() {

		sebt.launchURL(initializer.GetValue("app.url"));
		// snap.app_Down();
		initializer.infoCall("SUMMEREBT MANDATORY FIELDS ONLY TC___________________.");
		sebt.applySUMMEREBTButton();
	
		// *********************Before you start**************************************************************
		sebt.continue_Option();
		// *********************About me*********************************************************************
		sebt.firstLast_Name();
		sebt.dateOfBirth_Major();
		sebt.continue_Option();
	
		//*********************More about me****************************************************************
		sebt.continue_Option();
	
		//********************My contact info page 1****************************************************************
		sebt.address();
		sebt.continue_Option();
	
		//*******************My contact info page 2***********************************************************
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.continue_Option();
		sebt.one_Option();
		sebt.continue_Option();		
		sebt.firstLast_Name2();
		sebt.father_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		sebt.continue_Option();
	
		//*******************School enrollment for Abey Rosalas********************************************************
		sebt.typeSchoolPublicChart();
		sebt.schoolDistrict();
		sebt.schoolName();
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.one_Option();
		sebt.continue_Option();
	
		//*****************About your household benefits and income information*************************************
		sebt.continue_Option();
		sebt.noOne_Option();
		sebt.continue_Option();
		sebt.continue_Option();
		
		//*****************Sign & Submit*************************************
		//sebt.signSubmitValidationMsg();
		sebt.sebtSignSubmit_NoDownload();
		sebt.exitWithoutUploading();
	}

	public void ApplySUMMEREBT_ADDITIONAL_ADULT_HH_MEMB_TC() {
		sebt.launchURL(initializer.GetValue("app.url"));
		// snap.app_Down();
		initializer.infoCall("SUMMEREBT ADDITIONAL HOUSEHOLD MEMBER TC___________________.");
		sebt.applySUMMEREBTButton();
	
		// *********************Before you start**************************************************************
		sebt.continue_Option();
		// *********************About me*********************************************************************
		sebt.firstLast_Name();
		sebt.middle_Name();
		sebt.suffix_Name();
		sebt.dateOfBirth_Major();
		sebt.language();
		sebt.continue_Option();
	
		//*********************More about me****************************************************************
		sebt.female_Option();
		sebt.male_Option();
		//sebt.hispanic_MoreAboutMe();
		sebt.non_Hispanic_MoreAboutMe();
		sebt.asian_MoreAboutMe();
		sebt.continue_Option();
	
		//********************My contact info page 1****************************************************************
		sebt.no_Option();
		sebt.address();
		sebt.yes_MailAddress_MyContactInfo();
		sebt.continue_Option();
	
		//*******************My contact info page 2***********************************************************
		sebt.phone_MyContactInfo();
		sebt.phoneNotificationY();
		sebt.email_MyContactInfo();
		sebt.emailNotificationY();
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.continue_Option();
		sebt.one_Option();
		sebt.continue_Option();		
		sebt.firstLast_Name2();
		sebt.father_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		sebt.female_Option();
		sebt.asian_MoreAboutMe();
		sebt.continue_Option();
	
		//*******************School enrollment for Abey Rosalas********************************************************
		//sebt.typeSchoolPublicChart();
		//sebt.schoolDistrict();
		//sebt.schoolName();
		sebt.typeSchoolPrivate();
		sebt.schoolCity();
		sebt.pvtSchoolName();
		//sebt.typeSchoolNotEnrolled();
		//sebt.studentIDNumber();
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.two_Option();
		sebt.continue_Option();
		
		//******************About Adult 1***************************************************************************
		sebt.firstLast_Name3();
		sebt.middle_Name();
		sebt.suffix_Name();
		sebt.continue_Option();
		sebt.male_Option();
		sebt.non_Hispanic_MoreAboutMe();
		sebt.black_American_MoreAboutMe();
		sebt.continue_Option();	
	
		//*****************About your household benefits and income information*************************************
		sebt.snapBenefitsY();
		sebt.agencyID();
		sebt.massHealthY();
		sebt.massHealthID();
		sebt.continue_Option();
		sebt.grantee_Option();
		//sebt.noOne_Option();
		sebt.continue_Option();
		sebt.wages_IncomeAndBenefits();
		sebt.veteransBenefits_IncomeAndBenefits();
		sebt.childSupport_IncomeAndBenefits();
		sebt.continue_Option();

		sebt.one_Amount1_EveryTwoWeeks();
		sebt.add_Wage();
		sebt.one_Amount2_Week();
		sebt.two_Amount1_EveryTwoWeeks();
		sebt.three_Amount1_Week();
		sebt.continue_Option();
		
		//*****************Income summary*************************************
		sebt.continue_Option();
	
		//*****************Sign & Submit*************************************
		sebt.signSubmitValidationMsg();
		sebt.sebtSignSubmit();
		//sebt.exitWithoutUploading();
	}
	
	public void ApplySUMMEREBT_MULTIPLE_STUDENTS_TC() {

		sebt.launchURL(initializer.GetValue("app.url"));
		initializer.infoCall("SUMMEREBT MULTIPLE STUDENTS TC___________________.");
		//sebt.app_Down();
		sebt.applySUMMEREBTButton();
		
		// *********************Before you start**************************************************************
 		
		sebt.continue_Option();
		
		// *********************About me*********************************************************************
		sebt.firstLast_Name();
		sebt.middle_Name();
		sebt.suffix_Name();
		sebt.dateOfBirth_Major();
		sebt.language();
		sebt.continue_Option();
		
		//*********************More about me****************************************************************
		sebt.female_Option();
		//sebt.male_Option();
		sebt.hispanic_MoreAboutMe();
		//sebt.non_Hispanic_MoreAboutMe();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
		
		//********************My contact info page 1****************************************************************
		sebt.no_Option();
		sebt.address();
		sebt.yes_MailAddress_MyContactInfo();
		sebt.continue_Option();
		
		//*******************My contact info page 2***********************************************************
		sebt.phone_MyContactInfo();
		sebt.phoneNotificationY();
		sebt.email_MyContactInfo();
		sebt.emailNotificationY();
		sebt.continue_Option();
		
		//******************About my household*********************************************************************
		sebt.continue_Option();
		sebt.three_Option();
		sebt.continue_Option();	
		
		//******************About Student 1*******************************************************************
		sebt.firstLast_Name2();
		sebt.mother_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		//********************More about Student 1*************************************************************
		sebt.female_Option();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
		//*******************School enrollment for Student 1********************************************************
		sebt.typeSchoolPublicChart();
		sebt.schoolDistrict();
		sebt.schoolName();
		//sebt.typeSchoolPrivate();
		//sebt.typeSchoolNotEnrolled();
		sebt.studentIDNumber();
		sebt.continue_Option();
		
		//*******************About Student 2********************************************************
		sebt.firstLast_Name3();
		sebt.mother_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		//********************More about Student 2*************************************************************
		sebt.male_Option();
		sebt.decline_Ethnicity_MoreAboutMe();
		sebt.decline_Race_MoreAboutMe();
		sebt.continue_Option();
		//*******************School enrollment for Student 2********************************************************
		sebt.typeSchoolPrivate();
		sebt.schoolCity();
		sebt.pvtSchoolName();
		sebt.continue_Option();
		
		//*******************About Student 3********************************************************
		sebt.firstLast_Name4();
		sebt.mother_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		//********************More about Student 3*************************************************************
		sebt.male_Option();
		sebt.hispanic_MoreAboutMe();
		sebt.black_American_MoreAboutMe();
		sebt.continue_Option();
		//*******************School enrollment for Student 3********************************************************
		sebt.typeSchoolNotEnrolled();
		sebt.continue_Option();
				
		
		//******************About my household*********************************************************************
		sebt.one_Option();
		sebt.continue_Option();
		
		//*****************About your household benefits and income information*************************************
		sebt.snapBenefitsY();
		sebt.agencyID();
		sebt.massHealthY();
		sebt.massHealthID();
		sebt.continue_Option();
		sebt.noOne_Option();
		sebt.continue_Option();
		
		//*****************Income summary*************************************
		sebt.continue_Option();
		
		//*****************Sign & Submit*************************************
		sebt.signSubmitValidationMsg();
		sebt.sebtSignSubmit();
		sebt.exitWithoutUploading();
		
	}
	
	public void ApplySUMMEREBT_MULTIPLE_INCOME_MEMBERS_TC() {
		sebt.launchURL(initializer.GetValue("app.url"));
		// snap.app_Down();
		initializer.infoCall("SUMMEREBT MULTIPLE INCOME MEMBERS TC___________________.");
		sebt.applySUMMEREBTButton();
	
		// *********************Before you start**************************************************************
		sebt.continue_Option();
		// *********************About me*********************************************************************
		sebt.firstLast_Name();
		sebt.middle_Name();
		sebt.suffix_Name();
		sebt.dateOfBirth_Major();
		sebt.language();
		sebt.continue_Option();
	
		//*********************More about me****************************************************************
		sebt.female_Option();
		sebt.male_Option();
		//sebt.hispanic_MoreAboutMe();
		sebt.non_Hispanic_MoreAboutMe();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
	
		//********************My contact info page 1****************************************************************
		sebt.no_Option();
		sebt.address();
		sebt.yes_MailAddress_MyContactInfo();
		sebt.continue_Option();
	
		//*******************My contact info page 2***********************************************************
		sebt.phone_MyContactInfo();
		sebt.phoneNotificationY();
		sebt.email_MyContactInfo();
		sebt.emailNotificationY();
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.continue_Option();
		sebt.one_Option();
		sebt.continue_Option();		
		sebt.firstLast_Name2();
		sebt.father_AboutMember();
		sebt.dateOfBirth_Minor();
		sebt.continue_Option();
		sebt.female_Option();
		sebt.white_MoreAboutMe();
		sebt.continue_Option();
	
		//*******************School enrollment for Student 1********************************************************
		//sebt.typeSchoolPublicChart();
		//sebt.schoolDistrict();
		//sebt.schoolName();
		sebt.typeSchoolPrivate();
		sebt.schoolCity();
		sebt.pvtSchoolName();
		//sebt.typeSchoolNotEnrolled();
		//sebt.studentIDNumber();
		sebt.continue_Option();
	
		//******************About my household*********************************************************************
		sebt.two_Option();
		sebt.continue_Option();
		
		//******************About Adult 1***************************************************************************
		sebt.firstLast_Name3();
		sebt.middle_Name();
		sebt.suffix_Name();
		sebt.continue_Option();
		sebt.male_Option();
		sebt.non_Hispanic_MoreAboutMe();
		sebt.black_American_MoreAboutMe();
		sebt.continue_Option();	
	
		//*****************About your household benefits and income information*************************************
		sebt.snapBenefitsN();
		sebt.tafdcBenefitsY();
		sebt.agencyID();
		sebt.massHealthN();
		sebt.continue_Option();
		sebt.grantee_Option();
		//sebt.noOne_Option();
		sebt.continue_Option();
		sebt.wages_IncomeAndBenefits();
		sebt.continue_Option();
		sebt.one_Amount1_EveryTwoWeeks();
		sebt.continue_Option();
		
		//*******************Add another household member income*********************************************************
		sebt.add_Another_HHMember_Income();
		sebt.select_Another_HHMember_Income();
		//sebt.veteransBenefits_IncomeAndBenefits();
		sebt.wages_IncomeAndBenefits();
		sebt.continue_Option();
		sebt.one_Amount1_EveryTwoWeeks();
		sebt.continue_Option();
				
		//*****************Income summary*************************************
		sebt.continue_Option();
	
		//*****************Sign & Submit*************************************
		sebt.signSubmitValidationMsg();
		sebt.sebtSignSubmit();
		//sebt.exitWithoutUploading();
	}
	

	public void ApplySUMMEREBT_LOGIN_TC() {
		sebt.launchURL(initializer.GetValue("app.url"));
		sebt.loginButton();
		sebt.loginEmail();
		sebt.loginPassword();
		sebt.loginFormButton();
		sebt.connectToMyDTA();
		
	
  }
		
}
	