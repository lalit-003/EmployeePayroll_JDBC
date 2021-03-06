package com.bridgelabz.jdbc.JDBC;

import java.sql.Date;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class EmployeePayroll_Test {

	EmployeePayroll_DB employeePayrollService = new EmployeePayroll_DB();

	// UC2 retrieving data and checking size
	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		List<EmployeeData> EmployeeData = employeePayrollService.readData();
		Assert.assertEquals(3, EmployeeData.size());
	}

	// UC3 update and sync data in database
	@Test
	public void givenNewSalaryToEmployee_WhenUpdated_ShouldSyncWithDatabase() {
		List<EmployeeData> EmployeeData = employeePayrollService.readData();
		employeePayrollService.updateEmployeeSalary("Terrisa", 4000000.0);
		boolean result = employeePayrollService.checkEmployeePayrollSyncWithDB("Terrisa");
		Assert.assertTrue(result);
	}

	// UC4 update and sync data in database (using prepared statement)
	@Test
	public void givenNewSalaryToEmployee_WhenUpdated_ShouldSyncWithDatabaseUsingPreparedStatement() {
		List<EmployeeData> EmployeeData = employeePayrollService.readData();
		employeePayrollService.updateEmployeeSalary("Terrisa", 4000000.0);
		boolean result = employeePayrollService.checkEmployeePayrollSyncWithDB("Terrisa");
		Assert.assertTrue(result);
	}

	// UC5 matching employee count for given date range
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
		List<EmployeeData> EmployeeData = employeePayrollService.readData();
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		EmployeeData = employeePayrollService.getEmpPayrollDataForDataRange(startDate, endDate);
		Assert.assertEquals(3, EmployeeData.size());
	}

	// UC6 functions like sum,max,average by gender
	@Test
	public void givenPayrollData_WhenAverageSalaryByGender_ShouldReturnProperValue() {
		employeePayrollService.readData();
		Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender();
		Assert.assertTrue(averageSalaryByGender.get("F").equals(4000000.0));
		//Assert.assertTrue(averageSalaryByGender.get("M").equals(1500000.0));
	}

	@Test
	public void givenPayrollData_WhenMaxSalary_ShouldReturnProperValue() {
		employeePayrollService.readData();
		Double max = employeePayrollService.getMaxSalary();
		Assert.assertEquals(4000000.0, max, 0.0);
	}

	// UC8 new employee added to employee_payroll and payroll_details and synced wih database
	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWithDatabase() throws SQLException {
		employeePayrollService.readData();
		employeePayrollService.addEmployeeToPayroll("Mark", 500000.0, LocalDate.now(), "M","1232334332","Indore","Marketing");
		boolean result = employeePayrollService.checkEmployeePayrollSyncWithDB("Mark");
		Assert.assertTrue(result);
	}
	
	
	//Multithreading  --- UC1,UC2,UC3,UC4,UC5
     // adding multiple entries to payroll
	@Test
	public void given6Employees_WhenAdded_ShouldMatchEmployeeEntries()
	{
		EmployeeData[] arrayOfEmps = {
				new EmployeeData(0, "Jeff Bezos", 100000.0, LocalDate.now(),"M","1232334332","LA","Marketing"),
				new EmployeeData(0, "Bill Gates", 200000.0, LocalDate.now(),"M","1232334332","NY","Marketing"),
				new EmployeeData(0, "Mark Zuckerberg", 300000.0, LocalDate.now(),"M","1232334332","WahingtanDC","Marketing"),
				new EmployeeData(0, "Sundar", 600000.0, LocalDate.now(),"M","1232334332","LA","Marketing"),
				new EmployeeData(0, "Mukesh", 500000.0, LocalDate.now(),"M","1232334332","Mumbai","Marketing"),
				new EmployeeData(0, "Anil", 300000.0, LocalDate.now(),"M","1232334332","Mumbai","Marketing")
		};
		employeePayrollService.readData();
		Instant start = Instant.now();
		employeePayrollService.addEmployeeToPayroll(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		Instant threadStart = Instant.now(); 
		int count = employeePayrollService.addEmployeesToPayrollWithThreads(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		System.out.println("Duration With Thread: "+java.time.Duration.between(threadStart, threadEnd));
		System.out.println("Duration Without Thread: "+java.time.Duration.between(start, end));
		
		Assert.assertEquals(13, count+1);
	}
	
	@Test
	public void givenEmployees_WhenUpdated_ShouldMatchEmployeeEntries()
	{
		EmployeeData[] arrayOfEmps = {
				new EmployeeData( "Jeff Bezos", 950000.0),
				new EmployeeData( "Bill Gates", 850000.0),
				new EmployeeData( "Mark Zuckerberg", 650000.0)
		};
		employeePayrollService.readData();
				Instant threadStart = Instant.now(); 
		employeePayrollService.UpdateEmployeesToPayrollWithThreads(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		System.out.println("Duration With Thread: "+java.time.Duration.between(threadStart, threadEnd));
		boolean result1 = employeePayrollService.checkEmployeePayrollSyncWithDB("Jeff Bezos");
		Assert.assertTrue(result1);
		boolean result2 = employeePayrollService.checkEmployeePayrollSyncWithDB("Bill Gates");
		Assert.assertTrue(result2);
		boolean result3 = employeePayrollService.checkEmployeePayrollSyncWithDB("Mark Zuckerberg");
		Assert.assertTrue(result3);
		
	}
	}

