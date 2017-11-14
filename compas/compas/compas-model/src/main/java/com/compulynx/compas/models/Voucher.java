/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author shree
 *
 */
public class Voucher {

	public Voucher(int voucherId, String voucherDesc, boolean isActive,
			double voucherValue, String frqSelect, String modeSelect) {
		super();
		this.voucherId = voucherId;
		this.voucherDesc = voucherDesc;
		this.isActive = isActive;
		this.voucherValue = voucherValue;
		this.frqSelect = frqSelect;
		this.modeSelect = modeSelect;
	}
	public int voucherId;
	public String voucherCode;
	public String voucherDesc;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean isActive;
	public boolean expanded;
	public List<Service> services;
	public double voucherValue;
	public String voucherType;
	public String currency;
	public String frqSelect;
	public String modeSelect;
	public  String startDate;
	public  String endDate;
	
	public Voucher() {
		super();
		// TODO Auto-generated constructor stub
	}
}
