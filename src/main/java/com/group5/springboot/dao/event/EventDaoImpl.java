package com.group5.springboot.dao.event;



import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.event.EventInfo;




@Repository
public class EventDaoImpl implements EventDao {
	
	
	@Autowired
	EntityManager em;
	
	
	//把新增頁面POST請求 儲存進資料庫
	@Override
	public void saveEvent(EventInfo eventinfo) {

		em.persist(eventinfo);
	}
	//搜尋全部(AJAX)
	@SuppressWarnings("unchecked")
	public Map<String, Object>  EventfindAll(){
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM EventInfo";
		List<EventInfo> list = em.createQuery(hql).getResultList();
		//System.out.println("反轉前==============="+list);
		//for (EventInfo p : list) {
		//System.out.println(p.getA_name()+p.getA_aid()); 
		//}
		Collections.reverse(list);
		//讓排序反轉
        //System.out.println("反轉後==============="+list);
        //for (EventInfo p : list) {
        //System.out.println(p.getA_name()+p.getA_aid()); 
        //}
	    
		map.put("size", list.size()); 
		map.put("list", list); 
		 return map;
	}
	//模糊搜尋(AJAX)
	public Map<String, Object> queryByName(String rname) {
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM EventInfo e WHERE e.a_name like :name ";
		List<EventInfo> list =  em.createQuery(hql, EventInfo.class)
		                          .setParameter("name", "%" +  rname + "%")
		                          .getResultList();
		map.put("size", list.size());
		map.put("list", list);		
		return map;
	}
	//用a_aid搜尋(修改表單)
	public EventInfo findByid(Long id) {

		return em.find(EventInfo.class, id);
		// 依 Place.class 的主鍵做查詢 (只有主鍵可以這樣寫)
	}
	//更新表單
	public void update(EventInfo eventinfo) {

		em.merge(eventinfo);
	}
	//刪除
	public void deletdate(EventInfo eventinfo) {

		em.remove(eventinfo);
	}
	
	
	
	
	

}
