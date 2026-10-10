package com.group5.springboot.dto.cart;

public final class CreateCartItemView {
	private final Integer p_id;
	private final String p_name;
	private final Integer p_price;
	private final String u_id;
	private final String u_firstname;
	private final String u_lastname;


	private CreateCartItemView(Integer p_id, String p_name, Integer p_price, String u_id, String u_firstname, String u_lastname) {
		this.p_id = p_id;
		this.p_name = p_name;
		this.p_price = p_price;
		this.u_id = u_id;
		this.u_firstname = u_firstname;
		this.u_lastname = u_lastname;
	}

	public static CreateCartItemView newInstance() {
		return new CreateCartItemView(null, null, null, null, null, null);
	}


	// withers
	public CreateCartItemView withP_id(Integer p_id) {
		return new CreateCartItemView(
				p_id, 
				this.p_name, 
				this.p_price, 
				this.u_id, 
				this.u_firstname, 
				this.u_lastname
		);
	}

	public CreateCartItemView withP_name(String p_name) {
		return new CreateCartItemView(
				this.p_id,
				p_name,
				this.p_price,
				this.u_id,
				this.u_firstname,
				this.u_lastname
		);
	}

	public CreateCartItemView withP_price(Integer p_price) {
		return new CreateCartItemView(
				this.p_id,
				this.p_name,
				p_price,
				this.u_id,
				this.u_firstname,
				this.u_lastname
		);
	}

	public CreateCartItemView withU_id(String u_id) {
		return new CreateCartItemView(
				this.p_id,
				this.p_name,
				this.p_price,
				u_id,
				this.u_firstname,
				this.u_lastname
		);
	}

	public CreateCartItemView withU_firstname(String u_firstname) {
		return new CreateCartItemView(
				this.p_id,
				this.p_name,
				this.p_price,
				this.u_id,
				u_firstname,
				this.u_lastname
		);
	}

	public CreateCartItemView withU_lastname(String u_lastname) {
		return new CreateCartItemView(
				this.p_id,
				this.p_name,
				this.p_price,
				this.u_id,
				this.u_firstname,
				u_lastname
		);
	}


	// getters
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


	@Override
	public String toString() {
		return "CreateCartItemView{" +
			   "p_id=" + p_id +
			   ", p_name='" + p_name + '\'' +
			   ", p_price=" + p_price +
			   ", u_id='" + u_id + '\'' +
			   ", u_firstname='" + u_firstname + '\'' +
			   ", u_lastname='" + u_lastname + '\'' +
			   '}';
	}
}