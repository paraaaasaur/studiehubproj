package com.group5.springboot.dao.cart;

import com.group5.springboot.model.cart.CartItem;

import java.util.Map;

public interface CartItemDao {
	@Deprecated
	Integer update(String newU_id, Integer newP_id, Integer cart_id);

	/**
	 * To replace the old update DAO method.
	 * @return the managed entity (write-only)
	 */
	CartItem update2(CartItem entity);

	boolean deleteByUserId(String u_id);

	Map<String, Object> selectTop100();

	Map<String, Object> selectLikeOperator(String condition, String value);

	Map<String, Object> selectBy(String condition, String value);

	Map<String, Object> selectWithTimeRange(String startTime, String endTime);

	Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue);

	Map<String, Object> insert(Integer p_id, String u_id);

	/**
	 * To replace the old insert DAO method.
	 * @return the managed entity (write-only)
	 */
	CartItem insert2(CartItem data);

	@Deprecated
	Map<String, Object> select(Integer cart_id);

	/**
	 * To replace the old select DAO method.
	 * @return the managed entity
	 */
	CartItem find(Integer cartItemId);

	Boolean selectByPidUid(Integer p_id, String u_id);

	Map<String, Object> selectByUserId(String u_id);

	boolean deleteASingleProduct(String u_id, Integer p_id);

	boolean deleteASingleProduct(Integer cart_id);

	Integer delete(Integer[] cart_ids);
}