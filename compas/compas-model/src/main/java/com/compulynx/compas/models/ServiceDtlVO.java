package com.compulynx.compas.models;

public class ServiceDtlVO {
	
	private int id;
	private String subService;
	private String market;
	private String charge;
	private String param;
	private String type;
	
	public int getServiceId() {
		return id;
	}
	public void setServiceId(int id) {
		this.id = id;
	}
	public String getSubService() {
		return subService;
	}
	public void setSubService(String subService) {
		this.subService = subService;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}