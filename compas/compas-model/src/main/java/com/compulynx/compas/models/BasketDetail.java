/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class BasketDetail {
	public int basketId;
	public int serviceId;
	public String serviceName;
	public double quantity;
	public double price;
	public boolean editMode;
	public String	iconStyle;
	public boolean cancelled;
	public int createdBy;
	public int respCode;

	public BasketDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BasketDetail(int respCode) {
		super();
		this.respCode=respCode;
		// TODO Auto-generated constructor stub
	}
	public BasketDetail(int basketId, int serviceId, String serviceName,
			double quantity, double price, int respCode) {
		super();
		this.basketId = basketId;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.quantity = quantity;
		this.price = price;
	
		this.respCode = respCode;
	}
}
