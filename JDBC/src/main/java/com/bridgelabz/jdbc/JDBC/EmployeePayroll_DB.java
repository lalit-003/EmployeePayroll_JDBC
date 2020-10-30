package com.bridgelabz.jdbc.JDBC;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public List<EmployeeData> getEmpPayrollDataForDataRange(LocalDate startDate, LocalDate endDate) {
		return employeePayrollDBService.getEmpPayrollDataForDataRange( startDate,  endDate);
	}

	public Map<String, Double> readAverageSalaryByGender() {
		return employeePayrollDBService.getAverageSalaryByGender();
	}

	public Double getMaxSalary() {
		return employeePayrollDBService.getmaximumSalary();
	}

	public void addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) throws SQLException {
     employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name,salary,startDate,gender));		
	}

	int count1=0;
	public void addEmployeeToPayroll(List<EmployeeData> asList) {
		asList.forEach(employeePayrollData -> {
			System.out.println("Employee Being Added : " + employeePayrollData.getName());
			try {
				this.addEmployeeToPayroll(employeePayrollData.getName(),
						employeePayrollData.getSalary(), employeePayrollData.getStart().toLocalDate()
                        , employeePayrollData.getGender());
				count1++;
				System.out.println("Employee Added: " + employeePayrollData.getName());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}); 
		System.out.println(this.employeePayrollList);		
	}

	public long countEntries() {
		return employeePayrollList.size();
	}
	int count2=0;
	
	public int addEmployeesToPayrollWithThreads(List<EmployeeData> asList) {
		System.out.println(count1);
		Map<Integer,Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		asList.forEach(employeePayrollData ->{
			Runnable task = () -> {
				employeeAdditionStatus.put(employeePayrollData.hashCode(),false);
				System.out.println("Employee Being Added: "+Thread.currentThread().getName());
				try {
					this.addEmployeeToPayroll(employeePayrollData.getName(),
							employeePayrollData.getSalary(), employeePayrollData.getStart().toLocalDate()
	                        , employeePayrollData.getGender());
					employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
					System.out.println("Employee Added : "+Thread.currentThread().getName());
					count2++;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			};
			Thread thread = new Thread(task,employeePayrollData.getName());
			thread.start();

		});
		while(employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(count1+count2);
		System.out.println(this.employeePayrollList);	
		return count1+count2;
	}

	
	
}

