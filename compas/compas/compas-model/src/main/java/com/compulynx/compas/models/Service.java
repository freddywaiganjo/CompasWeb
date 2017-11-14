/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Service {

	public Service(int serviceId, String serviceName, boolean isActive, int createdBy,
			int respCode,double quantity,double serviceValue,String compoName,String compoType) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.quantity=quantity;
		this.serviceValue=serviceValue;
		this.compoName=compoName;
		this.count=count;
		this.compoType=compoType;
		
	}
	public Service(int serviceId, String serviceName, boolean isActive, int createdBy,
			int respCode) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.respCode = respCode;
		
	}
	public Service(int serviceId,String serviceName,double serviceValue
			) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceValue=serviceValue;
		
	}

	public int parentServiceId;
	public int level;
	public boolean hasChild;
	public int serviceId;
	public String serviceCode;
	public String serviceName;
	public double quantity;
	public double price;
	public double serviceValue;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean isActive;
	public List<Params> params;
	public String compoType;
	public String compoName;
	public int count;
	public int categoryId;
	public String serviceDesc;
	public String categoryName;
	public String image;
	public String uom;
	public double minPrice;
	public double maxPrice;
	public int voucherId;
	public double unit;		
		public Service() {
		super();
	}
	public Service(int respCode) {
		super();
		this.respCode=respCode;
	}

	public Service(int serviceId, String serviceCode, String serviceName,
			boolean active, int createdBy, int respCode,int level,
			boolean hasChild,double serviceValue,int count) {
		super();
		this.serviceId = serviceId;
		this.serviceCode = serviceCode;
		this.serviceName = serviceName;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.level=level;
		this.hasChild=hasChild;
		this.serviceValue=serviceValue;
		
	}

	public Service(int serviceId, String serviceCode, String serviceName,boolean active,
			int createdBy, int respCode,String compoName,String compoType,int count,
			int categoryId,String categoryName,String image,String serviceDesc,double minPrice,double maxPrice) {
		super();
		this.serviceId = serviceId;
		this.serviceCode=serviceCode;
		this.serviceName = serviceName;
		this.active=active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.compoName=compoName;
		this.compoType=compoType;
		this.count=count;
		this.categoryId=categoryId;
		this.categoryName=categoryName;
		this.image=image;
		this.serviceDesc=serviceDesc;
		this.minPrice=minPrice;
		this.maxPrice=maxPrice;
				
	}

}
