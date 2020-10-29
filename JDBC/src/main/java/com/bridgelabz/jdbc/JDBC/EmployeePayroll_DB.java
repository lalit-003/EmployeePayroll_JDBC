package com.bridgelabz.jdbc.JDBC;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class EmployeePayroll_DB {
	
	List<EmployeeData> employeePayrollList = new ArrayList<>();
	private EmployeePayrollDBService employeePayrollDBService;
	
	public EmployeePayroll_DB() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	public EmployeePayroll_DB(List<EmployeeData> employeePayrollList) {
		this();
		this.employeePayrollList = employeePayrollList;
	}

	
	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println("	" + driverClass.getClass().getName());
		}
	}
	
	public void updateEmployeeSalary(String name, double salary)
	{
		int result = new EmployeePayrollDBService().updateEmployeeSalaryResult(name, salary);
		if(result == 0) return;
		EmployeeData employeeData = this.getEmployeeData(name);
		if(employeeData != null) employeeData.setSalary(salary);
	}
	
	private EmployeeData getEmployeeData(String name) {
      EmployeeData employeeData ;
      employeeData = this.employeePayrollList.stream()
    		             .filter(employeePayrollDataItem  ->  (employeePayrollDataItem.getName()).equals(name))
    		             .findFirst()
    		             .orElse(null);
		return employeeData;
	}

	
	public boolean checkEmployeePayrollSyncWithDB(String name) {
		try {
			return employeePayrollDBService.getEmployeeData(name).get(0).equals(getEmployeeData(name));
		} catch (IndexOutOfBoundsException e) {
		}
		return false;
	}

	public List<EmployeeData> readData() {
         this.employeePayrollList = new EmployeePayrollDBService().readData();
		return employeePayrollList;
	}

	
}

