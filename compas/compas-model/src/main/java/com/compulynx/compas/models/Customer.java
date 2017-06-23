package com.compulynx.compas.models;

import java.util.List;



public class Customer {

	public int memberId;
	public int customerId;
	public String memberNo;
	public String firstName;
	public String surName;
	public String dateOfBirth;
	public int respCode;
	public String bioId;
	public String FileName;
	public int createdBy;
	public List<Customer> memberIdList;
	public boolean selected;
	public String memberPic;
	//For Jubilee
	public String title;
	public String otherName;
	public String idPassPortNo;
	public String gender;
	public String maritalStatus;
	public String weight;
	public String height;
	public String nhifNo;
	public String employerName;
	public String occupation;
	public String nationality;
	public String postalAdd;
	public String physicalAdd;
	public String homeTelephone ;
	public String officeTelephone;
	public String cellPhone;
	public String email ;
	public String cardStatus;
	public String uploadStatus;
	public int accType;
	public String cardNumber;
	public String serialNumber;
	public String expiryDate;
	public String status;
	public List<Programme> programmes;
	public String binRange;
	public int programmeId;
	public String programmeDesc;
	public List<BioImage> imageList;
	public List<BioImage> bioimages;
	public String bioImages;
	public String bioImage;
	public double cardBalance;
	public String cardSerialNo;
	public String cardPin;
	public int familySize;
	public int bnfGrpId;
	public String bnfStatus;
	public String verifyStatus;
	public String approvStatus;
	public String bioStatus;
	public String memType;
	public List<BeneficiaryFamilyMembers> familyMemList;
	public String fullName;
	public boolean isChecked;
	public String serialNo;
	public String right_thumb;
	public String right_index;
	public String left_thumb;
	public String left_index;
	public List<BulkSelectionIds> verifySelection;
	public String userName;
	public String userEmail;
	public int topupCount;
	public String bnfGrpName;
	public int locationId;
	public int branchId;
	public String pin;
	public int beneficiaryId;
	public int count;
	

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Customer(int respCode) {
		super();
		this.respCode = respCode;
	}
}
