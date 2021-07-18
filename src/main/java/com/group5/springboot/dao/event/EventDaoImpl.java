package com.group5.springboot.dao.event;



import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.event.Article;
import com.group5.springboot.model.event.Comment;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;




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
	@SuppressWarnings("unchecked")
	public Map<String, Object>  EventfindBYuid(String a_uid){
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM EventInfo e WHERE e.a_uid = :uid ";
		List<EventInfo> list = em.createQuery(hql)
	                           .setParameter("uid", a_uid)
	                           .getResultList();
		
	    System.out.println(list);
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
		// 依 EventInfo.class 的主鍵做查詢 (只有主鍵可以這樣寫)
	}
	//更新表單
	public void update(EventInfo eventinfo) {

		em.merge(eventinfo);
	}
	//刪除
	public void deletdate(EventInfo eventinfo) {

		em.remove(eventinfo);
	}
	//新增報名表
//	@Override
//	public void saveEntryform(Entryform entryform) {
//		
//		em.persist(entryform);
//
//	}
	
	@Override
	//放進用AID搜尋過的EventInfo 跟放在 @SessionAttribute 裡的 User_Info  去關聯儲存 關聯報名表
	public void saveEntryform(EventInfo eventInfo , User_Info user_info) {
		
		Entryform Entryform = new Entryform();
		// 打開報名表
		Entryform.setEventInfo(eventInfo);
		Entryform.setE_id(user_info.getU_id());
		Entryform.setE_lastname(user_info.getU_lastname());
		Entryform.setE_firstname(user_info.getU_firstname());
		Entryform.setE_tel(user_info.getU_tel());
		Entryform.setE_email(user_info.getU_email());
		
		
		em.persist(Entryform);

	}
	
	//依AID搜尋報名者
	@SuppressWarnings("unchecked")
	public List<Entryform> findentryformByaid(EventInfo eventinfo) {
		
//		Map<String, Object> map = new HashMap<>();

		String hql = "FROM Entryform e WHERE e.eventInfo = :aid ";	
		List<Entryform> list = em.createQuery(hql)
                                 .setParameter("aid", eventinfo)
                                 .getResultList();
		 
		
//		map.put("size", list.size());
//		map.put("list", list);	
		
		return list;

	}
	
	//刪除報名表單
	@SuppressWarnings("unchecked")
	public void deleteEntryformByid(long id) {
		
		Entryform entryform = em.find(Entryform.class, id);
		
		Query query = em.createQuery("DELETE Entryform e WHERE e.id = :id");
		query.setParameter("id", entryform.getId());
		int deletedNum = query.executeUpdate();
		//回傳更新或刪除的實體數量
		if(deletedNum == 1 ) {
		System.out.println("報名表單已被刪除"+ id );
		}
		
		

	}
	
	
	//檢查活動報名表裡 UID 跟 關聯活動表 有沒有重複 
	public boolean isEntryformExist(EventInfo eventInfo , User_Info user_info) {
		boolean exist = false;
		
		try {
	    String uid = user_info.getU_id();
			
		Query query = em.createQuery("FROM Entryform e WHERE e.eventInfo = :aid and e.e_id = :eid");
		//jpa 寫的是永續類別 所以這裡要丟 活動表物件 才能找到 報名表裡的外來鍵
			
		query.setParameter("aid", eventInfo);
		
		query.setParameter("eid", uid);
		
	    query.getSingleResult();
		//query.getResultList(); 不管有沒有找到都會回傳list  用size去判斷 list是不是0 是0代表沒有查到 
		//query.getSingleResult(); 沒有找到會報錯 或大於1個抱錯 
		exist = true ;
		}
		
		catch (NoResultException ex) {
			;
			System.out.println("搜尋isEntryformExist的時候拋出異常 ");
		}
		
		//有重複回傳true  
		//沒有重複回傳false
		return exist;
	}
	//依AID 搜尋到的eventinfo 找出關聯報名表 然後 回傳有幾個 代表說有幾個報名者
	public int findentryformByaidreturnsize(EventInfo eventinfo) {

		String hql = "FROM Entryform e WHERE e.eventInfo = :aid ";
		int size = em.createQuery(hql).setParameter("aid", eventinfo).getResultList().size();

		return size;

	}
	
	
	
	
//	=====================================測試文章

	@Override
	public void saveArticle(Article article) {
		
		em.persist(article);

	}
	@Override
	public void updateArticle(Article article) {
		
		em.merge(article);
		
	}
	@Override
	public Article findByArticleid(long id) {
		
		return em.find(Article.class, id);
		
	}
	@Override
	public void deletArticle(long id) {
		
		Article article = em.find(Article.class, id);
		
		em.remove(article);
		
	}
	
//	=====================================測試評論

	@Override
	public void saveComment(Comment comment) {
		em.persist(comment);
		
	}
	@Override
	public void updateComment(Comment comment) {
		em.merge(comment);
		
	}
	@Override
	public Comment findByCommentid(long id) {
		
		return em.find(Comment.class, id);
	}
	@Override
	public void deletComment(long id) {
	
		Comment comment = em.find(Comment.class, id);
		
//		comment.dleteComments();
		
		Query query = em.createQuery("DELETE Comment c WHERE c.id = :id");
		query.setParameter("id", comment.getId());
		int deletedNum = query.executeUpdate();
		if(deletedNum == 1 ) {
		System.out.println(id+"已被刪除");
		}
//		em.remove(id);		
	}

	
	
	
	
	


	
	
	
	

}
