package com.ehs.dta;

import org.testng.annotations.Test;

import com.framework.helpers.BaseClass;

public class DTAConnect extends BaseClass {

	/*
	 * Function Name : This test scenario will submitting the SNAP application
	 * 
	 * @author Santhosh Karra
	 */

	@Test
	public void Online_Account() {
	ts_DTA.Online_Account();
	}
	public class ApplyCASH extends BaseClass {

		/*
		 * Function Name : This test scenario will submitting the TAFDC application
		 * 
		 * @author Santhosh Karra
		 */

		@Test
		public void TAFDC_Apply() {
			ts_CASH.Apply_TAFDC();
		}

		/*
		 * Function Name : This test scenario will submitting the EAEDC application
		 * 
		 * @author Santhosh Karra
		 */

		@Test
		public void EAEDC_Apply() {
			ts_CASH.Apply_EAEDC();
		}
	}
	
}
