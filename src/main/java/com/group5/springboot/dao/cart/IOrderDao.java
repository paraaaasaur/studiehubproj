package com.group5.springboot.dao.cart;

import java.util.*;

import com.group5.springboot.model.cart.OrderInfo;

public interface IOrderDao {
	// 建立連線、提供SQL方法
	
	Map<String, Object> insert(OrderInfo orderBean);
//	OrderInfo select(String P_ID);
	Map<String, Object> selectCustom(String hql);
	Map<String, Object> selectAll();
	Map<String, Object> select(OrderInfo orderBean);
	
	/**
	
	// 回傳資料筆數；0表示沒變化、-1表示出問題、1以上表示更改比數
	boolean updateOrder(OrderBean orderBean, String str3, Object obj4); 
	
	// order只會修正資料，紀錄會一直留下
	boolean deleteOrder(String O_ID); 
	*/
}
