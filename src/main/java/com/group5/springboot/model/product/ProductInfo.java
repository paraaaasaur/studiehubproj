package com.group5.springboot.model.product;

import java.io.CharArrayWriter;
import java.io.Reader;
import java.sql.Clob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.user.User_Info;

@Entity
@Table(name = "ProductInfo")
@Component
public class ProductInfo {
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer p_ID;
	@Column(columnDefinition = "NVARCHAR(255)", nullable = false)
	private String p_Name;
	private String p_Class;
	private Integer p_Price;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date p_createDate;
//	@JsonIgnore
	private Clob p_DESC;
	@Column(columnDefinition = "NVARCHAR(255)")
	private String p_Img;
	@Column(columnDefinition = "NVARCHAR(255)")
	private String p_Video;
	private String u_ID;
	//0 = 審核中 1 = 審核通過
	private Integer p_Status;
	@Transient
	private MultipartFile imgFile;
	@Transient
	private MultipartFile videoFile;
	@Transient
	private String descString;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prodcuInfo", cascade = CascadeType.REFRESH)
	private Set<Rating> p_Rating = new HashSet<Rating>();
	
	

	
	


	public Set<Rating> getP_Rating() {
		return p_Rating;
	}

	public void setP_Rating(Set<Rating> p_Rating) {
		this.p_Rating = p_Rating;
	}

	public Integer getP_Status() {
		return p_Status;
	}

	public void setP_Status(Integer p_Status) {
		this.p_Status = p_Status;
	}

	public String getDescString() {
		return descString;
	}

	public void setDescString(String descString) {
		this.descString = descString;
	}

	/**❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗*/
	// 被OrderInfo參考
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "productInfo")
	private Set<OrderInfo> orderInfoSet = new HashSet<OrderInfo>();
	public Set<OrderInfo> getOrderInfoSet() {		return orderInfoSet;	}
	public void setOrderInfoSet(Set<OrderInfo> orderInfoSet) {		this.orderInfoSet = orderInfoSet;	}
	
	// 被CartItem參考
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "productInfo")
	private Set<CartItem> cartItemSet = new HashSet<CartItem>();
	public Set<CartItem> getCartItemSet() {		return cartItemSet;	}
	public void setCartItemSet(Set<CartItem> cartItemSet) {		this.cartItemSet = cartItemSet;	}
	
	// 去參考User_Info (※以後要和User_Info建立關聯時再開啟)
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "U_ID", referencedColumnName = "U_ID", insertable = true, updatable = true)
	private User_Info user_Info;
	public User_Info getUser_Info() {		return user_Info;	}
	public void setUser_Info(User_Info user_Info) {		this.user_Info = user_Info;	}
	
	/**❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗*/


	public Integer getP_ID() {
		return p_ID;
	}

	public void setP_ID(Integer p_ID) {
		this.p_ID = p_ID;
	}

	public String getP_Name() {
		return p_Name;
	}

	public void setP_Name(String p_Name) {
		this.p_Name = p_Name;
	}

	public String getP_Class() {
		return p_Class;
	}

	public void setP_Class(String p_Class) {
		this.p_Class = p_Class;
	}

	public Integer getP_Price() {
		return p_Price;
	}

	public void setP_Price(Integer p_Price) {
		this.p_Price = p_Price;
	}

	public Date getP_createDate() {
		return p_createDate;
	}

	public void setP_createDate(Date p_createDate) {
		this.p_createDate = p_createDate;
	}

	public String getP_DESC() {
		
		String result = "";
		try {
			Reader reader = p_DESC.getCharacterStream();
			CharArrayWriter caw = new CharArrayWriter();
			char[] c = new char[8192];
			int len = 0;
			while ((len=reader.read(c))!=-1) {
				caw.write(c, 0, len);
			}
			result = p_DESC.getSubString(1, (int) p_DESC.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void setP_DESC(Clob p_DESC) {
		this.p_DESC = p_DESC;
	}

	public String getP_Img() {
		return p_Img;
	}

	public void setP_Img(String p_Img) {
		this.p_Img = p_Img;
	}

	public String getP_Video() {
		return p_Video;
	}

	public void setP_Video(String p_Video) {
		this.p_Video = p_Video;
	}

	public String getU_ID() {
		return u_ID;
	}

	public void setU_ID(String u_ID) {
		this.u_ID = u_ID;
	}


	public MultipartFile getImgFile() {
		return imgFile;
	}

	public void setImgFile(MultipartFile imgFile) {
		this.imgFile = imgFile;
	}

	public MultipartFile getVideoFile() {
		return videoFile;
	}

	public void setVideoFile(MultipartFile videoFile) {
		this.videoFile = videoFile;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(p_ID);
		return builder.toString();
	}

	

}
