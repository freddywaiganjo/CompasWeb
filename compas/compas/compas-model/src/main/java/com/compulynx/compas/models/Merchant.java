/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Merchant {

	public int merchantId;
	public String merchantName;
	public String merchantCode;
	public int organizationId;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean self;
	public int count;
	
	public Merchant(int merchantId, String merchantName, boolean active,
			int createdBy, int respCode,String merchantCode,int count) {
		super();
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.merchantCode=merchantCode;
		this.count=count;
	}
	public Merchant(int respCode) {
		super();
		this.respCode = respCode;
	}
	public Merchant() {
		super();
		// TODO Auto-generated constructor stub
	}
}
