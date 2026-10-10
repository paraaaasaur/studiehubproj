package com.group5.springboot.dto.cart;

public final class OrderSearchCriteria {
	private final String condition;
	private final String value;


	public OrderSearchCriteria(String searchBy, String searchBar) {
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
		return "OrderSearchCriteria{" +
			   "condition='" + condition + '\'' +
			   ", value='" + value + '\'' +
			   '}';
	}
}
