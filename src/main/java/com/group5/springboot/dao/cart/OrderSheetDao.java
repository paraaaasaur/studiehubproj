package com.group5.springboot.dao.cart;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderSheetDao {
	@Autowired 
	private EntityManager em;
	
	public List<?> selectAllOOPU() {
		
		// 希望可以跑得動(1)或(2)達到join三個table的查詢語句但語法好像有問題查半天也找不到有和我一樣問題的人...
		// (3) 跑得動但不是我要的，只是測試用
		
		// (1) 跑不動 (console連jpa查詢語句都跑不出來)
//		String hql = "SELECT os.o_id, os.o_amt, od.u_id, od.p_id, p.p_Name "
//				+ "FROM OrderSheet os "
//				+ "JOIN OrderDetail od ON (os.o_id = od.o_id) "
//				+ "JOIN ProductInfo p (od.p_id = p.p_ID)";
		
		// (2) 跑不動 (console都已經有成功秀出jpa查詢語句了但getResultList()時報錯)
		//  could not extract ResultSet; SQL [n/a]; nested exception is org.hibernate.exception.SQLGrammarException: could not extract ResultSet
		String hql = "SELECT new list(os.o_id, os.o_amt, od.u_id, od.p_id, p.p_Name) "
				+ " FROM OrderDetail od FULL JOIN OrderSheet os LEFT JOIN ProductInfo p "
				+ "ON (os.o_id = od.o_id) AND (od.p_id = p.p_ID)";
		
		// (3) 跑得動也秀得出結果
//		String hql = "SELECT new list(os.o_id, os.o_amt, od.u_id, od.p_id) "
//				+ " FROM OrderSheet os FULL JOIN OrderDetail od "
//				+ "ON os.o_id = od.o_id ";
		
		// (4) 跑得動也秀得出結果
//		String hql = "SELECT new list(p.p_ID, p.p_Name, od.o_id) "
//				+ " FROM ProductInfo p FULL JOIN OrderDetail od "
//				+ "ON p.p_ID = od.p_id ";
		
		Query sqlQuery = em.createQuery(hql);
		List<?> list = sqlQuery.getResultList();
		list.forEach(System.out::println);
		return list;
	}

}