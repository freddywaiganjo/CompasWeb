package com.compulynx.compas.models;

public class SafComLogin {

	public String userName;
	public String password;
	public String deviceId;
	public String userType;
	public String cardNumber;
	public String oldPin;
	public String newPin;
	public int userId;
	public String agentPin;
	public String cardPin;
	public String licenseNumber;
	public SafComLogin(String userName, String password, String deviceId, String userType) {
		super();
		this.userName = userName;
		this.password = password;
		this.deviceId = deviceId;
		this.userType = userType;
	}
	public SafComLogin() {
		super();
		// TODO Auto-generated constructor stub
	}
}
