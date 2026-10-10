package com.group5.springboot.dto.cart;

import com.group5.springboot.model.cart.CartItem;

public final class UpdateCartItemRequest {
	private final Integer cart_id; // from path variable
	private final Integer p_id;
	private final String u_id;


	/** 
	 * - Prefer {@link from} and withers to build a request when possible. <br>
	 * - Here in Spring 5.3.8, non-public constructors are not supported for data binding<br>
	 */
	public UpdateCartItemRequest(Integer cartid, Integer p_id, String u_id) {
		this.cart_id = cartid;
		this.p_id = p_id;
		this.u_id = u_id;
	}

	public static UpdateCartItemRequest from(CartItem entity) {
		return new UpdateCartItemRequest(
				entity.getCart_id(),
				entity.getP_id(),
				entity.getU_id()
		);
	}


	// withers
	public UpdateCartItemRequest withP_id(Integer p_id) {
		return new UpdateCartItemRequest(
				this.cart_id,
				p_id,
				this.u_id
		);
	}

	public UpdateCartItemRequest withU_id(String u_id) {
		return new UpdateCartItemRequest(
				this.cart_id,
				this.p_id,
				u_id
		);
	}

	// getters
	public Integer getCart_id() {
		return cart_id;
	}

	public Integer getP_id() {
		return p_id;
	}

	public String getU_id() {
		return u_id;
	}


	@Override
	public String toString() {
		return "UpdateCartItemRequest{" +
			   "cart_id=" + cart_id +
			   ", p_id=" + p_id +
			   ", u_id='" + u_id + '\'' +
			   '}';
	}
}