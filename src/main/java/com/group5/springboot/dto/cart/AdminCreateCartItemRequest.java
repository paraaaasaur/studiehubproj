package com.group5.springboot.dto.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;

public final class AdminCreateCartItemRequest {
	private final int p_id;
	private final String p_name;
	private final int p_price;
	private final String u_id;
	private final String u_firstname;
	private final String u_lastname;


	@JsonCreator
	public AdminCreateCartItemRequest(int p_id, String p_name, int p_price, String u_id, String u_firstname, String u_lastname) {
		this.p_id = p_id;
		this.p_name = p_name;
		this.p_price = p_price;
		this.u_id = u_id;
		this.u_firstname = u_firstname;
		this.u_lastname = u_lastname;
	}

	public AdminCreateCartItemRequest(ProductInfo product, User_Info customer) {
		this.p_id = product.getP_ID();
		this.p_name = product.getP_Name();
		this.p_price = product.getP_Price();
		this.u_id = customer.getU_id();
		this.u_firstname = customer.getU_firstname();
		this.u_lastname = customer.getU_lastname();
	}


	public int getP_id() {
		return p_id;
	}

	public String getP_name() {
		return p_name;
	}

	public int getP_price() {
		return p_price;
	}

	public String getU_id() {
		return u_id;
	}

	public String getU_firstname() {
		return u_firstname;
	}

	public String getU_lastname() {
		return u_lastname;
	}


	@Override
	public String toString() {
		return "AdminCreateCartItemRequest{" +
			   "p_id=" + p_id +
			   ", p_name='" + p_name + '\'' +
			   ", p_price=" + p_price +
			   ", u_id='" + u_id + '\'' +
			   ", u_firstname='" + u_firstname + '\'' +
			   ", u_lastname='" + u_lastname + '\'' +
			   '}';
	}
}
