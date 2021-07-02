package com.group5.springboot.service.cart;

import java.util.List;

import javax.persistence.TypedQuery;

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
	
	public OrderInfo insert(OrderInfo order) {
		return orderDao.insert(order);
	}
	
	public List<OrderInfo> selectAll() {
		return orderDao.selectAll();
	}
	
	public List<OrderInfo> selectLikeOperator(Object condition, Object value) {
		return orderDao.selectLikeOperator(condition, value);
	}
	
	public OrderInfo select(OrderInfo orderBean) {
		return orderDao.select(orderBean);
	}

	public List<OrderInfo> selectCustom(String hql) {
		return orderDao.selectCustom(hql);
	}
	
	// Admin - 1
	public List<OrderInfo> selectTop20() {
		return orderDao.selectTop20();
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
