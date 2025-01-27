package com.framework.FunctionLibraries;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.BaseClass;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class TestScripts_ApplyCASH extends BaseClass {
	public ExtentTest test = null;
	private Apply_CASH_ApplicationFunctions cash = null;
//	private CommonFunctions commonFunctions = null;
//	private Stage stage = null;
	private Initializer initializer = null;

	public TestScripts_ApplyCASH() {
	}

	public TestScripts_ApplyCASH(CommonFunctions commonFunctions, Apply_CASH_ApplicationFunctions cash,
			Initializer initializer, Stage stage) {
		this.commonFunctions = commonFunctions;
		this.cash = cash;
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

	public void Apply_TAFDC_TestCase() {
		cash.launchURL(initializer.GetValue("app.url"));
		cash.applyTAFDCButton();
		// *********************How does applying work---Before you
		// start***********************************************************************************************
		cash.handyDocs_BeforeYouStart();
		cash.continue_Option();
		// *********************Initial Application for TAFDC***********************************************************************************************************
		cash.firstName_ValidationMsg();
		cash.lastName_ValidationMsg();
		cash.dateofbirth_ValidationMsg();
		cash.firstLast_Name();
		cash.middle_Name();
		cash.suffix_Name();
		cash.dateOfBirth_Senior();
		cash.sSN_Option();
		cash.agencyID();
		cash.language();
		cash.addressZipCode_TAFDC_ValidationMsg();
		cash.address();
		cash.phone();
		cash.email_ValidationMsg();
		cash.email();
		cash.pregnant_yes_Option();
		cash.children_ValidationMsg();
		cash.children_yes_Option();
		cash.tty_yes_Option();
		cash.income_TAFDC_Yes_Option();
		cash.source_One();
		cash.amount_One();
		cash.source_Two();
		cash.amount_Two();
		cash.source_Three();
		cash.amount_Three();
		cash.source_Four();
		cash.amount_Four();
		cash.source_Five();
		cash.amount_Five();
		initializer.wait(2);
		cash.ebt_TAFDC_Yes_Option();
		cash.continue_Option();
		cash.signSubmitValidationMsg();
		cash.signSubmit();
		cash.exit();
	}
	public void Apply_TAFDC_TestCase1() {
		cash.launchURL(initializer.GetValue("app.url"));
		cash.applyTAFDCButton();
		// *********************How does applying work---Before you
		// start***********************************************************************************************
		cash.handyDocs_BeforeYouStart();
		cash.continue_Option();
		// *********************Initial Application for TAFDC***********************************************************************************************************
		cash.firstLast_Name();
		cash.middle_Name();
		cash.suffix_Name();
		cash.dateOfBirth_Senior();
		cash.sSN_Option();
		cash.agencyID();
		cash.language();
		cash.address();
		cash.phone();
		cash.email();
		cash.pregnant_yes_Option();
		cash.children_yes_Option();
		cash.tty_yes_Option();
		cash.income_TAFDC_Yes_Option();
		cash.source_One();
		cash.amount_One();
		cash.source_Two();
		cash.amount_Two();
		cash.ebt_TAFDC_Yes_Option();
		cash.continue_Option();
		cash.signSubmitValidationMsg();
		cash.signSubmit();
		cash.exit();
	}
	public void Apply_TAFDC_TestCase2() {
		cash.launchURL(initializer.GetValue("app.url"));
		cash.applyTAFDCButton();
		// *********************How does applying work---Before you
		// start***********************************************************************************************
		cash.handyDocs_BeforeYouStart();
		cash.continue_Option();
		// *********************Initial Application for TAFDC***********************************************************************************************************
		cash.firstName_ValidationMsg();
		cash.lastName_ValidationMsg();
		cash.dateofbirth_ValidationMsg();
		cash.firstLast_Name();
		cash.middle_Name();
		cash.suffix_Name();
		cash.dateOfBirth_Major();
		cash.noSSN_Option();
		cash.agencyID();
		cash.language();
		cash.addressZipCode_TAFDC_ValidationMsg();
		cash.address();
		cash.phone();
		cash.email_ValidationMsg();
		cash.email();
		cash.pregnant_no_Option();
		cash.children_ValidationMsg();
		cash.children_no_Option();
		cash.tty_no_Option();
		cash.income_TAFDC_No_Option();
		initializer.wait(2);
		cash.ebt_TAFDC_Yes_Option();
		cash.continue_Option();
		cash.signSubmitValidationMsg();
		cash.signSubmit();
		cash.exit();	
	}
	public void Apply_TAFDC_TestCase3() {
		cash.launchURL(initializer.GetValue("app.url"));
		initializer.infoCall("********Apply_TAFDC_TestCaseSet3:Language changed********");
		cash.languageSelect();
		cash.applyTAFDCButton_ES();
		cash.continue_Option();
		cash.firstLast_Name();
		cash.suffix_Name();
		cash.dateOfBirth_Major();
		cash.language();
		cash.address();
		cash.phone();
		cash.email();
		cash.pregnant_no_Option();
		cash.children_no_Option();
		cash.tty_no_Option();
		cash.income_TAFDC_No_Option();
		cash.continue_Option();
		cash.signSubmit();
		cash.exit_ES();	
	}

	public void Apply_EAEDC_TestCase() {
		cash.launchURL(initializer.GetValue("app.url"));
		initializer.infoCall("********Apply_EAEDC_TestCaseSet1:Happy path********");
		cash.applyEAEDCButton();
		// *********************How does applying work---Before you
		// start***********************************************************************************************
		cash.handyDocs_BeforeYouStart();
		cash.continue_Option();
		// *********************Initial Application for EAEDC***********************************************************************************************************
		cash.firstName_ValidationMsg();
		cash.lastName_ValidationMsg();
		cash.dateofbirth_ValidationMsg();
		cash.address_ValidationMsg();
		cash.addressZipCode_EAEDC_ValidationMsg();
		cash.phone_ValidationMsg();
		cash.firstLast_Name();
		cash.middle_Name();
		cash.suffix_Name();
		cash.dateOfBirth_Senior();
		cash.sSN_Option();
		cash.agencyID();
		cash.language();
		cash.address();
		cash.phone();
		cash.email_ValidationMsg();
		cash.email();
		cash.pregnant_yes_Option();
		cash.children_yes_Option();
		cash.tty_yes_Option();
		cash.disability_no_Option();
		cash.income_EAEDC_Yes_Option();
		cash.source_One();
		cash.amount_One();
		cash.source_Two();
		cash.amount_Two();
		cash.source_Three();
		cash.amount_Three();
		cash.source_Four();
		cash.amount_Four();
		cash.source_Five();
		cash.amount_Five();
		initializer.wait(2);
		cash.ebt_EAEDC_Yes_Option();
		cash.continue_Option();
		cash.signSubmitValidationMsg();
		cash.signSubmit();
		cash.exit();

	}
	public void Apply_EAEDC_TestCase2() {
		cash.launchURL(initializer.GetValue("app.url"));
		initializer.infoCall("********Apply_EAEDC_TestCaseSet2:Random data********");
		cash.applyEAEDCButton();
		// *********************How does applying work---Before you
		// start***********************************************************************************************
		cash.handyDocs_BeforeYouStart();
		cash.continue_Option();
		// *********************Initial Application for EAEDC***********************************************************************************************************
		cash.firstLast_Name();
		cash.suffix_Name();
		cash.dateOfBirth_Senior();
		cash.sSN_Option();
		cash.agencyID();
		cash.language();
		cash.address();
		cash.phone();
		cash.email_ValidationMsg();
		cash.email();
		cash.pregnant_yes_Option();
		cash.children_yes_Option();
		cash.tty_yes_Option();
		cash.disability_no_Option();
		cash.income_EAEDC_Yes_Option();
		cash.source_One();
		cash.amount_One();
		cash.source_Two();
		cash.amount_Two();
		cash.ebt_EAEDC_Yes_Option();
		cash.continue_Option();
		cash.signSubmitValidationMsg();
		cash.signSubmit();
		cash.exit();

	}
	public void Apply_EAEDC_TestCase3() {
		cash.launchURL(initializer.GetValue("app.url"));
		initializer.infoCall("********Apply_EAEDC_TestCaseSet3:Negative Scenario********");
		cash.applyEAEDCButton();
		// *********************How does applying work---Before you
		// start***********************************************************************************************
		cash.handyDocs_BeforeYouStart();
		cash.continue_Option();
		// *********************Initial Application for EAEDC***********************************************************************************************************
		cash.firstLast_Name();
		cash.middle_Name();
		cash.suffix_Name();
		cash.dateOfBirth_Senior();
		cash.sSN_Option();
		cash.agencyID();
		cash.language();
		cash.address();
		cash.phone();
		cash.email();
		cash.pregnant_no_Option();
		cash.children_no_Option();
		cash.disability_no_Option();
		cash.income_EAEDC_No_Option();
		cash.ebt_EAEDC_Yes_Option();
		cash.continue_Option();
		cash.signSubmitValidationMsg();
		cash.signSubmit();
		cash.exit();

	}
	public void Apply_EAEDC_TestCase4() {
		cash.launchURL(initializer.GetValue("app.url"));
		initializer.infoCall("********Apply_EAEDC_TestCaseSet3:Language changed********");
		cash.languageSelect();
		cash.applyEAEDCButton_ES();
		cash.continue_Option();
		cash.firstLast_Name();
		cash.suffix_Name();
		cash.dateOfBirth_Major();
		cash.address();
		cash.phone();
		cash.email();
		cash.language();
		cash.pregnant_no_Option();
		cash.children_no_Option();
		cash.disability_no_Option();
		cash.tty_no_Option();
		cash.income_EAEDC_No_Option();
		cash.continue_Option();
		cash.signSubmit();
		cash.exit_ES();	

	}
}
