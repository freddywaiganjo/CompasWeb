package com.compulynx.compas.models;

import java.util.List;

public class Basket {
	public int basketId;
	public String basketCode;
	public String basketName;
	public boolean active;
	public double basketValue;
	public int createdBy;
	public int respCode;
	public List<BasketDetail> services;
	
	public Basket(int basketId, String basketCode, String basketName,
			boolean active,double basketValue, int createdBy, int respCode) {
		super();
		this.basketId = basketId;
		this.basketCode = basketCode;
		this.basketName = basketName;
		this.active = active;
		this.basketValue=basketValue;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}

	/**
	 * 
	 */
	public Basket() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Basket(int respCode) {
		super();
		this.respCode=respCode;
		// TODO Auto-generated constructor stub
	}
}
