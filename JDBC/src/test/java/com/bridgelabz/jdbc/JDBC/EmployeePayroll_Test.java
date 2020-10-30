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
		Assert.assertEquals(4, EmployeeData.size());
	}

	// UC3 update and sync data in database
	@Test
	public void givenNewSalaryToEmployee_WhenUpdated_ShouldSyncWithDatabase() {
		List<EmployeeData> EmployeeData = employeePayrollService.readData();
		employeePayrollService.updateEmployeeSalary("Terrisa", 3000000.0);
		boolean result = employeePayrollService.checkEmployeePayrollSyncWithDB("Terrisa");
		Assert.assertTrue(result);
	}

	// UC4 update and sync data in database (using prepared statement)
	@Test
	public void givenNewSalaryToEmployee_WhenUpdated_ShouldSyncWithDatabaseUsingPreparedStatement() {
		List<EmployeeData> EmployeeData = employeePayrollService.readData();
		employeePayrollService.updateEmployeeSalary("Terrisa", 3000000.0);
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
		Assert.assertEquals(4, EmployeeData.size());
	}

	// UC6 functions like sum,max,average by gender
	@Test
	public void givenPayrollData_WhenAverageSalaryByGender_ShouldReturnProperValue() {
		employeePayrollService.readData();
		Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender();
		Assert.assertTrue(averageSalaryByGender.get("F").equals(3000000.0));
		//Assert.assertTrue(averageSalaryByGender.get("M").equals(1500000.0));
	}

	@Test
	public void givenPayrollData_WhenMaxSalary_ShouldReturnProperValue() {
		employeePayrollService.readData();
		Double max = employeePayrollService.getMaxSalary();
		Assert.assertEquals(3000000.0, max, 0.0);
	}

	// UC8 new employee added to employee_payroll and payroll_details and synced wih database
	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWithDatabase() throws SQLException {
		employeePayrollService.readData();
		employeePayrollService.addEmployeeToPayroll("Mark", 500000.0, LocalDate.now(), "M");
		boolean result = employeePayrollService.checkEmployeePayrollSyncWithDB("Mark");
		Assert.assertTrue(result);
	}
	
	
	//Multithreading UC1
     // adding multiple entries to payroll
	@Test
	public void given6Employees_WhenAdded_ShouldMatchEmployeeEntries()
	{
		EmployeeData[] arrayOfEmps = {
				new EmployeeData(0, "Jeff Bezos","M", 100000.0, LocalDate.now()),
				new EmployeeData(0, "Bill Gates","M", 200000.0, LocalDate.now()),
				new EmployeeData(0, "Mark Zuckerberg","M", 300000.0, LocalDate.now()),
				new EmployeeData(0, "Sundar","M", 600000.0, LocalDate.now()),
				new EmployeeData(0, "Mukesh","M", 500000.0, LocalDate.now()),
				new EmployeeData(0, "Anil","M", 300000.0, LocalDate.now())
		};
		employeePayrollService.readData();
		Instant start = Instant.now();
		employeePayrollService.addEmployeeToPayroll(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Duration Without Thread: "+java.time.Duration.between(start, end));
		Assert.assertEquals(7, employeePayrollService.countEntries());
	}
}
