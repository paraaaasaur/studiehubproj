package com.group5.springboot.service.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group5.springboot.dao.cart.CartItemDao;
import com.group5.springboot.dao.product.ProductDaoImpl;
import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.product.ProductInfo;

@Service
@Transactional
public class CartItemService implements ICartItemService {
	@Autowired // SDI✔
	private CartItemDao cartItemDao;
	@Autowired // SDI✔
	private ProductDaoImpl productDao;
	
	public Map<String, Object> select(Integer cart_id) {
		return cartItemDao.select(cart_id);
	}
	
	@Override
	public Map<String, Object> selectByUserId(String u_id) {
		return cartItemDao.selectByUserId(u_id);
	}
	
	public Map<String, Object> selectTop100() {
		return cartItemDao.selectTop100();
	}
	
	/**********************************************************************************************************/
	
	
	public Map<String, Object> selectLikeOperator(String condition, String value) {
		return cartItemDao.selectLikeOperator(condition, value);
	}
	
	/**
	 * 用來查某商品是不是已經存在於購物車裡了。<br>
	 * true > 尚未存在 = 可以加入購物車 <br>
	 * false > 已存在 = 不允許加入購物車 <br>
	 **/
	public boolean selectByProductId(Integer p_id, String u_id) {
		return cartItemDao.selectByPidUid(p_id, u_id);
	}
	
	public Map<String, Object> selectBy(String condition, String value) {
		return cartItemDao.selectBy(condition, value);
	}
	
	public Map<String, Object> selectWithTimeRange(String startTime, String endTime) {
		return cartItemDao.selectWithTimeRange(startTime, endTime);
	}
	
	public Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue) {
		return cartItemDao.selectWithNumberRange(condition, minValue, maxValue);
	}	
	
	/**********************************************************************************************************/
	
	@Override
	public Map<String, Object> insert(Integer p_id, String u_id) {
		return cartItemDao.insert(p_id, u_id);
	}
	
	public Integer update(String newU_id, Integer newP_id, Integer cart_id) {
		return cartItemDao.update(newU_id, newP_id, cart_id);
	}

	@Override
	public boolean deleteByUserId(String u_id) {
		return cartItemDao.deleteByUserId(u_id);
	}
	
	@Override @Deprecated
	public boolean deleteASingleProduct(String u_id, Integer p_id) {
		return cartItemDao.deleteASingleProduct(u_id, p_id);
	}
	
	public boolean deleteASingleProduct(Integer cart_id) {
		return cartItemDao.deleteASingleProduct(cart_id);
	}
	
	public Integer delete(Integer[] cart_ids) {
		return cartItemDao.delete(cart_ids);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCart(String u_id) {
		List<CartItem> cartItems = (List<CartItem>) cartItemDao.selectByUserId(u_id).get("cartItems");
		// 測試用
		if(cartItems.size() == 0 || cartItems == null) {
			refillCart(u_id);
		}
		//
		List<Map<String, Object>> cart = new ArrayList<>();
		
		for(CartItem cartItem : cartItems) {
			ProductInfo pBean = productDao.findByProductID(cartItem.getP_id());
			Map<String, Object> map = new HashMap<String, Object>();
			
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

	
	// 測試用，插入p_id = 1 和 2的商品進購物車
	private void refillCart(String u_id) {
		for(int i = 0; i <= 5; i++) {
			try {
				cartItemDao.insert(i, u_id);
			} catch (Exception e) {
				continue;
			}
		}
		return;
	}

}
