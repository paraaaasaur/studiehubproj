package com.group5.springboot.dto.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;

public final class AdminUpdateCartItemRequest {
	private final int cart_id;
	private final int p_id;
	private final String p_name;
	private final int p_price;
	private final String u_id;
	private final String u_firstname;
	private final String u_lastname;


	@JsonCreator
	AdminUpdateCartItemRequest(int cart_id, int p_id, String p_name, int p_price, String u_id, String u_firstname, String u_lastname) {
		this.cart_id = cart_id;
		this.p_id = p_id;
		this.p_name = p_name;
		this.p_price = p_price;
		this.u_id = u_id;
		this.u_firstname = u_firstname;
		this.u_lastname = u_lastname;
	}

	private AdminUpdateCartItemRequest(Builder builder) {
		cart_id = builder.cart_id;
		p_id = builder.p_id;
		p_name = builder.p_name;
		p_price = builder.p_price;
		u_id = builder.u_id;
		u_firstname = builder.u_firstname;
		u_lastname = builder.u_lastname;
	}


	public int getCart_id() {
		return cart_id;
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
		return "AdminUpdateCartItemRequest{" +
			   "cart_id=" + cart_id +
			   ", p_id=" + p_id +
			   ", p_name='" + p_name + '\'' +
			   ", p_price=" + p_price +
			   ", u_id='" + u_id + '\'' +
			   ", u_firstname='" + u_firstname + '\'' +
			   ", u_lastname='" + u_lastname + '\'' +
			   '}';
	}


	public static final class Builder {
		private final int cart_id;
		private final int p_id;
		private final String p_name;
		private final int p_price;
		private final String u_id;
		private final String u_firstname;
		private final String u_lastname;

		private Builder(int cart_id, int p_id, String p_name, int p_price, String u_id, String u_firstname, String u_lastname) {
			this.cart_id = cart_id;
			this.p_id = p_id;
			this.p_name = p_name;
			this.p_price = p_price;
			this.u_id = u_id;
			this.u_firstname = u_firstname;
			this.u_lastname = u_lastname;
		}

		private Builder(Builder builder, ProductInfo newProduct) {
			this.cart_id = builder.cart_id;
			this.p_id = newProduct.getP_ID();
			this.p_name = newProduct.getP_Name();
			this.p_price = newProduct.getP_Price();
			this.u_id = builder.u_id;
			this.u_firstname = builder.u_firstname;
			this.u_lastname = builder.u_lastname;
		}

		private Builder(Builder builder, User_Info newCustomer) {
			this.cart_id = builder.cart_id;
			this.p_id = builder.p_id;
			this.p_name = builder.p_name;
			this.p_price = builder.p_price;
			this.u_id = newCustomer.getU_id();
			this.u_firstname = newCustomer.getU_firstname();
			this.u_lastname = newCustomer.getU_lastname();
		}

		private Builder(CartItem entity) {
			this.cart_id = entity.getCart_id();
			this.p_id = entity.getProductInfo().getP_ID();
			this.p_name = entity.getProductInfo().getP_Name();
			this.p_price = entity.getProductInfo().getP_Price();
			this.u_id = entity.getUser_Info().getU_id();
			this.u_firstname = entity.getUser_Info().getU_firstname();
			this.u_lastname = entity.getUser_Info().getU_lastname();
		}

		public static Builder from(CartItem oldEntity) {
			return new Builder(oldEntity);
		}

		public Builder newProduct(ProductInfo newProduct) {
			return new Builder(this, newProduct);
		}

		public Builder newUser(User_Info newCustomer) {
			return new Builder(this, newCustomer);
		}

		public AdminUpdateCartItemRequest build() {
			return new AdminUpdateCartItemRequest(this);
		}
	}
}
