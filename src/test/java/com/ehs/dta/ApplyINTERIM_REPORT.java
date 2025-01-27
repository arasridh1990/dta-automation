package com.ehs.dta;

import org.testng.annotations.Test;

import com.framework.helpers.BaseClass;

public class ApplyINTERIM_REPORT extends BaseClass {

	@Test
	public void INTERIMApplyTC1() {
		ts_INTREP.Interim_REPORT_YES_TC();
	}
	@Test
	public void INTERIMApplyTC2() {
		 ts_INTREP.Interim_REPORT_NO_TC();
	}
	
	@Test
	public void INTERIMApplyTC3() {
		 ts_INTREP.Interim_REPORT_LANG();
	}
	
	
}
