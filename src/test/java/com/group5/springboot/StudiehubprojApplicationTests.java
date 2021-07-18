package com.group5.springboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.dom4j.tree.FlyweightComment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.group5.springboot.model.event.Article;
import com.group5.springboot.model.event.Comment;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.service.event.EventServiceImpl;

@SpringBootTest
class StudiehubprojApplicationTests {

	@Autowired
	EventServiceImpl eventserviceImpl;

	@Autowired
	EntityManager em;
	
	
//	@Test
	void contextLoads() {

		Map<String, Object> aaa = eventserviceImpl.EventfindAll();
		System.out.println(JSON.toJSONString(aaa, true));

	}

//	@Test
	public void saveArticle() {
		Article article = new Article();
		article.setTitle("關於創業~");
		article.setContent("創業的想法");

		List<Comment> comments = new ArrayList<>();

		Comment Comment1 = new Comment("靠杯");
		Comment1.setArticle(article);
		Comment Comment2 = new Comment("幹你娘");
		Comment2.setArticle(article);
		comments.add(Comment1);
		comments.add(Comment2);

		article.setComments(comments);

//		article.addcomment(Comment1);
//		article.addcomment(Comment2);

		eventserviceImpl.saveArticle(article);
	}

//	@Test
	public void upArticle() {

		Article article = eventserviceImpl.findByArticleid(3);
		article.setContent("測試更新3");
		List<Comment> comments = new ArrayList<>();

		Comment Comment1 = new Comment("測試更新2");
		Comment1.setArticle(article);
		Comment Comment2 = new Comment("測試更新2");
		Comment2.setArticle(article);
		comments.add(Comment1);
		comments.add(Comment2);

		article.setComments(comments);

		eventserviceImpl.updateArticle(article);

	}

//	@Test
	public void findArticle() {

		Article article = eventserviceImpl.findByArticleid(2);
		System.out.println(JSON.toJSONString(article, true));

	}

//	@Test
	public void dleteArticle() {

		eventserviceImpl.deletArticle(3);

	}

//============================評論相關	
//	 @Test
	public void SavaComment() {

		Article article = eventserviceImpl.findByArticleid(2);

		Comment comment = new Comment();
		comment.setContent("測試comment");
		comment.setArticle(article);

		eventserviceImpl.saveComment(comment);
	}
//	
//	@Test
	public void deletComment() {
		
		
		
		eventserviceImpl.deletComment(6);

	}
//	@Test
//	@SuppressWarnings("unchecked")
//	public List<Entryform> findentryformByaid(long aid) {
//		
//		List<Entryform> aaa = eventserviceImpl.findentryformByaid(aid);
//		
//		return aaa;
//
//	}
	
	

}
