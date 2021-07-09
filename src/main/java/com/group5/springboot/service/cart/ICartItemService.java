package com.group5.springboot.service.cart;

import java.util.Map;

public interface ICartItemService {

	boolean deleteByUserId(String u_id);

	Map<String, Object> insert(Integer p_id, String u_id);

	Map<String, Object> selectByUserId(String u_id);

}
