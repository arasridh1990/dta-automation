package com.ehs.dta;

import org.testng.annotations.Test;

import com.framework.helpers.BaseClass;

public class ApplyEBT extends BaseClass {

	/*
	 * Function Name : This test scenario will replacing the P-EBT card
	 * 
	 * @author Kishore B
	 */

	@Test
	public void EBT_Apply() {
		ts_EBT.Apply_EBT();
	}
}
