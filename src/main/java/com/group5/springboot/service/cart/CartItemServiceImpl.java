package com.group5.springboot.service.cart;

import com.group5.springboot.dao.cart.CartItemDao;
import com.group5.springboot.dao.product.ProductDao;
import com.group5.springboot.dao.user.UserDao;
import com.group5.springboot.dto.cart.UpdateCartItemRequest;
import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.product.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {
	private final CartItemDao cartItemDao;
	private final UserDao userDao;
	private final ProductDao productDao;


	@Autowired
	public CartItemServiceImpl(CartItemDao cartItemDao, UserDao userDao, ProductDao productDao) {
		this.cartItemDao = cartItemDao;
		this.userDao = userDao;
		this.productDao = productDao;
	}


	@Deprecated
	@Override
	public Map<String, Object> select(Integer cart_id) {
		return cartItemDao.select(cart_id);
	}

	@Override
	public CartItem find(Integer cartItemId) {
		return cartItemDao.find(cartItemId);
	}

	@Override
	public Map<String, Object> selectTop100() {
		return cartItemDao.selectTop100();
	}

	@Override
	public Map<String, Object> selectLikeOperator(String condition, String value) {
		return cartItemDao.selectLikeOperator(condition, value);
	}

	@Override
	public boolean selectByProductId(Integer p_id, String u_id) {
		return cartItemDao.selectByPidUid(p_id, u_id);
	}

	@Override
	public Map<String, Object> selectBy(String condition, String value) {
		return cartItemDao.selectBy(condition, value);
	}

	@Override
	public Map<String, Object> selectWithTimeRange(String startTime, String endTime) {
		return cartItemDao.selectWithTimeRange(startTime, endTime);
	}

	@Override
	public Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue) {
		return cartItemDao.selectWithNumberRange(condition, minValue, maxValue);
	}

	@Deprecated
	@Override
	public Map<String, Object> insert(Integer p_id, String u_id) {
		return cartItemDao.insert(p_id, u_id);
	}

	@Override
	public CartItem insert2(Integer p_id, String u_id) {
		var newEntity = applyToEntity(p_id, u_id);
		return cartItemDao.insert2(newEntity);
	}

	@Deprecated
	@Override
	public Integer update(String newU_id, Integer newP_id, Integer cart_id) {
		return cartItemDao.update(newU_id, newP_id, cart_id);
	}

	@Override
	public CartItem update2(UpdateCartItemRequest data) {
		var entity = applyToEntity(data);
		
		return cartItemDao.update2(entity);
	}

	@Override
	public boolean deleteByUserId(String u_id) {
		return cartItemDao.deleteByUserId(u_id);
	}
	
	@Override
	public boolean deleteASingleProduct(String u_id, Integer p_id) {
		return cartItemDao.deleteASingleProduct(u_id, p_id);
	}

	@Override
	public boolean deleteASingleProduct(Integer cart_id) {
		return cartItemDao.deleteASingleProduct(cart_id);
	}

	@Override
	public Integer delete(Integer[] cart_ids) {
		return cartItemDao.delete(cart_ids);
	}

	@Override
	public List<Map<String, Object>> getCart(String u_id) {
		List<CartItem> cartItems = (List<CartItem>) cartItemDao.selectByUserId(u_id).get("cartItems");

		List<Map<String, Object>> cart = new ArrayList<>();
		
		for (CartItem cartItem : cartItems) {
			ProductInfo pBean = productDao.findByProductID(cartItem.getP_id());
			Map<String, Object> map = new HashMap<>();
			
			map.put("cart_id", cartItem.getCart_id());
			map.put("p_name", pBean.getP_Name());
			map.put("p_id", pBean.getP_ID());
			map.put("p_price", pBean.getP_Price());
			map.put("p_desc", pBean.getP_DESC());
			map.put("p_teacher", pBean.getU_ID());
			
			cart.add(map);
		}
		return cart;
	}


	// convenience methods
	private CartItem applyToEntity(Integer p_id, String u_id) {
		var dbProduct = productDao.findByProductID(p_id);
		var dbUser = userDao.getSingleUser(u_id);

		CartItem newEntity = new CartItem();
		newEntity.setProductInfo(dbProduct);
		newEntity.setUser_Info(dbUser);

		return newEntity;
	}
	
	private CartItem applyToEntity(UpdateCartItemRequest data) {
		var dbCartItem = cartItemDao.find(data.getCart_id());
		var dbUser = userDao.getSingleUser(data.getU_id());
		var dbProduct = productDao.findByProductID(data.getP_id());
		
		// update associations
		dbCartItem.setProductInfo(dbProduct);
		dbCartItem.setUser_Info(dbUser);
		
		return dbCartItem;
	}
}