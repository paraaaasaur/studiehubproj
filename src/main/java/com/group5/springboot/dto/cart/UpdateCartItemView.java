package com.group5.springboot.dto.cart;

public final class UpdateCartItemView {
	private final Integer cart_id;
	private final Integer p_id;
	private final String p_name;
	private final Integer p_price;
	private final String u_id;
	private final String u_firstname;
	private final String u_lastname;
	private final String cart_date;


	private UpdateCartItemView(Integer cart_id, Integer p_id, String p_name, Integer p_price, String u_id, String u_firstname, String u_lastname, String cart_date) {
		this.cart_id = cart_id;
		this.p_id = p_id;
		this.p_name = p_name;
		this.p_price = p_price;
		this.u_id = u_id;
		this.u_firstname = u_firstname;
		this.u_lastname = u_lastname;
		this.cart_date = cart_date;
	}

	public static UpdateCartItemView newInstance(Integer cart_id) {
		return new UpdateCartItemView(cart_id, 
				null, null, null, null, null, null, null);
	}


	// withers
	public UpdateCartItemView withP_id(Integer p_id) {
		return new UpdateCartItemView(
				this.cart_id,
				p_id,
				this.p_name,
				this.p_price,
				this.u_id,
				this.u_firstname,
				this.u_lastname,
				this.cart_date
		);
	}
	
	public UpdateCartItemView withP_name(String p_name) {
		return new UpdateCartItemView(
				this.cart_id,
				this.p_id,
				p_name,
				this.p_price,
				this.u_id,
				this.u_firstname,
				this.u_lastname,
				this.cart_date
		);
	}
	
	public UpdateCartItemView withP_price(Integer p_price) {
		return new UpdateCartItemView(
				this.cart_id,
				this.p_id,
				this.p_name,
				p_price,
				this.u_id,
				this.u_firstname,
				this.u_lastname,
				this.cart_date
		);
	}
	
	public UpdateCartItemView withU_id(String u_id) {
		return new UpdateCartItemView(
				this.cart_id,
				this.p_id,
				this.p_name,
				this.p_price,
				u_id,
				this.u_firstname,
				this.u_lastname,
				this.cart_date
		);
	}
	
	public UpdateCartItemView withU_firstname(String u_firstname) {
		return new UpdateCartItemView(
				this.cart_id,
				this.p_id,
				this.p_name,
				this.p_price,
				this.u_id,
				u_firstname,
				this.u_lastname,
				this.cart_date
		);
	}
	
	public UpdateCartItemView withU_lastname(String u_lastname) {
		return new UpdateCartItemView(
				this.cart_id,
				this.p_id,
				this.p_name,
				this.p_price,
				this.u_id,
				this.u_firstname,
				u_lastname,
				this.cart_date
		);
	}

	public UpdateCartItemView withCart_date(String cart_date) {
		return new UpdateCartItemView(
				this.cart_id,
				this.p_id,
				this.p_name,
				this.p_price,
				this.u_id,
				this.u_firstname,
				this.u_lastname,
				cart_date
		);
	}

	// getters
	public Integer getCart_id() {
		return cart_id;
	}

	public Integer getP_id() {
		return p_id;
	}

	public String getP_name() {
		return p_name;
	}

	public Integer getP_price() {
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

	public String getCart_date() {
		return cart_date;
	}


	@Override
	public String toString() {
		return "UpdateCartItemView{" +
			   "cart_id=" + cart_id +
			   ", p_id=" + p_id +
			   ", p_name='" + p_name + '\'' +
			   ", p_price=" + p_price +
			   ", u_id='" + u_id + '\'' +
			   ", u_firstname='" + u_firstname + '\'' +
			   ", u_lastname='" + u_lastname + '\'' +
			   ", cart_date='" + cart_date + '\'' +
			   '}';
	}
}