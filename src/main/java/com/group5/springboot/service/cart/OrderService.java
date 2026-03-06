package com.group5.springboot.service.cart;

import com.group5.springboot.model.cart.OrderInfo;

import java.util.Map;

public interface OrderService {
	Integer getCurrentIdSeed();

	Boolean selectIfBoughtOrNot(Integer p_id, String u_id);

	Map<String, Object> insert(OrderInfo order);

	Map<String, Object> selectLikeOperator(String condition, String value);

	Map<String, Object> selectBy(String condition, String value);

	Map<String, Object> selectWithTimeRange(String startTime, String endTime);

	Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue);

	Map<String, Object> select(OrderInfo orderBean);

	Map<String, Object> selectTop100();
}