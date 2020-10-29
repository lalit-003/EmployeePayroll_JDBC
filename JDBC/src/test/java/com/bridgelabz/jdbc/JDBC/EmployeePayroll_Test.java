package com.bridgelabz.jdbc.JDBC;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;


public class EmployeePayroll_Test {

	EmployeePayroll_DB employeePayrollService = new EmployeePayroll_DB();
	
	//UC2 retrieving data and checking size
	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount()
	{
		List<EmployeeData> employeePayrollData = employeePayrollService.readData();
		Assert.assertEquals(3, employeePayrollData.size());
	}
	
	// UC3 update and sync data in database
	@Test
	public void givenNewSalaryToEmployee_WhenUpdated_ShouldSyncWithDatabase()
	{
		List<EmployeeData> employeePayrollData = employeePayrollService.readData();
		employeePayrollService.updateEmployeeSalary("Terrisa",3000000.0);
		boolean result = employeePayrollService.checkEmployeePayrollSyncWithDB("Terrisa");
		Assert.assertTrue(result);
	}
}
