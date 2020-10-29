package com.bridgelabz.jdbc.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

	private PreparedStatement payRollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;
	
	
	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
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
		List<EmployeeData> employeePayrollList = new ArrayList<>();
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
	
	public List<EmployeeData> getEmployeeData(String name) {
		List<EmployeeData> employeePayrollList = new ArrayList<>();
		if (payRollDataStatement == null)
		{
			prepareStatementForEmployeeData();
		}
			try {
			payRollDataStatement.setString(1, name);
			ResultSet resultSet = payRollDataStatement.executeQuery();
			while (resultSet.next()) {
				employeePayrollList.add(new EmployeeData(resultSet.getInt("id"), resultSet.getString("name"),
						resultSet.getDouble("salary"), resultSet.getDate("start")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = getConnection();
			String sql = "SELECT * FROM employee_payroll WHERE name = ?";
			payRollDataStatement = connection.prepareStatement(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public int updateEmployeeSalaryResult(String name, double salary) {
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
	
}
