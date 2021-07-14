package com.group5.springboot.dao.event;



import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.event.Article;
import com.group5.springboot.model.event.Comment;
import com.group5.springboot.model.event.Entryform;
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
	@Override
	public void saveEntryform(Entryform entryform) {
		
		em.persist(entryform);

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
		if(deletedNum == 1 ) {
		System.out.println("報名表單已被刪除"+ id );
		}
		
		

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
