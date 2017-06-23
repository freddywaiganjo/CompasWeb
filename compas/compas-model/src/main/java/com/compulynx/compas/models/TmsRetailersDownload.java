/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author shree
 *
 */
public class TmsRetailersDownload {

	public String retailer_name;
	public String location;
	public String contact_name;
	public String phone_number;
	public String mobile_number;
	public TmsRetailersDownload(String retailer_name, String location,
			String contact_name, String phone_number, String mobile_number) {
		super();
		this.retailer_name = retailer_name;
		this.location = location;
		this.contact_name = contact_name;
		this.phone_number = phone_number;
		this.mobile_number = mobile_number;
	}
	public TmsRetailersDownload() {
		super();
		// TODO Auto-generated constructor stub
	}
}
