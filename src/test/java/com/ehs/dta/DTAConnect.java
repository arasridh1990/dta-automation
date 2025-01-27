package com.ehs.dta;

import org.testng.annotations.Test;

import com.framework.helpers.BaseClass;

public class DTAConnect extends BaseClass {

	// TAFDC application
	@Test
	public void TAFDCApply() {
		ts_CASH.Apply_TAFDC_TestCase();
	}

	// EAEDC application
	@Test
	public void EAEDCApply() {
		ts_CASH.Apply_EAEDC_TestCase();
	}

	// SNAP application
	@Test
	public void SNAPApply_TC1() {
		ts_FS.ApplySNAP_YES_TC();
	}
	
    // SNAP Eligibility Screener
	@Test
	public void SNAP_Eligibility() {
		ts_FS.SNAP_Eligibility();
	}
	
	// Interim Report application
	@Test
	public void INTERIMApplyTC1() {
		ts_INTREP.Interim_REPORT_YES_TC();
	}

	// Recertification application
	@Test
	public void RECERTApplyTC2() {
		ts_RECERT.Apply_RECERT_YES_TC();
	}

	// Covid Recert application
	@Test
	public void COVIDRECERTApply() {
		ts_COVIDRECERT.Apply_COVIDRECERT();
	}
	
	// Provider portal login
	@Test
	public void PROVIDERLogin() {
		ts_LOGIN.provider_LOGIN();
	}
	// Apply EBT and Replace EBT Card
	@Test
	public void EBTApply() {
		ts_EBT.Apply_EBT();
	}
}
