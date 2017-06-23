package com.compulynx.compas.models;

import java.util.List;

public class Organization {
	public int organizationId;
	public String organizationName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public List<Wallet> wallets;

	public Organization(int respCode) {
		super();
		this.respCode = respCode;
	}

	public Organization(int organizationId, String organizationName, int createdBy, int respCode) {
		super();
		this.organizationId = organizationId;
		this.organizationName = organizationName;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}

	public Organization() {
		super();
	}

}
