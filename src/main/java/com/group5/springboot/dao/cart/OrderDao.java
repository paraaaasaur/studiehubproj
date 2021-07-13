package com.group5.springboot.dao.cart;
// 購物車的連線物件
// 要考慮做DAO Factory嗎？

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;

@Repository
public class OrderDao implements IOrderDao {
	@Autowired 
	private EntityManager em;
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	
	
	@SuppressWarnings({ "unchecked" })
	public List<OrderInfo> test() {
		Query query = em.createNativeQuery("SELECT * FROM order_info WHERE o_date < '2021-07-08' AND o_date > :value ", OrderInfo.class); // ❓❗
		query.setParameter("value", "2021-07-05 18:00:00");
		List<OrderInfo> list = query.getResultList();
		return list; 
	}
	
	@Override
	public Map<String, Object> selectAll() {
		Map<String, Object> map = new HashMap<String, Object>();
		TypedQuery<OrderInfo> query = em.createQuery("FROM OrderInfo", OrderInfo.class);
		map.put("list", query.getResultList());
		return map;
	}
	
	public Map<String, Object> selectLikeOperator(String condition, String value) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isString = !( "o_id".equals(condition) || "p_id".equals(condition) || "p_price".equals(condition));
		condition = (isString)? "o." + condition : "STR(o." + condition + ")";
		System.out.println("condition = " + condition + "; value = " + value);
		TypedQuery<OrderInfo> query = em.createQuery("FROM OrderInfo o WHERE " + condition + " LIKE :value", OrderInfo.class);
		query.setParameter("value", "%" + value + "%");
		List<OrderInfo> resultList = query.getResultList();
		System.out.println(resultList);
		map.put("list", resultList);
		return map;
	}
	
	public Map<String, Object> selectBy(String condition, String value) {
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean isInteger = ( "o_id".equals(condition) || "p_id".equals(condition) || "p_price".equals(condition));
		Object parsedValue = (isInteger)? Integer.parseInt(value) : value;
		System.out.println("condition = " + condition + "; value = " + parsedValue);
		TypedQuery<OrderInfo> query = em.createQuery("FROM OrderInfo o WHERE " + condition + " = :value", OrderInfo.class);
		query.setParameter("value", parsedValue);
		
		List<OrderInfo> resultList = query.getResultList();
		System.out.println(resultList);
		map.put("list", resultList);
		return map;
	}
	
	public Map<String, Object> selectWithTimeRange(String startTime, String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT * FROM order_info WHERE o_date >= :startTime AND o_date <= :endTime ORDER BY o_date DESC, u_id DESC";
		Query query = em.createNativeQuery(sql, OrderInfo.class);
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
		List<OrderInfo> list = (List<OrderInfo>) (query.getResultList());
		map.put("list", list);
		return map;
	}
	
	public Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT * FROM order_info WHERE " + condition + " >= :minValue AND " + condition + "<= :maxValue ORDER BY " + condition + " DESC";
		Query query = em.createNativeQuery(sql, OrderInfo.class);
		query.setParameter("minValue", minValue);
		query.setParameter("maxValue", maxValue);
		@SuppressWarnings("unchecked")
		List<OrderInfo> list = query.getResultList();
		map.put("list", list);
		return map;
	}
	
	@Override
	public Map<String, Object> select(OrderInfo orderBean) {
		// ‼ HQL不是用table名 ‼
		Map<String, Object> map = new HashMap<String, Object>();
		TypedQuery<OrderInfo> query = em.createQuery("FROM OrderInfo WHERE o_id = :oid", OrderInfo.class);
		query.setParameter("oid", orderBean.getO_id());
		map.put("orderInfo", query.getSingleResult());
		return map;
	}

	@Override
	public Map<String, Object> selectCustom(String hql) {
		Map<String, Object> map = new HashMap<String, Object>();
		TypedQuery<OrderInfo> query = em.createQuery(hql, OrderInfo.class);
		List<OrderInfo> resultList = query.getResultList();
		map.put("list", resultList);
		return map;
	}

	public Map<String, Object> selectTop100() {
		Map<String, Object> map = new HashMap<String, Object>();
//		TypedQuery<OrderInfo> query = em.createQuery("FROM OrderInfo ob ORDER BY ob.o_id ASC", OrderInfo.class).setMaxResults(100);
//		List<OrderInfo> resultList = query.getResultList();
		@SuppressWarnings("unchecked")
		List<OrderInfo> resultList = (List<OrderInfo>) (em.createNativeQuery("SELECT * FROM order_info ORDER BY o_id DESC", OrderInfo.class).setMaxResults(100).getResultList());
		map.put("list", resultList);
		return map;
	}
	
	@Override
	public Map<String, Object> insert(OrderInfo oBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		ProductInfo pBean = em.find(ProductInfo.class, oBean.getP_id());
		User_Info uBean = em.find(User_Info.class, oBean.getU_id());
		
		if(pBean == null) {
			System.out.println("********** 新增失敗：以 p_id (" + oBean.getP_id() + ") 在資料庫中找不到對應的 Product 資料。 **********");
			return null;
		} else if(uBean == null) {
			System.out.println("********** 新增失敗：以 u_id (" + oBean.getU_id() + ") 在資料庫中找不到對應的 User 資料。 **********");
			return null;	
		}
		// 把值補完整
		oBean.setU_firstname(uBean.getU_firstname()); 
		oBean.setU_lastname(uBean.getU_lastname()); 
		oBean.setU_email(uBean.getU_email());
		
		oBean.setP_name(pBean.getP_Name());
		oBean.setP_price(pBean.getP_Price());
		
		// 準備綁定關聯
		Set<OrderInfo> orderSet = new HashSet<OrderInfo>();
		orderSet.add(oBean);
		Set<ProductInfo> productInfoSet = new HashSet<ProductInfo>();
		productInfoSet.add(pBean);
		
		// 互相綁定關聯 (共計 3! = 6 個關聯)
//			pBean.setOrderInfoSet(orderSet); // P-Os 關聯
//			uBean.setOrderInfoSet(orderSet); // U-Os 關聯
//			uBean.setProductInfoSet(productInfoSet); // U-Ps 關聯
		oBean.setProductInfo(pBean); // O-P 關聯
		oBean.setUser_Info(uBean); // O-U 關聯
//			pBean.setUser_Info(uBean); // P-U 關聯
		
		System.out.println("**********************************************************");
		System.out.println(oBean.toString());
		System.out.println("**********************************************************");
		em.merge(oBean);
//		em.persist(oBean); // ❓ Admin可以 Index不行，為何RRR
		
		map.put("orderBean", oBean);
		return map;
	}

	// Admin - 2
	public boolean update(OrderInfo newOBean) {
		boolean updateStatus = false;
		// 以PK查出資料庫的 O- / P- / U-Bean
		OrderInfo oBean = em.find(OrderInfo.class, newOBean.getO_id()); 
		ProductInfo pBean = em.find(ProductInfo.class, newOBean.getP_id());
		User_Info uBean = em.find(User_Info.class, newOBean.getU_id());
		
		
		if (oBean != null) {
			
			if(pBean == null) {
				System.out.println("********** 錯誤：以 p_id (" + newOBean.getP_id() + ") 在資料庫中找不到對應的 Product 資料。 **********");
				return updateStatus;
			} else if(uBean == null) {
				System.out.println("********** 錯誤：以 o_id (" + newOBean.getU_id() + ") 在資料庫中找不到對應的 User 資料。 **********");
				return updateStatus;	
			}
//			oBean.setO_id         (newBean.getO_id()       ); // 無意義  
			oBean.setP_id         (newOBean.getP_id()       );  
			oBean.setP_name       (newOBean.getP_name()     );  
			oBean.setP_price      (newOBean.getP_price()    );  
			oBean.setU_id         (newOBean.getU_id()       );  
			oBean.setU_firstname  (newOBean.getU_firstname());  
			oBean.setU_lastname   (newOBean.getU_lastname() );  
			oBean.setU_email      (newOBean.getU_email()    );  
			oBean.setO_status     (newOBean.getO_status()   );  
			oBean.setO_date       (newOBean.getO_date()     );  
			oBean.setO_amt        (newOBean.getO_amt()      );  
			// 準備綁定關聯
			Set<OrderInfo> orderSet = new HashSet<OrderInfo>();
			orderSet.add(oBean);
			// 互相綁定關聯 (共計 3! = 6 個關聯)
//			pBean.setOrderInfoSet(orderSet); // P-Os 關聯
//			uBean.setOrderInfoSet(orderSet); // U-Os 關聯
//			uBean.setProductInfoSet(productInfoSet); // U-Ps 關聯
			oBean.setProductInfo(pBean); // O-P 關聯
			oBean.setUser_Info(uBean); // O-U 關聯
//			pBean.setUser_Info(uBean); // P-U 關聯
			
			em.merge(oBean); 
		} else {
			System.out.println("*** Order with O_ID = " + newOBean.getO_id() + "doesn't exist in the database :^) ***");
		}
		updateStatus = true;
		return updateStatus;
	}

	
	public Integer delete(Integer[] o_ids) {
		Query deleteQuery = em.createQuery("DELETE OrderInfo WHERE o_id IN (:oids)");
		deleteQuery.setParameter("oids", Arrays.asList(o_ids));
		Integer result = deleteQuery.executeUpdate();
		return result;
	}

}