package com.group5.springboot.service.cart;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group5.springboot.dao.cart.OrderDao;
import com.group5.springboot.model.cart.OrderInfo;

@Service
@Transactional
public class OrderService implements IOrderService{
	@Autowired // SDI✔
	private OrderDao orderDao;
	
	public Integer getCurrentIdSeed() {
		return orderDao.getCurrentIdSeed();
	}

	public Map<String, Object> insert(OrderInfo order) {
		return orderDao.insert(order);
	}
	
	public OrderInfo selectLatestOid() {
		return orderDao.selectLatestOid();
	}
	
	public Map<String, Object> selectAll() {
		return orderDao.selectAll();
	}
	
	public Map<String, Object> selectLikeOperator(String condition, String value) {
		return orderDao.selectLikeOperator(condition, value);
	}
	
	public Map<String, Object> selectBy(String condition, String value) {
		return orderDao.selectBy(condition, value);
	}
	
	public Map<String, Object> selectWithTimeRange(String startTime, String endTime) {
		return orderDao.selectWithTimeRange(startTime, endTime);
	}
	
	public Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue) {
		return orderDao.selectWithNumberRange(condition, minValue, maxValue);
	}
	
	public Map<String, Object> select(OrderInfo orderBean) {
		return orderDao.select(orderBean);
	}

	public Map<String, Object> selectCustom(String hql) {
		return orderDao.selectCustom(hql);
	}
	
	public Map<String, Object> selectTop100() {
		return orderDao.selectTop100();
	}
	
	public boolean update(OrderInfo newBean) {
		return orderDao.update(newBean);
	}
	
	public Integer delete(Integer[] o_ids) {
		return orderDao.delete(o_ids);
	}
}
