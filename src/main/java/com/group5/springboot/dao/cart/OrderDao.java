package com.group5.springboot.dao.cart;
// 購物車的連線物件
// 要考慮做DAO Factory嗎？

import java.util.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;

@Repository
public class OrderDao implements IOrderDao {
	@Autowired // SDI✔
	private SessionFactory factory;
//	private Session session;
	// dataArrays在每次執行selectAllOrder()或selectOrder()時都會先被重製，
	// 其他需要重製的時候需要手動重製。
	public static ArrayList<ArrayList<String>> dataArrays;
	public static final String columnNames[] = {"O_ID", "P_ID", "P_Name", "P_Price", "U_ID", "U_FirstName", "U_LastName",
			"U_Email", "O_Status", "O_Date", "O_Amt"};
	
	
	@Override
	public OrderInfo insert(OrderInfo oBean) {
		Session session = factory.getCurrentSession();

		ProductInfo pBean = session.get(ProductInfo.class, oBean.getP_id());
		User_Info uBean = session.get(User_Info.class, oBean.getU_id());
		
		if(pBean == null) {
			System.out.println("********** 錯誤：以 p_id (" + oBean.getP_id() + ") 在資料庫中找不到對應的 Product 資料。 **********");
			return null;
		} else if(uBean == null) {
			System.out.println("********** 錯誤：以 o_id (" + oBean.getU_id() + ") 在資料庫中找不到對應的 User 資料。 **********");
			return null;	
		}
		// 準備綁定關聯
		Set<OrderInfo> orderSet = new HashSet<OrderInfo>();
		orderSet.add(oBean);
		// 互相綁定關聯
		pBean.setOrder(orderSet); // P-Os 關聯
		uBean.setOrder(orderSet); // U-Os 關聯
		oBean.setProductInfo(pBean); // O-P 關聯
		oBean.setUser_Info(uBean); // O-U 關聯
		
		session.save(oBean);
		return oBean;
	}
	
	@Override
	public List<OrderInfo> selectAll() {
		Session session = factory.getCurrentSession();
		Query<OrderInfo> query = session.createQuery("FROM Order_Info", OrderInfo.class);
		return query.list();
	}
	
	@Override
	public OrderInfo select(String P_ID) {
		Session session = factory.getCurrentSession();
		// ‼ HQL不是用table名而是 @Entity ‼
		Query<OrderInfo> query = session.createQuery("SELECT * FROM Order WHERE p_id = :pid", OrderInfo.class);
		query.setParameter("pid", P_ID);
		return query.uniqueResult();
	}

	@Override
	public List<OrderInfo> selectCustom(String hql) {
		Session session = factory.getCurrentSession();
		Query<OrderInfo> query = session.createQuery(hql, OrderInfo.class);
		List<OrderInfo> resultList = query.getResultList();
		return resultList;
	}
	
	// Admin - 1
	public List<OrderInfo> selectTop20() {
		Session session = factory.getCurrentSession();
		Query<OrderInfo> query = session.createQuery("FROM Order ob ORDER BY ob.o_id ASC", OrderInfo.class).setMaxResults(20);
//		Order uniqueResult = query.uniqueResult();
		List<OrderInfo> resultList = query.getResultList();
		return resultList;
	}
	// Admin - 2
	public boolean update(OrderInfo newOBean) {
		Session session = factory.getCurrentSession();
		boolean updateStatus = false;
		// 以PK查出資料庫的 O- / P- / U-Bean
		OrderInfo oBean = session.get(OrderInfo.class, newOBean.getO_id()); 
		ProductInfo pBean = session.get(ProductInfo.class, newOBean.getP_id());
		User_Info uBean = session.get(User_Info.class, newOBean.getU_id());
		
		if(pBean == null) {
			System.out.println("********** 錯誤：以 p_id (" + newOBean.getP_id() + ") 在資料庫中找不到對應的 Product 資料。 **********");
			return updateStatus;
		} else if(uBean == null) {
			System.out.println("********** 錯誤：以 o_id (" + newOBean.getU_id() + ") 在資料庫中找不到對應的 User 資料。 **********");
			return updateStatus;	
		}
		
		if (oBean != null) {
//			resultBean.setO_id         (newBean.getO_id()       ); // 無意義  
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
			// 互相綁定關聯
			pBean.setOrder(orderSet); // P-Os 關聯
			uBean.setOrder(orderSet); // U-Os 關聯
			oBean.setProductInfo(pBean); // O-P 關聯
			oBean.setUser_Info(uBean); // O-U 關聯
			
			session.update(oBean); 
		} else {
			System.out.println("*** Order with O_ID = " + newOBean.getO_id() + "doesn't exist in the database :^) ***");
		}
		updateStatus = true;
		return updateStatus;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean delete(OrderInfo orderBean) {
		Session session = factory.getCurrentSession();
		// 方法⓵ > 執行SELECT + DELETE
/**		Order resultBean = session.get(Order.class, orderBean.getO_id());
		if (resultBean != null) {
			session.delete(orderBean); 
		}*/
		// 方法⓶ > HQL
		Query query = session.createQuery("DELETE Order WHERE o_id = :oid");
		query.setParameter("oid", orderBean.getO_id());
		int deletedNum = query.executeUpdate();
		System.out.println("You deleted " + deletedNum + " row(s) from order_info table.");
		return (deletedNum == 0)? false : true;
	}

}