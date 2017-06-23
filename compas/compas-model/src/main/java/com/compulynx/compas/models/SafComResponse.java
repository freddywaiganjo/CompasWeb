package com.compulynx.compas.models;

public class SafComResponse {

	public int respCode;
	public String respMessage;
	public int userId;
	public String uName;
	public String market;
	public String payBill;
	public String posVersion;
	public String ftpUrl;
	public int agentId;

	public SafComResponse(int respCode, String respMessage, int userId, String uname, String market, String payBill,String posVersion,String ftpUrl,int agentId) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.userId = userId;
		this.uName = uname;
		this.market = market;
		this.payBill = payBill;
		this.posVersion=posVersion;
		this.ftpUrl=ftpUrl;
		this.agentId=agentId;
	}


	public SafComResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
}
