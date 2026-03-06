package com.group5.springboot.dao.cart;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class OrderDaoImpl implements OrderDao {
	private final EntityManager em;


	@Autowired
	public OrderDaoImpl(EntityManager em) {
		this.em = em;
	}


	@Override
	public Integer getCurrentIdSeed() {
		return ((BigDecimal) em.createNativeQuery("SELECT IDENT_CURRENT('order_info')").getSingleResult()).intValue();
	}

	/** @return a boolean that indicates "can add to cart or not" */
	@Override
	public Boolean selectIfBoughtOrNot(Integer p_id, String u_id) {
		if (p_id == null || u_id == null) {
			return false;
		}

		TypedQuery<String> query = em.createQuery("SELECT o.o_status FROM OrderInfo o WHERE p_id = :pid AND u_id = :uid", String.class);
		query.setParameter("pid", p_id);
		query.setParameter("uid", u_id);
		List<String> list = query.getResultList();
		Integer counter = 0;
		for (String status : list) {
			if ("完成".equals(status)) {
				counter++;
			}
		}

		if (counter > 1) {
			System.out.println("有兩筆以上的購買完成紀錄，屬資料異常，請確認資料庫。");
			return false;
		} else if (counter == 1) {
			System.out.println("使用者" + u_id + "已購買本課程(代號 = " + p_id + ")，不得重複購買。");
			return false;
		} else if (counter == 0) {
			System.out.println("使用者" + u_id + "尚未有完成購買過本課程(代號 = " + p_id + ")的紀錄，可以加入購物車。");
		}

		return true;
	}

	@Override
	public Map<String, Object> selectLikeOperator(String condition, String value) {
		Map<String, Object> map = new HashMap<>();

		boolean isString = !("o_id".equals(condition) || "p_id".equals(condition) || "p_price".equals(condition));
		condition = (isString) ? "o." + condition : "STR(o." + condition + ")";
		TypedQuery<OrderInfo> query = em.createQuery("FROM OrderInfo o WHERE " + condition + " LIKE :value", OrderInfo.class);
		query.setParameter("value", "%" + value + "%");
		List<OrderInfo> resultList = query.getResultList();

		map.put("list", resultList);

		return map;
	}

	@Override
	public Map<String, Object> selectBy(String condition, String value) {
		Map<String, Object> map = new HashMap<>();

		Boolean isInteger = ("o_id".equals(condition) || "p_id".equals(condition) || "p_price".equals(condition));
		Object parsedValue = (isInteger) ? Integer.parseInt(value) : value;
		TypedQuery<OrderInfo> query = em.createQuery("FROM OrderInfo o WHERE " + condition + " = :value", OrderInfo.class);
		query.setParameter("value", parsedValue);
		List<OrderInfo> resultList = query.getResultList();

		map.put("list", resultList);

		return map;
	}

	@Override
	public Map<String, Object> selectWithTimeRange(String startTime, String endTime) {
		Map<String, Object> map = new HashMap<>();
		String sql = "SELECT * FROM order_info WHERE o_date >= :startTime AND o_date <= :endTime ORDER BY o_date DESC, u_id DESC";
		Query query = em.createNativeQuery(sql, OrderInfo.class);

		try {
			Date parsedStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
			Date parsedEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
			query.setParameter("startTime", parsedStartTime, TemporalType.TIME);
			query.setParameter("endTime", parsedEndTime, TemporalType.TIME);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<OrderInfo> list = (List<OrderInfo>) (query.getResultList());

		map.put("list", list);

		return map;
	}

	@Override
	public Map<String, Object> selectWithNumberRange(String condition, Integer minValue, Integer maxValue) {
		Map<String, Object> map = new HashMap<>();

		String sql = "SELECT * FROM order_info WHERE " + condition + " >= :minValue AND " + condition + "<= :maxValue ORDER BY " + condition + " DESC";
		Query query = em.createNativeQuery(sql, OrderInfo.class);
		query.setParameter("minValue", minValue);
		query.setParameter("maxValue", maxValue);

		List<OrderInfo> list = query.getResultList();

		map.put("list", list);

		return map;
	}

	@Override
	public Map<String, Object> select(OrderInfo orderBean) {
		Map<String, Object> map = new HashMap<>();

		TypedQuery<OrderInfo> query = em.createQuery("FROM OrderInfo WHERE identity_seed = :identitySeed", OrderInfo.class);
		OrderInfo result = query.setParameter("identitySeed", orderBean.getIdentity_seed()).getSingleResult();

		map.put("orderInfo", result);

		return map;
	}

	@Override
	public Map<String, Object> selectTop100() {
		Map<String, Object> map = new HashMap<>();

		List<OrderInfo> resultList = (List<OrderInfo>) (em.createNativeQuery("SELECT * FROM order_info ORDER BY o_id DESC", OrderInfo.class).setMaxResults(100).getResultList());

		map.put("list", resultList);

		return map;
	}

	@Override
	public Map<String, Object> insert(OrderInfo oBean) {
		Map<String, Object> map = new HashMap<>();

		ProductInfo pBean = em.find(ProductInfo.class, oBean.getP_id());
		User_Info uBean = em.find(User_Info.class, oBean.getU_id());

		if (pBean == null) {
			return null;
		} else if (uBean == null) {
			return null;
		}

		oBean.setU_firstname(uBean.getU_firstname());
		oBean.setU_lastname(uBean.getU_lastname());
		oBean.setU_email(uBean.getU_email());
		oBean.setP_name(pBean.getP_Name());
		oBean.setP_price(pBean.getP_Price());

		Set<OrderInfo> orderSet = new HashSet<>();
		orderSet.add(oBean);
		Set<ProductInfo> productInfoSet = new HashSet<>();
		productInfoSet.add(pBean);

		oBean.setProductInfo(pBean); // O-P 關聯
		oBean.setUser_Info(uBean); // O-U 關聯

		em.merge(oBean);

		map.put("orderBean", oBean);

		return map;
	}
}