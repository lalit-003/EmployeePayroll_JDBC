package com.bridgelabz.jdbc.JDBC;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {

	public int connectionCounter = 0;
	private PreparedStatement payRollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;
	
	
	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}
	
	
	private synchronized Connection getConnection() throws SQLException {
		connectionCounter++;
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_service?useSSL=false";
		String userName = "root";
		Connection connection;
		System.out.println("Processing thread  : "+ Thread.currentThread().getName()+"Connecting to database with Id :"+connectionCounter);
		connection = DriverManager.getConnection(jdbcURL, userName, "theharyanaking");
		System.out.println("Processing thread  : "+ Thread.currentThread().getName()+"Id :"+connectionCounter+"Connection is successful !! " + connection);
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
		employeePayrollList = this.getList(resultSet);
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
				 System.out.println("4");
			payRollDataStatement.setString(1, name);
			ResultSet resultSet = payRollDataStatement.executeQuery();
			employeePayrollList = this.getList(resultSet);
			 System.out.println("5");
			} catch (Exception e) {
			e.printStackTrace();
		}
			 System.out.println("6");
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


	public List<EmployeeData> getEmpPayrollDataForDataRange(LocalDate startDate, LocalDate endDate) {
		String  sql=String.format("select * from employee_payroll where start_date between '%s' AND '%s';",
				Date.valueOf(startDate),Date.valueOf(endDate));
		return getEmployeePayrollDataUsinDB(sql);
	}

	public List<EmployeeData> getList( ResultSet resultSet) throws SQLException
	{
		List<EmployeeData> employeePayrollList= new ArrayList<>();
		while(resultSet.next()) {
			employeePayrollList.add(new EmployeeData(resultSet.getInt("id"),resultSet.getString("name"),
					resultSet.getDouble("salary"),resultSet.getDate("start_date"),resultSet.getString("gender"),resultSet.getString("phoneNumber")
					,resultSet.getString("address"),resultSet.getString("department") ));
		}
		return employeePayrollList;
	}

	private List<EmployeeData> getEmployeePayrollDataUsinDB(String sql) {
		List<EmployeeData> employeePayrollList= new ArrayList<>();
		try(Connection connection = this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			employeePayrollList=this.getList(resultSet);
					}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println(employeePayrollList);
	   return employeePayrollList;	}


	public Map<String, Double> getAverageSalaryByGender() {
		String  sql=String.format("select gender,AVG(salary) from employee_payroll group by gender;");
		Map<String,Double>genderToAverageSalaryMap=new HashMap<>();
		try (Connection connection=getConnection()){
			Statement preparedStatement = connection.createStatement();
			ResultSet resultSet=preparedStatement.executeQuery(sql);
			while (resultSet.next()) {
				genderToAverageSalaryMap.put(resultSet.getString(1), resultSet.getDouble(2));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return genderToAverageSalaryMap;
	}


	public Double getmaximumSalary() {
		double maximumSalary = 0;
		try(Connection connection=getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("select max(salary) from employee_payroll");
			while(result.next()) {
				maximumSalary = result.getInt(1);
				break;
			}
		} catch (SQLException exception) {
               exception.printStackTrace();
         }
		return maximumSalary;
	}


	public EmployeeData addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) throws SQLException {
		int emp_id = -1;
		EmployeeData employeeData = null;
		String sql = String.format("insert into employee_payroll(name,salary,start_date,gender)  values ('%s','%s','%s','%s');",name,salary,Date.valueOf(startDate),gender);
		Connection connection = null;
		try
		{
			 connection = this.getConnection();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
		try {
			Statement statement = connection.createStatement();
			int rowAffected = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1)
			{
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) emp_id = resultSet.getInt(1);

			}
			employeeData = new EmployeeData(emp_id, name, salary, Date.valueOf(startDate));
					} catch (SQLException exception) {
						System.out.println("exception occured");
               exception.printStackTrace();
         }
	

		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = taxablePay = tax;
			String sql1 = String
					.format("INSERT INTO payroll_details(emp_id,basic_pay,deductions,taxable_pay,tax,net_pay) values "
							+ "('%s','%s','%s','%s','%s','%s');", emp_id, salary, deductions, taxablePay, tax, netPay);
			int rowAffected = statement.executeUpdate(sql1);
			if (rowAffected == 1) {
				employeeData = new EmployeeData(emp_id, name, salary, Date.valueOf(startDate));
			}
		} catch (SQLException s) {
				s.printStackTrace();
			}
		finally {
			if(connection != null) connection.close();
		}
		
		return employeeData;
	}
	
}
