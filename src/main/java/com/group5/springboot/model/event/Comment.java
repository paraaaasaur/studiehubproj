package com.group5.springboot.model.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "Comment")
@Component
public class Comment {

	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 private String content ;
	 @ManyToOne
	 private Article article;
	 
	 public Comment() {
	 }
	 
	 public void dleteComments() {
		 
          this.getArticle().getComments().remove(this);
	}
	 
	 public Comment(String content) {
		 this.content =  content ;
	 } 
	 
	 

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Article getArticle() {
		return article;
	}


	public void setArticle(Article article) {
		this.article = article;
	}
	 
	
	 
}
