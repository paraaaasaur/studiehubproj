package com.group5.springboot.dao.cart;
// 購物車的連線物件
// 要考慮做DAO Factory嗎？

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
public class CartItemDao implements ICartItemDao{
	@Autowired 
	private EntityManager em;
	
	public CartItem test05() {
		return em.createQuery("FROM CartItem WHERE cart_id=1000", CartItem.class).getSingleResult();
	}
	
	public Map<String, Object> select(Integer cart_id) {
		System.out.println(cart_id);
		Map<String, Object> map = new HashMap<String, Object>();
		CartItem cartItem = em.find(CartItem.class, cart_id);
		System.out.println(cartItem);
		map.put("cartItem", cartItem);
		return map;
	}
	/**
	 * 用來查某商品是不是已經存在於購物車裡了。<br>
	 * true > 尚未存在 = 可以加入購物車 <br>
	 * false > 已存在 = 不允許加入購物車 <br>
	 **/
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
		Map<String, Object> map = new HashMap<String, Object>();
		if(u_id == null) {
			map.put("errorMessage", "No u_id passed into this method (CartItemDao.selectByUserId()).");
			return map;
		}
		TypedQuery<CartItem> query = em.createQuery("FROM CartItem WHERE u_id = :uid", CartItem.class);
		query.setParameter("uid", u_id);
		map.put("cartItems", query.getResultList());
		return map;
	}
	
	public Map<String, Object> selectTop100() {
		Query sqlQuery = em.createNativeQuery("SELECT TOP(100) * FROM cart_item ORDER BY cart_id DESC, u_id DESC;", CartItem.class);
		@SuppressWarnings("unchecked")
		List<CartItem> list = (List<CartItem>) sqlQuery.getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		return map;
	}
	/**********************************************************************************************************/
	
	
	public Map<String, Object> selectLikeOperator(String condition, String value) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isString = !( "cart_id".equals(condition) || "p_id".equals(condition) || "p_price".equals(condition));
		condition = (isString)? "cart." + condition : "STR(cart." + condition + ")";
		System.out.println("condition = " + condition + "; value = " + value);
		TypedQuery<CartItem> query = em.createQuery("FROM CartItem cart WHERE " + condition + " LIKE :value", CartItem.class);
		query.setParameter("value", "%" + value + "%");
		List<CartItem> resultList = query.getResultList();
		System.out.println(resultList);
		map.put("list", resultList);
		return map;
	}
	
	public Map<String, Object> selectBy(String condition, String value) {
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean isInteger = ( "cart_id".equals(condition) || "p_id".equals(condition) || "p_price".equals(condition));
		Object parsedValue = (isInteger)? Integer.parseInt(value) : value;
		System.out.println("condition = " + condition + "; value = " + parsedValue);
		TypedQuery<CartItem> query = em.createQuery("FROM CartItem cart WHERE " + condition + " = :value", CartItem.class);
		query.setParameter("value", parsedValue);
		
		List<CartItem> resultList = query.getResultList();
		System.out.println(resultList);
		map.put("list", resultList);
		return map;
	}
	
	public Map<String, Object> selectWithTimeRange(String startTime, String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT * FROM cart_item WHERE cart_date >= :startTime AND cart_date <= :endTime ORDER BY cart_date DESC, u_id DESC";
		Query query = em.createNativeQuery(sql, CartItem.class);
		// ❗❓ 總覺得下面的轉法只要換個國家就會出錯...
		try {
			Date parsedStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			Date parsedEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
			System.out.println("From : " + parsedStartTime);
			System.out.println("To : " + parsedEndTime);
			query.setParameter("startTime", parsedStartTime, TemporalType.TIME);
			query.setParameter("endTime", parsedEndTime, TemporalType.TIME);
		} catch (ParseException e) {
			System.out.println("轉時間出錯了");
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		List<CartItem> list = (List<CartItem>) (query.getResultList());
		map.put("list", list);
		return map;
	}
	
	public Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT * FROM cart_item WHERE " + condition + " >= :minValue AND " + condition + "<= :maxValue ORDER BY " + condition + " DESC";
		Query query = em.createNativeQuery(sql, CartItem.class);
		query.setParameter("minValue", minValue);
		query.setParameter("maxValue", maxValue);
		@SuppressWarnings("unchecked")
		List<CartItem> list = query.getResultList();
		map.put("list", list);
		return map;
	}	
	
	/**********************************************************************************************************/
	@Override
	public Map<String, Object> insert(Integer p_id, String u_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		// FK 驗證
		ProductInfo pBean = em.find(ProductInfo.class, p_id);		
		if(pBean == null) {
			String errorMessage = "********** 新增失敗：以 p_id (" + p_id + ") 在資料庫中找不到對應的 Product 資料。 **********";
			System.out.println(errorMessage);
			map.put("errorMessage", errorMessage);
			return map;
		} 
		User_Info uBean = em.find(User_Info.class, u_id);
		if(uBean == null) {
			String errorMessage = "********** 新增失敗：以 u_id (" + u_id + ") 在資料庫中找不到對應的 User 資料。 **********";
			System.out.println(errorMessage);
			map.put("errorMessage", errorMessage);
			return map;
		}
		
		// 綁定關聯
		CartItem cartBean = new CartItem();
		// 補完非FK值
		cartBean.setU_firstname(uBean.getU_firstname());
		cartBean.setU_lastname(uBean.getU_lastname());
		cartBean.setP_name(pBean.getP_Name());
		cartBean.setP_price(pBean.getP_Price());
		// 綁定關聯物件
		cartBean.setProductInfo(pBean);
		cartBean.setUser_Info(uBean);
		
		em.merge(cartBean);
		
		map.put("cartBean", cartBean);
		return map;
	}
	
	public Integer update(String newU_id, Integer newP_id, Integer cart_id) {
		System.out.println(cart_id);
		CartItem cartBean = em.find(CartItem.class, cart_id);
		
		if (cartBean != null) {
			// FK驗證
			User_Info uBean = em.find(User_Info.class, newU_id);
			if(uBean == null) {
				System.out.println("********** 錯誤：以 o_id (" + cartBean.getU_id() + ") 在資料庫中找不到對應的 User 資料。 **********");
				return -1;	
			}
			ProductInfo pBean = em.find(ProductInfo.class, newP_id);
			if(pBean == null) {
				System.out.println("********** 錯誤：以 p_id (" + cartBean.getP_id() + ") 在資料庫中找不到對應的 Product 資料。 **********");
				return -1;
			} 
			// 補完非FK值
			cartBean.setU_firstname(uBean.getU_firstname());
			cartBean.setU_lastname(uBean.getU_lastname());
			cartBean.setP_name(pBean.getP_Name());
			cartBean.setP_price(pBean.getP_Price());
			// 綁定關聯物件
			cartBean.setUser_Info(uBean);
			cartBean.setProductInfo(pBean);
			System.out.println("1111111111111111111111111");
			em.merge(cartBean);
			System.out.println("222222222222222222222");
			
			return 1;
		} else {
			System.out.println("*** CartItem with CART_ID = " + cart_id + "doesn't exist in the database :^) ***");
			return -1;			
		}
		
	}
	
	
	@Override
	public boolean deleteByUserId(String u_id) {
		Query query = em.createQuery("DELETE CartItem WHERE u_id = :uid");
		query.setParameter("uid", u_id);
		int deletedNum = query.executeUpdate();
		System.out.println("You deleted " + deletedNum + " row(s) from cart_item table.");
		return (deletedNum == 0)? false : true;
	}
	
	@Override
	public boolean deleteASingleProduct(String u_id, Integer p_id) {
		
		Query query = em.createQuery("DELETE CartItem WHERE u_id = :uid AND p_id = :pid");
		query.setParameter("uid", u_id);
		query.setParameter("pid", p_id);
		int deletedNum = query.executeUpdate();
		System.out.println("You deleted a product (p_id = " + p_id + " ) from user (u_id = " + u_id + " )'s cart_item table.");
		
		return (deletedNum == 0)? false : true;
	}
	
	public Integer delete(Integer[] cart_ids) {
		Query deleteQuery = em.createQuery("DELETE CartItem WHERE cart_id IN (:cartids)");
		deleteQuery.setParameter("cartids", Arrays.asList(cart_ids));
		Integer result = deleteQuery.executeUpdate();
		return result;
	}
	

}