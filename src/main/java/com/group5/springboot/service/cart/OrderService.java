package com.group5.springboot.service.cart;

import java.util.List;
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
	
	public List<OrderInfo> test() {
		return orderDao.test();
	}

	public Map<String, Object> insert(OrderInfo order) {
		return orderDao.insert(order);
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
	
	public Map<String, Object> select(OrderInfo orderBean) {
		return orderDao.select(orderBean);
	}

	public Map<String, Object> selectCustom(String hql) {
		return orderDao.selectCustom(hql);
	}
	
	// Admin - 1
//	public Map<String, Object> selectTop20() {
//		return orderDao.selectTop20();
//	}
	
	
	public Map<String, Object> selectTop100() {
		return orderDao.selectTop100();
	}
	
	// Admin - 2
	public boolean update(OrderInfo newBean) {
		return orderDao.update(newBean);
	}
	
	// Admin - 3
	public boolean delete(OrderInfo orderBean) {
		return orderDao.delete(orderBean);
	}
}
