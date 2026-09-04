package com.group5.springboot.dto.product;

public final class CreateProductView {
	private final String u_ID;
	private final String p_Name;
	private final String p_Class;
	private final Integer p_Price;
	private final String descString;


	public CreateProductView(String u_ID, String p_Name, String p_Class, Integer p_Price, String descString) {
		this.u_ID = u_ID;
		this.p_Name = p_Name;
		this.p_Class = p_Class;
		this.p_Price = p_Price;
		this.descString = descString;
	}


	public static CreateProductView newInstance() {
		return new CreateProductView(null, "", "", 0, "");
	}


	public String getU_ID() {
		return u_ID;
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
		return "CreateProductForm{" +
			   "u_ID='" + u_ID + '\'' +
			   ", p_Name='" + p_Name + '\'' +
			   ", p_Class='" + p_Class + '\'' +
			   ", p_Price=" + p_Price +
			   ", descString='" + descString + '\'' +
			   '}';
	}
}