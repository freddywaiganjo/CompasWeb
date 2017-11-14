package com.compulynx.compas.models;

public class CompasResponse {

	public CompasResponse() {
		super();
	}

	public int respCode;
	public String respMessage;
	public int txnId;
	public double amount;
	public String cust_name;
	public int customerId;
	public String cardNumber;
	public CompasResponse(int respCode, String respMessage, int txnId,double amount,String cust_name) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.txnId = txnId;
		this.amount=amount;
		this.cust_name=cust_name;
	}
	public CompasResponse(int respCode, String respMessage) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
	}
	public CompasResponse(int respCode) {
		super();
		this.respCode = respCode;
	}
	public CompasResponse(int respCode, String respMessage, int customerId,String cust_name) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.customerId = customerId;
		this.cust_name=cust_name;
	}
	public CompasResponse(int respCode, String respMessage, String cardNumber, String cust_name) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.cardNumber = cardNumber;
		this.cust_name=cust_name;
	}
}
