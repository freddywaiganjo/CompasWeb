/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Device {

	public int regId;
	public String serialNo;
	public int agentId;
	public String agentDesc;
	public int issueId;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int branchId;
	public int	count;
	public String licenseNumber;
	public boolean isActive;
	public Device() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Device(int regId, String serialNo,boolean active, int createdBy,
			int respCode) {
		super();
		this.regId = regId;
		this.serialNo = serialNo;
		this.active=active;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}
			
	public Device(int issueId,int regId, String serialNo, int agentId,  String agentDesc,
			int createdBy, int respCode,int branchID,String licenseNumber,boolean isActive) {
		super();
		this.issueId = issueId;
		this.regId = regId;
		this.serialNo = serialNo;
		this.agentId = agentId;
		this.agentDesc=agentDesc;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.branchId=branchID;
		this.licenseNumber=licenseNumber;
		this.isActive=isActive;
	}

}
