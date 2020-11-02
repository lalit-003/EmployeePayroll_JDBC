package com.bridgelabz.jdbc.JDBC;

import java.sql.Date;
import java.time.LocalDate;

public class EmployeeData {

	private String name;
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setName(String name) {
		this.name = name;
	}

	private double salary;
	private Date start;
	private String gender;
	private String address;
	private String phoneNumber;
	private String department;
	
	public EmployeeData( String name, double salary) {
        this.name = name;
        this.salary= salary;
}

	public EmployeeData(int id, String name, double salary, Date start) {
              this.id = id;
              this.name = name;
              this.salary= salary;
              this.start=start;
	}

	public EmployeeData(int id, String name, String gender, double salary, LocalDate start) {
            this(id,name,salary,Date.valueOf(start));
            this.setGender(gender);
	}

	public EmployeeData(int id, String name, double salary, Date start, String gender, String phoneNumber,
			String address, String department){
          this(id,name,gender,salary,start.toLocalDate());
          this.phoneNumber = phoneNumber;
          this.address = address;
          this.department = department;
	}

	public EmployeeData(int id, String name, double salary, LocalDate start, String gender, String phoneNumber, String address,
			String department) {
		this(id,name,salary,Date.valueOf(start));
		this.gender = gender;
		 this.phoneNumber = phoneNumber;
         this.address = address;
         this.department = department;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Name=" + name + ", ID=" + id + ", Salary=" + salary + ", Start_date=" + start+ ", "
				+ " Gender = " +gender +",phoneNumber = "+phoneNumber +",address = "+address +",department = "+department;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this==o) return true;
		if(o == null|| getClass() != o.getClass()) return false;
		EmployeeData that = (EmployeeData) o;
		return id == that.id && Double.compare(that.salary, salary) == 0 && name.equals(that.name) &&
				       gender.equals(that.gender) && address.equals(that.address) && phoneNumber.equals(that.phoneNumber)
				       && department.equals(department);
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}
