package com.group5.springboot.dto.cart;

public final class CreateCartItemRequest {
	private final Integer p_id;
	private final String u_id; // target user ID, not login user ID


	public CreateCartItemRequest(Integer p_id, String u_id) {
		this.p_id = p_id;
		this.u_id = u_id;
	}


	public Integer getP_id() {
		return p_id;
	}

	public String getU_id() {
		return u_id;
	}


	@Override
	public String toString() {
		return "CreateCartItemRequest{" +
			   "p_id=" + p_id +
			   ", u_id='" + u_id + '\'' +
			   '}';
	}
}
