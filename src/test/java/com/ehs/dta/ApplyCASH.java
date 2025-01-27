package com.ehs.dta;

import org.testng.annotations.Test;

import com.framework.helpers.BaseClass;

public class ApplyCASH extends BaseClass {

	/*
	 * Function Name : This test scenario will submitting the TAFDC application
	 * 
	 * @author Santhosh Karra
	 */

	// Happy scenario with all questions answered as yes
	@Test
	public void TAFDCApply() {
		ts_CASH.Apply_TAFDC_TestCase();
	}

	// Happy scenario with random data
	@Test
	public void TAFDCApply1() {
		ts_CASH.Apply_TAFDC_TestCase1();

	}

	// Test with giving no for all questions
	@Test
	public void TAFDCApply2() {
		ts_CASH.Apply_TAFDC_TestCase2();

	}

	// Test with language changed to Spanish
	@Test
	public void TAFDCApply3() {
		ts_CASH.Apply_TAFDC_TestCase3();

	}

	/*
	 * Function Name : This test scenario will submitting the EAEDC application
	 * 
	 * @author Santhosh Karra
	 */
	// Happy scenario with all questions answered as yes
	@Test
	public void EAEDCApply() {
		ts_CASH.Apply_EAEDC_TestCase();
	}

	// Happy scenario with random data
	@Test
	public void EAEDCApply1() {
		ts_CASH.Apply_EAEDC_TestCase2();
	}

	// Test with giving no for all questions
	@Test
	public void EAEDCApply2() {
		ts_CASH.Apply_EAEDC_TestCase3();
	}

	// Test with language changed to Spanish
	@Test
	public void EAEDCApply3() {
		ts_CASH.Apply_EAEDC_TestCase4();
	}
}
