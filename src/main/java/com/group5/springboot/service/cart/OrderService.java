package com.group5.springboot.service.cart;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group5.springboot.dao.cart.OrderDao;
import com.group5.springboot.model.cart.OrderInfo;

@Service
@Transactional
public class OrderService implements IOrderService {
	@Autowired private OrderDao orderDao;


	@Override
	public Integer getCurrentIdSeed() {
		return orderDao.getCurrentIdSeed();
	}

	/** @return a boolean that indicates "can add to cart or not" */
	@Override
	public Boolean selectIfBoughtOrNot(Integer p_id, String u_id) {
		return orderDao.selectIfBoughtOrNot(p_id, u_id);
	}

	@Override
	public Map<String, Object> insert(OrderInfo order) {
		return orderDao.insert(order);
	}

	@Override
	public Map<String, Object> selectLikeOperator(String condition, String value) {
		return orderDao.selectLikeOperator(condition, value);
	}

	@Override
	public Map<String, Object> selectBy(String condition, String value) {
		return orderDao.selectBy(condition, value);
	}

	@Override
	public Map<String, Object> selectWithTimeRange(String startTime, String endTime) {
		return orderDao.selectWithTimeRange(startTime, endTime);
	}

	@Override
	public Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue) {
		return orderDao.selectWithNumberRange(condition, minValue, maxValue);
	}

	@Override
	public Map<String, Object> select(OrderInfo orderBean) {
		return orderDao.select(orderBean);
	}

	@Override
	public Map<String, Object> selectTop100() {
		return orderDao.selectTop100();
	}
}