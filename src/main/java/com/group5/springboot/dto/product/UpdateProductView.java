package com.group5.springboot.dto.product;

public final class UpdateProductView {
	private final Integer p_ID;
	private final String p_Name;
	private final String p_Class;
	private final Integer p_Price;
	private final String descString;


	public UpdateProductView(Integer p_ID, String p_Name, String p_Class, Integer p_Price, String descString) {
		this.p_ID = p_ID;
		this.p_Name = p_Name;
		this.p_Class = p_Class;
		this.p_Price = p_Price;
		this.descString = descString;
	}


	public Integer getP_ID() {
		return p_ID;
	}

	public String getP_Name() {
		return p_Name;
	}

	public String getP_Class() {
		return p_Class;
	}

	public Integer getP_Price() {
		return p_Price;
	}

	public String getDescString() {
		return descString;
	}


	@Override
	public String toString() {
		return "CreateProductView{" +
			   "p_ID='" + p_ID + '\'' +
			   ", p_Name='" + p_Name + '\'' +
			   ", p_Class='" + p_Class + '\'' +
			   ", p_Price=" + p_Price +
			   ", descString='" + descString + '\'' +
			   '}';
	}
}