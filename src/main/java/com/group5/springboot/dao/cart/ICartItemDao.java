package com.group5.springboot.dao.cart;

import java.util.Map;

public interface ICartItemDao {

	boolean deleteByUserId(String u_id);

	Map<String, Object> insert(Integer p_id, String u_id);

	Map<String, Object> selectByUserId(String u_id);

}
