/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class BeneficiaryFamilyMembers {

	public int beneficiaryId;
	public int familyMemId;
	public String familyMemFirstName;
	public String familyMemLastName;
	public int relationId;
	public String relationDesc;
	public String familyMemPic;
	public int familyMemBioId;
	public int tempFamilyMemId;
	public boolean editMode;
	public String	iconStyle;
	public boolean cancelled;
	public int createdBy;
	public int respCode;
	
	public BeneficiaryFamilyMembers(int respCode) {
		super();
		this.respCode = respCode;
	}

	public BeneficiaryFamilyMembers(int familyMemId, String familyMemFirstName,
			String familyMemLastName, int relationId, String familyMemPic,int familyMemBioId,
			int respCode) {
		super();
		this.familyMemId = familyMemId;
		this.familyMemFirstName = familyMemFirstName;
		this.familyMemLastName = familyMemLastName;
		this.relationId = relationId;
		this.familyMemPic = familyMemPic;
		this.familyMemBioId=familyMemBioId;
		this.respCode = respCode;
	}

	public BeneficiaryFamilyMembers() {
		super();
		// TODO Auto-generated constructor stub
	}
}
