package com.group5.springboot.model.event;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "Article")
@Component
public class Article {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 private String title;
	 private String content;
     
	 @OneToMany(mappedBy = "article" , cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE} , fetch = FetchType.EAGER)
	 private  List<Comment>Comments;
	 
//	 @OneToMany(mappedBy = "article" , cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
//	 private  List<Comment>Comments = new ArrayList<>() ;
	 
	 
	 public Article() {
	 }
	 
	 
//	 public void addcomment(Comment comment){
//		comment.setArticle(this);
//		Comments.add(comment);
//	 }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Comment> getComments() {
		return Comments;
	}

	public void setComments(List<Comment> comments) {
		Comments = comments;
	}
	 
	 
}
