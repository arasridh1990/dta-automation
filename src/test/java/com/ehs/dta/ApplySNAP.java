package com.ehs.dta;

import org.testng.annotations.Test;

import com.framework.helpers.BaseClass;

public class ApplySNAP extends BaseClass {

	/*
	 * Function Name : This test scenario will submitting the SNAP application
	 * 
	 * @author Santhosh Karra
	 */

	@Test
	public void SNAPApply_TC1() {
		ts_FS.ApplySNAP_YES_TC();
	}
	@Test
	public void SNAPApply_TC2() {
		ts_FS.ApplySNAP_NO_TC();
	}
	@Test
	public void SNAPApply_TC3() {
		ts_FS.ApplySNAP_LANG_TC();
	}
	/*
	 * Function Name : This test scenario will submitting the SNAP Eligibility
	 * application
	 * 
	 * @author Santhosh Karra
	 */

	@Test
	public void SNAP_Eligibility() {
		ts_FS.SNAP_Eligibility();
	}
	

}
