/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author shree
 *
 */
public class TmsTransactionMst {

	public String voucher;
	public int transaction_type;
	public int cycle;
	public String value_remaining;
	public String quantity_remaining;
	public String total_amount_charged_by_retailer;
	public double cancelled_transaction;
	public String pos_terminal;
	public String receipt_number;
	public String user;
	public String timestamp_transaction_created;
	public int authentication_type;
	public String latitude;
	public String longitude;
	public String uid;
	public String cardNumber;
	public int createdBy;
	public int respCode;
	public List<TmsTransactionDtl> commodities;
	public String rationNo;
}
