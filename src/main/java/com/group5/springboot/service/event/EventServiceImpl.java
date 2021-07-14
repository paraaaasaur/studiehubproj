package com.group5.springboot.service.event;


import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.group5.springboot.dao.event.EventDao;
import com.group5.springboot.model.event.Article;
import com.group5.springboot.model.event.Comment;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;



@Service
@Transactional
public class EventServiceImpl {
	
	@Autowired
	EventDao EventDao;
	//新增儲存
	public void saveEvent(EventInfo eventinfo) {
		
		EventDao.saveEvent(eventinfo);
		
	};
	//用UID收搜尋顯示
	public Map<String, Object>  Eventfindbyuid(String a_uid){
		
		return EventDao.EventfindBYuid(a_uid);
	};
	//搜尋全部
	public Map<String, Object>  EventfindAll(){
		
		return EventDao.EventfindAll();
	};
    //模糊搜尋 
	public Map<String, Object> queryByName(String rname){
		 
		return EventDao.queryByName(rname);
	};
	//用a_aid搜尋(修改表單)
	public EventInfo findByid(Long id) {
		
		return EventDao.findByid(id);
	};
	//修改
	public void update(EventInfo eventinfo) {
		 EventDao.update(eventinfo);
	} ;

	public void deletdate(EventInfo eventinfo) {

		EventDao.deletdate(eventinfo);
	}
	
	public void saveEntryform(Entryform entryform) {
		
		EventDao.saveEntryform(entryform);
	};
	
	
	public List<Entryform> findentryformByaid(EventInfo eventinfo) {
	
		return EventDao.findentryformByaid(eventinfo);
	}
	
	public void deleteEntryformByid(long id) {
		
		EventDao.deleteEntryformByid(id);
	}

	
	
//	=============測試文章
	
	
	public void saveArticle(Article article) {
		
		EventDao.saveArticle(article);

	}
	
	public void updateArticle(Article article) {
		
		EventDao.updateArticle(article);		
	}
	
	public Article findByArticleid(long id) {
		
		return EventDao.findByArticleid(id);
	}
	
	public void deletArticle(long id) {
		
		EventDao.deletArticle(id);
		
	}

//	=============測試評論
	
public void saveComment(Comment comment) {
		
		EventDao.saveComment(comment);

	}
	
	public void updateComment(Comment comment) {
		
		EventDao.updateComment(comment);		
	}
	
	public Comment findByComment(long id) {
		
		return EventDao.findByCommentid(id);
	}
	
	public void deletComment(long id) {
		
//		Comment comment	=EventDao.findByCommentid(id);
//		
//		comment.dleteComment();
		
//		System.out.println(JSON.toJSONString(comment, true));


		
		EventDao.deletComment(id);
		
	}
	
	
}
