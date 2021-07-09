package com.group5.springboot.dao.cart;
// 購物車的連線物件
// 要考慮做DAO Factory嗎？

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
		cartBean.setProductInfo(pBean);
		cartBean.setUser_Info(uBean);
		
		em.merge(cartBean);
		
		map.put("cartBean", cartBean);
		return map;
	}
	
	@Override
	public boolean deleteByUserId(String u_id) {
		Query query = em.createQuery("DELETE CartItem WHERE u_id = :uid");
		query.setParameter("uid", u_id);
		int deletedNum = query.executeUpdate();
		System.out.println("You deleted " + deletedNum + " row(s) from cart_item table.");
		return (deletedNum == 0)? false : true;
	}
	
	public boolean deleteASingleProduct(String u_id, Integer p_id) {
		Query query = em.createQuery("DELETE CartItem WHERE u_id = :uid AND p_id = :pid");
		query.setParameter("uid", u_id);
		query.setParameter("pid", p_id);
		int deletedNum = query.executeUpdate();
		System.out.println("You deleted a product (p_id = " + p_id + " ) from user (u_id = " + u_id + " )'s cart_item table.");
		return (deletedNum == 0)? false : true;
	}
	

}