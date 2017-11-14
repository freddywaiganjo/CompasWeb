/**
 * 
 */
package com.compulynx.compas.models.android;

import java.util.ArrayList;

/**
 * @author shree
 *
 */
public class AndroidProducts {

	public int productId;
	public String productCode;
	public String productName;
	public String productDesc;
	public String uom;
	public int categoryId;
	public String image;
	public int serviceId;
	public double minPrice;
	public double maxPrice;
	public ArrayList<AndPrice> priceDetails;
	public int locationId;
}
