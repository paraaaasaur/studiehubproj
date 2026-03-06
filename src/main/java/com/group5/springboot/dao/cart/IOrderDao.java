package com.group5.springboot.dao.cart;

import java.util.*;

import com.group5.springboot.model.cart.OrderInfo;

public interface IOrderDao {
	Map<String, Object> selectTop100();

	Map<String, Object> insert(OrderInfo orderBean);

	Integer getCurrentIdSeed();

	Boolean selectIfBoughtOrNot(Integer p_id, String u_id);

	Map<String, Object> selectLikeOperator(String condition, String value);

	Map<String, Object> selectBy(String condition, String value);

	Map<String, Object> selectWithTimeRange(String startTime, String endTime);

	Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue);

	Map<String, Object> select(OrderInfo orderBean);
}