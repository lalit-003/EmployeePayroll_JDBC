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

	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println("	" + driverClass.getClass().getName());
		}
	}
	
	public List<EmployeeData> readData()
	{
		String sql = "Select * from employee_payroll;";
		List<EmployeeData> employeePayrollList = new ArrayList<>();
		try(Connection connection = this.getConnection();
)
		{
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		List<EmployeeData> listEmployees = new ArrayList<>();
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

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_service?useSSL=false";
		String userName = "root";
		Connection connection;
		System.out.println("Connecting to database : "+jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, "theharyanaking");
		System.out.println("Connection is successful !! " + connection);
		return connection;
	}
	}

