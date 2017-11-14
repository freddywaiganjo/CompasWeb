/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Transaction {

	public int txnId;
	public int txnType;
	public String cardNo;
	public String billNo;
	public String txnAmount;
	public String runningBalance;
	public String serviceId;
	public String subServiceId;
	public String programmeId;
	public String paramId;
	public String deviceId;
	public String userId;
	public String txnTime;
	public int createdBy;
	public String createdOn;
	public int respCode;
	public List<Params> params;
	public String paymentmethod;
	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Transaction(int txnId, int txnType, String billNo, String txnAmount,
			String balance, int createdBy, String createdOn, int respCode) {
		super();
		this.txnId = txnId;
		this.txnType = txnType;
		this.billNo = billNo;
		this.txnAmount = txnAmount;
		this.runningBalance = balance;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.respCode = respCode;
	}
}
