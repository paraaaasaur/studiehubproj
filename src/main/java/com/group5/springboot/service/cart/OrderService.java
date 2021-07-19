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
	
	/** 成功試出三張資料表聯集啦啦啦啦啦(不過是用原生SQL) */
	public List<OrderInfo> selectOrderInfoByOPUJoin() {
		return orderDao.selectOrderInfoByOPUJoin();
	}
	
	public Boolean selectCheckOrderExistence(Integer oid) {
		return orderDao.selectCheckOrderExistence(oid);
	}
	
	public Integer getCurrentIdSeed() {
		return orderDao.getCurrentIdSeed();
	}
	/**
	 * 用來查某課程商品是不是已經存在於資料庫內並交易完成了。<br>
	 * true > 尚未存在 = 可以加入購物車 <br>
	 * false > 已存在 = 不允許加入購物車 <br>
	 **/
	public Boolean selectIfBoughtOrNot(Integer p_id, String u_id) {
		return orderDao.selectIfBoughtOrNot(p_id, u_id);
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
	
	public Integer delete(Integer[] identitySeeds) {
		return orderDao.delete(identitySeeds);
	}
}
