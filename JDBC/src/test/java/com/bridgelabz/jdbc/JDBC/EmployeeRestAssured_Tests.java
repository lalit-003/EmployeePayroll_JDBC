package com.bridgelabz.jdbc.JDBC;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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
	
	private Response addEmployeeToJsonServer(EmployeeData employeePayrollData) {
          String empJson = new Gson().toJson(employeePayrollData);
          RequestSpecification request = RestAssured.given();
          request.header("Content-Type","application/json");
          request.body(empJson);
		return request.post("/employee_payroll");
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
	
	//adding new employee to json server and than counting
	@Test
	public void givenNewEmployee_WhenAdded_ShouldMatchCountand201ResponseAndCount() throws SQLException
	{
		EmployeeData[] arrayOfEmps = getEmployeeList();
		EmployeePayroll_DB employeePayroll;
		employeePayroll = new EmployeePayroll_DB(Arrays.asList(arrayOfEmps));
        
		EmployeeData employeePayrollData = null;
		employeePayrollData = new EmployeeData(0,"Mark Zuckerberg","M",300000.0,LocalDate.now());
		Response response = addEmployeeToJsonServer(employeePayrollData);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201,statusCode);
		
		employeePayrollData = new Gson().fromJson(response.asString(),EmployeeData.class);
		employeePayroll.addEmployeeToPayroll(employeePayrollData);
		long entries = employeePayroll.countEntries();
		Assert.assertEquals(3,entries);
	}

	
		
	}
