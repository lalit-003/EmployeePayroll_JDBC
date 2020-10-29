package com.bridgelabz.jdbc.JDBC;

import java.sql.Date;

public class EmployeeData {

	private String name;
	private int id;
	private double salary;
	private Date start;

	public EmployeeData(int id, String name, double salary, Date start) {
              this.id = id;
              this.name = name;
              this.salary= salary;
              this.start=start;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Name=" + name + ", ID=" + id + ", Salary=" + salary + ", Start_date=" + start;
	}
}
