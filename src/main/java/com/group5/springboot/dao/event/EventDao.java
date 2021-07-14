package com.group5.springboot.dao.event;



import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.group5.springboot.model.event.Article;
import com.group5.springboot.model.event.Comment;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;

@Repository
//增刪改茶的接口
public interface EventDao {


	
	//把新增頁面POST請求 儲存進資料庫	
	public void saveEvent(EventInfo eventinfo);
	//搜尋所有+回傳list數量(AJAX)
	public Map<String, Object>  EventfindAll();
    //模糊搜尋(AJAX)
	public Map<String, Object> queryByName(String rname);
	//用UID搜尋
	public Map<String, Object>  EventfindBYuid(String a_uid);
	//用a_aid搜尋(修改表單)
	public EventInfo findByid(Long id);
	//修改表單
	public void update(EventInfo eventinfo) ;
	//刪除
	public void deletdate(EventInfo eventinfo);
	//儲存報名者
	public void saveEntryform(Entryform entryform);
	//搜尋報名清單 
	public List<Entryform> findentryformByaid(EventInfo eventinfo);
	//報名表單用e_id主鍵搜尋 再用Query刪除
	public void deleteEntryformByid(long id);


	
	
	
//	===========================================測試文章
	
	public void saveArticle(Article article);
	
	public void updateArticle(Article article);
	
	public Article findByArticleid(long id);
	
	public void deletArticle(long id);

//	===========================================測試評論	

	
	public void saveComment(Comment comment);


	public void updateComment(Comment comment);


	public Comment findByCommentid(long id);

	public void deletComment(long id);

	
}
