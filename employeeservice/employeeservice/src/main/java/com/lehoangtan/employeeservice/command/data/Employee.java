package com.lehoangtan.employeeservice.command.data;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
	@Id
	private String employeeId;
	
	@Column(name ="firstname")
	private String firstName;
	
	@Column(name ="lastName")
	private String lastName;
	
	@Column(name ="kin")
	private String kin;
	
	@Column(name ="isDisciplined")
	private Boolean isDisciplined;
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getKin() {
		return kin;
	}
	public void setKin(String kin) {
		this.kin = kin;
	}
	public Boolean getIsDisciplined() {
		return isDisciplined;
	}
	public void setIsDisciplined(Boolean isDisciplined) {
		this.isDisciplined = isDisciplined;
	}
}