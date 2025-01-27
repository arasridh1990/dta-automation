package com.ehs.dta;

import org.testng.annotations.Test;

import com.framework.helpers.BaseClass;

public class ApplyRECERT extends BaseClass {
//
	@Test
	public void RECERTApplyTC1() {
		ts_RECERT.Apply_RECERT_NO_TC();
	}
	@Test
	public void RECERTApplyTC2() {
		ts_RECERT.Apply_RECERT_YES_TC();
	}

    @Test
	public void RECERTApplyTC3() {
		ts_RECERT.Apply_RECERT_LANG_TC();
	}

}
