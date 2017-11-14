/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Product {

	public int productId;
	public String productCode;
	public String productName;
	public int merchantId;
	public String merchantName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int count;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(int productId, String productCode, String productName,
			int merchantId, String merchantName, boolean active,int count) {
		super();
		this.productId = productId;
		this.productCode = productCode;
		this.productName = productName;
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.active = active;
		this.count=count;
	}
}
