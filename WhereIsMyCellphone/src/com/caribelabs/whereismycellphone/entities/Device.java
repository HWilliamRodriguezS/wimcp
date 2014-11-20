package com.caribelabs.whereismycellphone.entities;

public class Device {
	
	private int id;
	private String name;
	private String registration_id;
	private String phonenumber;
	private String sumary;
	private int user_id;
	
	public Device(){
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRegistration_id() {
		return registration_id;
	}
	
	public void setRegistration_id(String registration_id) {
		this.registration_id = registration_id;
	}
	
	public String getPhonenumber() {
		return phonenumber;
	}
	
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	public String getSumary() {
		return sumary;
	}
	
	public void setSumary(String sumary) {
		this.sumary = sumary;
	}
	
	public int getUser_id() {
		return user_id;
	}
	
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
}
