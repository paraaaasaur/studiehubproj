package com.group5.springboot.service.cart;

import com.group5.springboot.dto.cart.UpdateCartItemRequest;
import com.group5.springboot.model.cart.CartItem;

import java.util.List;
import java.util.Map;

public interface CartItemService {
	@Deprecated
	Map<String, Object> select(Integer cart_id);

	/**
	 * To replace the old select service.
	 * @return the managed entity
	 */
	CartItem find(Integer cartItemId);

	Map<String, Object> selectTop100();

	@Deprecated
	Integer update(String newU_id, Integer newP_id, Integer cart_id);

	/**
	 * To replace the old update service.
	 * @return the managed entity (write-only)
	 */
	CartItem update2(UpdateCartItemRequest data);

	boolean deleteByUserId(String u_id);

	Map<String, Object> selectLikeOperator(String condition, String value);

	boolean selectByProductId(Integer p_id, String u_id);

	Map<String, Object> selectBy(String condition, String value);

	Map<String, Object> selectWithTimeRange(String startTime, String endTime);

	Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue);

	@Deprecated
	Map<String, Object> insert(Integer p_id, String u_id);

	/**
	 * To replace the old insert service.
	 * @return the managed entity (write-only)
	 */
	CartItem insert2(Integer p_id, String u_id);

	boolean deleteASingleProduct(String u_id, Integer p_id);

	boolean deleteASingleProduct(Integer cart_id);

	Integer delete(Integer[] cart_ids);

	List<Map<String, Object>> getCart(String u_id);
}