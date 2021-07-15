package com.group5.springboot.model.product;

import java.io.CharArrayWriter;
import java.io.Reader;
import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Rating")
@Component
public class Rating {
	
	@Id @Column(name = "r_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer r_ID;
	@Column(name = "p_ID")
	private Integer p_ID;
	private Integer ratedIndex;
	private Integer rating_count;
	private Clob comment;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "p_ID",insertable = false,updatable = false)
	private ProductInfo prodcuInfo;
	
	@Transient
	private String commentString;
	
	

	public Integer getP_ID() {
		return p_ID;
	}

	public void setP_ID(Integer p_ID) {
		this.p_ID = p_ID;
	}

	public Integer getR_ID() {
		return r_ID;
	}

	public void setR_ID(Integer r_ID) {
		this.r_ID = r_ID;
	}

	public Integer getRatedIndex() {
		return ratedIndex;
	}

	public void setRatedIndex(Integer ratedIndex) {
		this.ratedIndex = ratedIndex;
	}

	public Integer getRating_count() {
		return rating_count;
	}

	public void setRating_count(Integer rating_count) {
		this.rating_count = rating_count;
	}

	public String getComment() {
		String result = "";
		try {
			Reader reader = comment.getCharacterStream();
			CharArrayWriter caw = new CharArrayWriter();
			char[] c = new char[8192];
			int len = 0;
			while ((len=reader.read(c))!=-1) {
				caw.write(c, 0, len);
			}
			result = comment.getSubString(1, (int) comment.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void setComment(Clob comment) {
		this.comment = comment;
	}

	public ProductInfo getProdcuInfo() {
		return prodcuInfo;
	}

	public void setProdcuInfo(ProductInfo prodcuInfo) {
		this.prodcuInfo = prodcuInfo;
	}

	public String getCommentString() {
		return commentString;
	}

	public void setCommentString(String commentString) {
		this.commentString = commentString;
	}
	
	
}
