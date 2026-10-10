package com.group5.springboot.dto.cart;

public final class CartItemSearchCriteria {
	private final String condition;
	private final String value;


	public CartItemSearchCriteria(String searchBy, String searchBar) {
		this.condition = searchBy;
		this.value = searchBar;
	}


	public String getCondition() {
		return condition;
	}
	public String getValue() {
		return value;
	}


	@Override
	public String toString() {
		return "CartItemSearchCriteria{" +
			   "condition='" + condition + '\'' +
			   ", value='" + value + '\'' +
			   '}';
	}
}
