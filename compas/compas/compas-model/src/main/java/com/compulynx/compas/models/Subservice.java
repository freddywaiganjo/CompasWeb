package com.compulynx.compas.models;


public class Subservice {
	
	private int id;
	private int parentType;
	private int parentId;
	private int level;
	private String name;
	private String children;
	private ServiceDtlVO serviceDtl;
	private Subservice child;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentType() {
		return parentType;
	}
	public void setParentType(int parentType) {
		this.parentType = parentType;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChildren() {
		return children;
	}
	public void setChildren(String children) {
		this.children = children;
	}
	public ServiceDtlVO getServiceDtl() {
		return serviceDtl;
	}
	public void setServiceDtl(ServiceDtlVO serviceDtl) {
		this.serviceDtl = serviceDtl;
	}
	public Subservice getChild() {
		return child;
	}
	public void setChild(Subservice child) {
		this.child = child;
	}
	
}
