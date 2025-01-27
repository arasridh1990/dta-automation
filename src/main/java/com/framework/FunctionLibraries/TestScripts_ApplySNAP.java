package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_ApplySNAP extends BaseClass {
	public ExtentTest test = null;
//	private DTAConnect_ApplicationFunctions dta = null;
	private Apply_SNAP_ApplicationFunctions snap = null;
//	private Apply_CASH_ApplicationFunctions cash = null;

//	private CommonFunctions commonFunctions = null;
//	private Stage stage = null;
	private Initializer initializer = null;

	public TestScripts_ApplySNAP() {
	}

//	public TestScripts_ApplySNAP(CommonFunctions commonFunctions, DTAConnect_ApplicationFunctions dta,
	//Apply_SNAP_ApplicationFunctions snap,Apply_CASH_ApplicationFunctions cash,
//			Initializer initializer, Stage stage)
	public TestScripts_ApplySNAP(CommonFunctions commonFunctions, Apply_SNAP_ApplicationFunctions snap,
			Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.snap = snap;
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

	public void ApplySNAP_YES_TC() {

		snap.launchURL(initializer.GetValue("app.url"));
		initializer.infoCall("SNAP YES HAPPY PATH TC___________________.");
		// snap.app_Down();
		snap.applySNAPButton();
		// *********************How does applying work---Before you
		// start**********************************************************************************************
		snap.handyDocs_BeforeYouStart();
		snap.continue_Option();
		// *********************About
		// me*******************************************************************************************************************************
		snap.aboutMe_ValidationMsg();
		snap.firstLast_Name();
		snap.middle_Name();
		snap.suffix_Name();
		snap.dateOfBirth_Senior();
		snap.noSSN_Option();
		snap.continue_Option();

		// *********************My contact
		// info*******************************************************************************************************************************
		snap.no_Option();
		snap.continue_Option();
		snap.myContactInfo_Address_Phone_ValidationMsg();
		snap.myContactInfo_Email_ValidationMsg();
		snap.myContactInfo_AddressZipCode_ValidationMsg();
		snap.address();
		
		snap.no_MailAddress_MyContactInfo();
		snap.myContactInfo_MailAddressEmail_ValidationMsg();
		snap.myContactInfo_MailAddressZipCode_ValidationMsg();
		snap.mail_address();
		
		snap.email_MyContactInfo();
		snap.phone_MyContactInfo();
		snap.continue_Option();
        //***********************Emergency
		//snap benefits************************************************************************************************************************
		snap.emergencyFSBenefits_Lesshouseexp_No();
		snap.emergencyFSBenefits_LessMonthlyIncome_Yes();
		snap.emergencyFSBenefits_MigrantWorker_Yes();
		snap.continue_Option();

		snap.yes_Option();
		snap.language_MoreAboutMe();
		snap.continue_Option();

		snap.male_Option();
		snap.non_Hispanic_MoreAboutMe();
		snap.asian_MoreAboutMe();
		snap.continue_Option();

		snap.four_Option();
		snap.continue_Option();

		snap.firstLast_Name1();
		snap.son_AboutMember();
		snap.dateOfBirth_Minor();
		snap.male_Option();
		snap.yes_Option();
		snap.noSSN_Option();
		snap.continue_Option();

		snap.firstLast_Name2();
		snap.daughter_AboutMember();
		snap.dateOfBirth_Major();
		snap.female_Option();
		snap.yes_Option();
		snap.sSN_Option();
		snap.continue_Option();

		snap.firstLast_Name3();
		snap.grandfather_AboutMember();
		snap.dateOfBirth_Senior();
		snap.male_Option();
		snap.yes_Option();
		snap.sSN_Option();
		snap.continue_Option();

		snap.dependent_1_Option();
		snap.continue_Option();

		snap.grantee_Option();
		snap.continue_Option();

		snap.wages_IncomeAndBenefits();
		snap.veteransBenefits_IncomeAndBenefits();
		snap.childSupport_IncomeAndBenefits();
		snap.continue_Option();

		snap.one_Amount1_EveryTwoWeeks();
		snap.add_Wage();
		snap.one_Amount2_Week();
		snap.two_Amount1_EveryTwoWeeks();
		snap.three_Amount1_Week();
		snap.continue_Option();

		snap.continue_Option();

		snap.continue_Option();

		snap.heat_Utility_HousingCosts();
		snap.rent_Shelter_HousingCosts();
		snap.continue_Option();

		snap.one_Amount1_Month();
		snap.continue_Option();
//		*********************Expenses---Child or Adult Dependent Care Costs---Tell us about your Child or Adult Dependent Care costs********************************
		snap.grantee_Transport_ChildAdultDependentCareCosts();
		snap.dependent_One_Pay_ChildAdultDependentCareCosts();
		snap.continue_Option();
//		*********************Expenses---Child or Adult Dependent Care Costs---Child or Adult Dependent Care costs for FirstName LastName (DD.MMM.YYYY)**************
		snap.providerCare_Amount_ChildAdultDependentCareCosts();
		snap.providerCare_Week_ChildAdultDependentCareCosts();
		snap.yes_Transportation_ChildAdultDependentCareCosts();
		snap.transportation_Amount_ChildAdultDependentCareCosts();
		snap.transportation_Year_ChildAdultDependentCareCosts();
		snap.yes_Drive_ChildAdultDependentCareCosts();
		snap.two_Option();
		snap.continue_Option();

		snap.address();
		snap.providerAddress_Trips();
		snap.providerAddress_Trips_Week();
		snap.continue_Option();

		snap.address();
		snap.providerAddress_Trips();
		snap.providerAddress_Trips_Week();
		snap.continue_Option();

		snap.grantee_Option();
		snap.grantee_Amount_ChildsupportCosts();
		snap.grantee_Month_ChildsupportCosts();
		snap.continue_Option();

		snap.grantee_Option();
		snap.continue_Option();

		snap.yes_Option();
		snap.one_Option();
		snap.continue_Option();

		snap.address();
		snap.providerAddress_Trips();
		snap.providerAddress_Trips_Week();
		snap.pharmacy_Transportation_Amount();
		snap.pharmacy_Transportation_Week();
		snap.continue_Option();

		snap.yes_Option();
		snap.continue_Option();

		snap.signSubmitValidationMsg();
		snap.signSubmit();

	}
	public void ApplySNAP_NO_TC() {

		snap.launchURL(initializer.GetValue("app.url"));
		// snap.app_Down();
		initializer.infoCall("SNAP NO TC___________________.");
		snap.applySNAPButton();
		// *********************How does applying work---Before you
		// start**********************************************************************************************
		snap.handyDocs_BeforeYouStart();
		snap.continue_Option();
		// *********************About
		// me*******************************************************************************************************************************
		snap.firstLast_Name();
		snap.dateOfBirth_Major();
		snap.continue_Option();

		// *********************My contact
		// info*******************************************************************************************************************************
		snap.no_Option();
		snap.continue_Option();
		snap.address();
		
		snap.no_MailAddress_MyContactInfo();
		snap.mail_address();
		
		snap.email_MyContactInfo();
		snap.phone_MyContactInfo();
		snap.continue_Option();
        //***********************Emergency
		//snap benefits************************************************************************************************************************
		snap.continue_Option();

		snap.continue_Option();

		snap.continue_Option();

		snap.one_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.noOne_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.signSubmit();

	}
	public void ApplySNAP_LANG_TC() {

		snap.launchURL(initializer.GetValue("app.url"));
		// snap.app_Down();
		initializer.infoCall("SNAP LANG TC___________________.");
		snap.languageSelect();
		snap.applySNAPButton_es();
		snap.continue_Option();
		snap.firstLast_Name();
		snap.dateOfBirth_Major();
		snap.continue_Option();

		snap.no_Option();
		snap.continue_Option();
		snap.address();
		
		snap.email_MyContactInfo();
		snap.phone_MyContactInfo();
		snap.continue_Option();
        snap.continue_Option();

		snap.continue_Option();

		snap.continue_Option();

		snap.one_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.noOne_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.continue_Option();
		snap.signSubmit_ES();

	}
	public void SNAP_Eligibility() {

		snap.launchURL(initializer.GetValue("app.url"));
//		snap.app_Down();
		snap.SNAP_Eligibility_Button();
		snap.eligibility_people_ValidationMsg();
		snap.four_Option();
		snap.eligibility_seniors_ValidationMsg();
		snap.eligibility_Seniors_Yes();
		snap.eligibility_disabilities_ValidationMsg();
		snap.eligibility_Disabilities_Yes();
		snap.eligibility_income_ValidationMsg();
		snap.eligibility_Amount();
		snap.eligibility_Week();
		snap.continue_Option();
		snap.eligibility_handyDocs();
		snap.continue_Option();

	}
}
