package com.framework.FunctionLibraries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aventstack.extentreports.ExtentTest;
import com.framework.helpers.Initializer;
import com.framework.helpers.Stage;

public class DB {

	public Connection objConnDB = null;
	public Statement st;
	public String strURL;
	public ResultSet rs;
	public ExtentTest test = null;
	public String strQuery = null;
	public String strField = null;
	public String[] strEachField = null;
	public String[] strEachFieldValue = null;
	public PreparedStatement prSt = null;
	private Initializer initializer = null;
	private Stage stage = null;

	public DB() {
	}

	public DB(Initializer initializer, Stage stage) {
		this.initializer = initializer;
		this.stage = stage;
	}

	/**
	 * This method initialize the test object in DB library
	 *
	 * @param test
	 *            This is extentTest object, ExtentTest is used for report
	 *            generation
	 *            @author Santhosh Karra
	 */
	public void init(ExtentTest test) {
		this.test = test;
	}

	/**
	 * Function Name : connectDB Description : This will establish connection to
	 * Database and returns the object
	 * @return Database Connection object
	 * @author Santhosh Karra
	 */
	public Connection connectDB() {
		try {
//			strURL = "jdbc:oracle:thin:@" + initializer.GetValue("db.host") + ":" + initializer.GetValue("db.port")
//					+ "/MAMISA1" + initializer.GetValue("db.dbname");
			strURL = "jdbc:oracle:thin:@dta-ua-bea-adb1.dta.state.ma.us:1521:BCN3UAT";
			Class.forName("oracle.jdbc.driver.OracleDriver");
			try {
				objConnDB = DriverManager.getConnection(strURL,"BEACON","beaconuat");
				System.out.println("DB connection is successful");
			} catch (SQLException e) {
				initializer.log("SQL Exception occured in DB Connection : Exception is - " + e.getMessage());
			}
		} catch (ClassNotFoundException e) {
			initializer.log("Exception occured in DB Connection : Exception is - " + e.getMessage());
			initializer.finalizeResults();
		}
		return objConnDB;
	}

	/**
	 * Function Name : executeQuery Description : Execute the query and return the
	 * field value
	 * @param prSt
	 *            PreparedStatement object
	 * @param strField
	 *            Field value to be returned
	 * @return Returns the result value for the field.
	 * @author Santhosh Karra
	 */
	public String executeQuery(PreparedStatement prSt, String strField) {
		String strFieldValue = null;
		String strReturnValue = "";
		try {
			rs = prSt.executeQuery();
		} catch (SQLException e1) {
			initializer.failureCallwithException("Issue in creating prepared statement at ExecuteQuery method", e1);
		}
		try {
			List<String> strEachFieldValue = new ArrayList<>();
			strEachField = strField.split("\\|\\|");
			while (rs.next()) {
				for (String element : strEachField) {
					strEachFieldValue.add(rs.getString(element));
				}
			}
			for (String str : strEachFieldValue) {
				strReturnValue = strReturnValue + str + "||";
			}
			if (strReturnValue.length() > 0) {
				strFieldValue = strReturnValue.substring(0, strReturnValue.length() - 2);
			}
			strReturnValue = "";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strFieldValue;
	}


	/**
	 * Function Name : ArrayList Description : This execute the Sql query for
	 * multiple records and returns as ArrayList
	 * @param strDBName
	 *            Database name to be queried
	 * @param prSt
	 *            Prepared statement object
	 * @param strField
	 *            filed value to be returned
	 * @return Result value for the above field
	 * @author Santhosh Karra
	 */
	public ArrayList<String> funGetDBValuesArr(String strDBName, PreparedStatement prSt, String strField) {
		// Declaring variable
		Statement oState = null;
		ResultSet oRS = null;
		String strFieldFirst = null;
		String strFieldSecond = null;
		// Connecting DB
		if (strDBName.trim().length() != 0) {
			connectDB();
		}
		if (strField.contains("||")) {
			String[] strFields = strField.split("||");
			strFieldFirst = strFields[0];
			strFieldSecond = strFields[1];
		}
		ArrayList<String> arrDBValues = new ArrayList<>();
		try {
			oRS = prSt.executeQuery();
			while (oRS.next()) {
				if (strField.contains("||")) {
					arrDBValues.add(oRS.getString(strFieldFirst));
					arrDBValues.add(oRS.getString(strFieldSecond));
				} else {
					arrDBValues.add(oRS.getString(strField));
				}
			}
		} catch (Exception e) {
			initializer.log("Error on querying the field " + strField + "\n Query : " + strQuery + ". Exception : "
					+ e.getMessage());
		}
		return arrDBValues;
	}



}
