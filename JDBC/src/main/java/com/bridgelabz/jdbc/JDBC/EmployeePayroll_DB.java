package com.bridgelabz.jdbc.JDBC;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class EmployeePayroll_DB {
	
	List<EmployeeData> employeePayrollList = new ArrayList<>();

	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println("	" + driverClass.getClass().getName());
		}
	}
	
	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_service?useSSL=false";
		String userName = "root";
		Connection connection;
		System.out.println("Connecting to database : "+jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, "theharyanaking");
		System.out.println("Connection is successful !! " + connection);
		return connection;
	}

	
	public List<EmployeeData> readData()
	{
		String sql = "Select * from employee_payroll;";
		try(Connection connection = this.getConnection();)
		{
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while(resultSet.next()) {
			employeePayrollList.add(new EmployeeData(resultSet.getInt("id"), resultSet.getString("name"),
					resultSet.getDouble("salary"), resultSet.getDate("start")));
		}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	public void updateEmployeeSalary(String name, double salary)
	{
		int result = this.updateEmployeeSalaryResult(name, salary);
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

	private int updateEmployeeSalaryResult(String name, double salary) {
		String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary,name);
		try(Connection connection = this.getConnection();)
		{
		Statement statement = connection.createStatement();
		return statement.executeUpdate(sql);
				}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public boolean checkEmployeePayrollSyncWithDB(String name) {
		EmployeeData employeePayrollData =  this.getEmployeeData(name);
		return employeePayrollData.getName().equals(name);
	}

}

