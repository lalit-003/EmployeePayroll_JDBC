package com.bridgelabz.jdbc.JDBC;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class EmployeeRestAssured_Tests {

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}
	
	private EmployeeData[] getEmployeeList() {
		Response response = RestAssured.get("/employee_payroll");
		System.out.println("Employee payroll entries in json server : \n"+response.asString());
		EmployeeData[] arrayOfEmps = new Gson().fromJson(response.asString(),EmployeeData[].class);
		return arrayOfEmps;
	}

	//Test to retrieve entries from json server
	@Test
	public void givenEmployeeDataInJSONServer_WhenRetreived_ShouldMatchCount()
	{
		EmployeeData[] arrayOfEmps = getEmployeeList();
		EmployeePayroll_DB employeePayroll;
		employeePayroll = new EmployeePayroll_DB(Arrays.asList(arrayOfEmps));
		long entries = employeePayroll.countEntries();
		Assert.assertEquals(2,entries);
	}
	}
