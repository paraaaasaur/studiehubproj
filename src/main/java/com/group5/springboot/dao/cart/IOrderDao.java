package com.group5.springboot.dao.cart;

import java.util.*;

import com.group5.springboot.model.cart.OrderInfo;

public interface IOrderDao {
	// 建立連線、提供SQL方法
	
	OrderInfo insert(OrderInfo orderBean);
//	OrderInfo select(String P_ID);
	List<OrderInfo> selectCustom(String hql);
	List<OrderInfo> selectAll();
	OrderInfo select(OrderInfo orderBean);
	
	/**
	
	// 回傳資料筆數；0表示沒變化、-1表示出問題、1以上表示更改比數
	boolean updateOrder(OrderBean orderBean, String str3, Object obj4); 
	
	// order只會修正資料，紀錄會一直留下
	boolean deleteOrder(String O_ID); 
	*/
}
