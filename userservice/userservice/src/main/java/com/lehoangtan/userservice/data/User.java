package com.lehoangtan.userservice.data;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name ="username")
	private String username;
	
	@Column(name ="password")
	private String password;
	
	@Column(name ="employeeId")
	private String employeeId;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public User(Long id, String username, String password, String employeeId) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.employeeId = employeeId;
	}
	public User() {
		super();
	}
	
	
	
	
}
