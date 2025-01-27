package com.framework.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Stage {

	public Stage() {
	}

	// Log
	private Logger log = null;
	private String strTestName;
	private boolean iStatus;
	/**
	 *
	 * @return Returns Test Name
	 * @author Santhosh Karra
	 */
	public String getTestName() {
		return strTestName;
	}

	/**
	 *
	 * @param strTestName
	 *            Set Test name to stage
	 *            @author Santhosh Karra
	 */
	public void setTestName(String strTestName) {
		this.strTestName = strTestName;
	}



	/**
	 *
	 * @return Returns consolidated status of test script
	 * @author Santhosh Karra
	 */
	public boolean getStatus() {
		return iStatus;
	}

	/**
	 *
	 * @param iStatus
	 *            Set status of test script
	 *            @author Santhosh Karra
	 */
	public void setStatus(boolean iStatus) {
		this.iStatus = iStatus;
	}



	/**
	 * @return the log
	 * @author Santhosh Karra
	 */
	public Logger getLog() {
		if (log == null) {
			log = LogManager.getRootLogger();
		}
		return log;
	}

	/**
	 *
	 * @param log
	 *            , Logger reference
	 *            @author Santhosh Karra
	 */
	public void setLog(Logger log) {
		this.log = log;
	}

}
