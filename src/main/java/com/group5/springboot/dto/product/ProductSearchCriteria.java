package com.group5.springboot.dto.product;

public final class ProductSearchCriteria {
	private String pname;
	private String producttypename;
	private Boolean approved; // user-facing term for internal code p_Status


	public ProductSearchCriteria() {}


	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getProducttypename() {
		return producttypename;
	}

	public void setProducttypename(String producttypename) {
		this.producttypename = producttypename;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
}