package com.group5.springboot.dao.cart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;

@Repository
public class CartItemDao implements ICartItemDao {
	@Autowired private EntityManager em;


	@Override
	public Map<String, Object> select(Integer cart_id) {
		Map<String, Object> map = new HashMap<>();
		CartItem cartItem = em.find(CartItem.class, cart_id);
		map.put("cartItem", cartItem);

		return map;
	}

	@Override
	public Boolean selectByPidUid(Integer p_id, String u_id) {
		if(p_id == null || u_id == null) {
			return false;
		}
		return (em.createQuery("FROM CartItem WHERE p_id = :pid AND u_id = :uid", CartItem.class)
				.setParameter("pid", p_id)
				.setParameter("uid", u_id)
				.getResultList().size() != 0)? false : true;
	}
	
	@Override
	public Map<String, Object> selectByUserId(String u_id) {
		Map<String, Object> map = new HashMap<>();
		if(u_id == null) {
			map.put("errorMessage", "No u_id passed into this method (CartItemDao.selectByUserId()).");
			return map;
		}

		TypedQuery<CartItem> query = em.createQuery("FROM CartItem WHERE u_id = :uid", CartItem.class);
		query.setParameter("uid", u_id);
		map.put("cartItems", query.getResultList());

		return map;
	}

	@Override
	public Map<String, Object> selectTop100() {
		Query sqlQuery = em.createNativeQuery("SELECT TOP(100) * FROM cart_item ORDER BY cart_id DESC, u_id DESC;", CartItem.class);
		List<CartItem> list = (List<CartItem>) sqlQuery.getResultList();
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);

		return map;
	}

	@Override
	public Map<String, Object> selectLikeOperator(String condition, String value) {
		Map<String, Object> map = new HashMap<>();

		boolean isString = !( "cart_id".equals(condition) || "p_id".equals(condition) || "p_price".equals(condition));
		condition = (isString)? "cart." + condition : "STR(cart." + condition + ")";

		TypedQuery<CartItem> query = em.createQuery("FROM CartItem cart WHERE " + condition + " LIKE :value", CartItem.class);
		query.setParameter("value", "%" + value + "%");
		List<CartItem> resultList = query.getResultList();

		map.put("list", resultList);

		return map;
	}

	@Override
	public Map<String, Object> selectBy(String condition, String value) {
		Map<String, Object> map = new HashMap<>();

		Boolean isInteger = ( "cart_id".equals(condition) || "p_id".equals(condition) || "p_price".equals(condition));
		Object parsedValue = (isInteger)? Integer.parseInt(value) : value;
		TypedQuery<CartItem> query = em.createQuery("FROM CartItem cart WHERE " + condition + " = :value", CartItem.class);
		query.setParameter("value", parsedValue);
		
		List<CartItem> resultList = query.getResultList();

		map.put("list", resultList);

		return map;
	}

	@Override
	public Map<String, Object> selectWithTimeRange(String startTime, String endTime) {
		Map<String, Object> map = new HashMap<>();

		String sql = "SELECT * FROM cart_item WHERE cart_date >= :startTime AND cart_date <= :endTime ORDER BY cart_date DESC, u_id DESC";
		Query query = em.createNativeQuery(sql, CartItem.class);

		try {
			Date parsedStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			Date parsedEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
			query.setParameter("startTime", parsedStartTime, TemporalType.TIME);
			query.setParameter("endTime", parsedEndTime, TemporalType.TIME);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<CartItem> list = (List<CartItem>) (query.getResultList());

		map.put("list", list);

		return map;
	}

	@Override
	public Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue) {
		Map<String, Object> map = new HashMap<>();

		String sql = "SELECT * FROM cart_item WHERE " + condition + " >= :minValue AND " + condition + "<= :maxValue ORDER BY " + condition + " DESC";
		Query query = em.createNativeQuery(sql, CartItem.class);
		query.setParameter("minValue", minValue);
		query.setParameter("maxValue", maxValue);
		List<CartItem> list = query.getResultList();

		map.put("list", list);

		return map;
	}
	
	@Override
	public Map<String, Object> insert(Integer p_id, String u_id) {
		Map<String, Object> map = new HashMap<>();
		
		ProductInfo pBean = em.find(ProductInfo.class, p_id);
		if(pBean == null) {
			String errorMessage = "********** 新增失敗：以 p_id (" + p_id + ") 在資料庫中找不到對應的 Product 資料。 **********";
			map.put("errorMessage", errorMessage);
			return map;
		}

		User_Info uBean = em.find(User_Info.class, u_id);
		if(uBean == null) {
			String errorMessage = "********** 新增失敗：以 u_id (" + u_id + ") 在資料庫中找不到對應的 User 資料。 **********";
			map.put("errorMessage", errorMessage);
			return map;
		}
		
		CartItem cartBean = new CartItem();
		cartBean.setU_firstname(uBean.getU_firstname());
		cartBean.setU_lastname(uBean.getU_lastname());
		cartBean.setP_name(pBean.getP_Name());
		cartBean.setP_price(pBean.getP_Price());
		cartBean.setProductInfo(pBean);
		cartBean.setUser_Info(uBean);
		em.merge(cartBean);

		map.put("cartBean", cartBean);

		return map;
	}

	@Override
	public Integer update(String newU_id, Integer newP_id, Integer cart_id) {
		CartItem cartBean = em.find(CartItem.class, cart_id);
		
		if (cartBean != null) {
			User_Info uBean = em.find(User_Info.class, newU_id);
			if(uBean == null) {
				return -1;
			}

			ProductInfo pBean = em.find(ProductInfo.class, newP_id);
			if(pBean == null) {
				return -1;
			}

			cartBean.setU_firstname(uBean.getU_firstname());
			cartBean.setU_lastname(uBean.getU_lastname());
			cartBean.setP_name(pBean.getP_Name());
			cartBean.setP_price(pBean.getP_Price());
			cartBean.setUser_Info(uBean);
			cartBean.setProductInfo(pBean);
			em.merge(cartBean);
			
			return 1;
		} else {
			return -1;
		}
	}
	
	@Override
	public boolean deleteByUserId(String u_id) {
		Query query = em.createQuery("DELETE CartItem WHERE u_id = :uid");
		query.setParameter("uid", u_id);
		int deletedNum = query.executeUpdate();
		return deletedNum != 0;
	}
	
	@Override
	public boolean deleteASingleProduct(String u_id, Integer p_id) {
		Query query = em.createQuery("DELETE CartItem WHERE u_id = :uid AND p_id = :pid");
		query.setParameter("uid", u_id);
		query.setParameter("pid", p_id);
		int deletedNum = query.executeUpdate();

		return deletedNum != 0;
	}

	@Override
	public boolean deleteASingleProduct(Integer cart_id) {
		Query query = em.createQuery("DELETE CartItem WHERE cart_id = :cartid");
		query.setParameter("cartid", cart_id);
		int deletedNum = query.executeUpdate();

		return deletedNum != 0;
	}

	@Override
	public Integer delete(Integer[] cart_ids) {
		Query deleteQuery = em.createQuery("DELETE CartItem WHERE cart_id IN (:cartids)");
		deleteQuery.setParameter("cartids", Arrays.asList(cart_ids));

		return deleteQuery.executeUpdate();
	}
}