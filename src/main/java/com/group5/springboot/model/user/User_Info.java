package com.group5.springboot.model.user;

import java.sql.Blob;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group5.springboot.utils.SystemUtils;

@Entity
@Table(name = "user_info")
@Component("user_info")
public class User_Info {

	@Id
	@Column(name = "u_id")
	private String u_id;
	@Column(nullable = false)
	private String u_psw;
	private Date u_birthday;
	@Column(nullable = false)
	private String u_lastname;
	@Column(nullable = false)
	private String u_firstname;
	@Column(nullable = false)
	private String u_email;
	private String u_tel;
	private String u_gender;
	private String u_address;
	@JsonIgnore
	private Blob u_img;
	private String mimeType;

	// 接受前端MultipartFile
	@Transient
	private MultipartFile multipartFile;
	// Blob物件轉base64
	@Transient
	private String pictureString;
	// 抓上傳的圖片
	@Transient
	MultipartFile uploadImage;
	// 記住帳號(待查資料)
	@Transient
	String rememberMe;

	
	
	/**❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗*/
	// 被OrderInfo參考
//	@JsonIgnore
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "user_Info")
//	private Set<OrderInfo> orderInfoSet = new HashSet<OrderInfo>();
//	public Set<OrderInfo> getOrderInfoSet() {		return orderInfoSet;	}
//	public void setOrderInfoSet(Set<OrderInfo> orderInfoSet) {		this.orderInfoSet = orderInfoSet;	}
//	// 被ProductInfo參考 (※以後要和ProductInfo建立關聯時再開啟)
	
	// 被CartItem參考
//	@JsonIgnore
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "user_Info")
//	private Set<CartItem> cartItemSet = new HashSet<CartItem>();
//	public Set<CartItem> getCartItemSet() {		return cartItemSet;	}
//	public void setCartItemSet(Set<CartItem> cartItemSet) {		this.cartItemSet = cartItemSet;	}
	
	/*
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "user_Info")
	private Set<ProductInfo> productInfoSet = new HashSet<ProductInfo>();
	public Set<ProductInfo> getProductInfoSet() {		return productInfoSet;	}
	public void setProductInfoSet(Set<ProductInfo> productInfoSet) {		this.productInfoSet = productInfoSet;	}
	*/
	/**❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗*/
	
	//constructor

	public User_Info() {
	}

	public String getU_id() {
		return u_id;
	}

	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	public String getU_psw() {
		return u_psw;
	}

	public void setU_psw(String u_psw) {
		this.u_psw = u_psw;
	}

	public String getU_lastname() {
		return u_lastname;
	}

	public void setU_lastname(String u_lastname) {
		this.u_lastname = u_lastname;
	}

	public String getU_firstname() {
		return u_firstname;
	}

	public void setU_firstname(String u_firstname) {
		this.u_firstname = u_firstname;
	}

	public Date getU_birthday() {
		return u_birthday;
	}

	public void setU_birthday(Date u_birthday) {
		this.u_birthday = u_birthday;
	}

	public String getU_email() {
		return u_email;
	}

	public void setU_email(String u_email) {
		this.u_email = u_email;
	}

	public String getU_tel() {
		return u_tel;
	}

	public void setU_tel(String u_tel) {
		this.u_tel = u_tel;
	}

	public String getU_gender() {
		return u_gender;
	}

	public void setU_gender(String u_gender) {
		this.u_gender = u_gender;
	}

	public String getU_address() {
		return u_address;
	}

	public void setU_address(String u_address) {
		this.u_address = u_address;
	}

	public Blob getU_img() {
		return u_img;
	}

	public void setU_img(Blob u_img) {
		this.u_img = u_img;
	}

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public String getPictureString() {
//		return pictureString;
		return SystemUtils.blobToDataProtocol(mimeType, u_img);
	}

	public void setPictureString(String pictureString) {
		this.pictureString = pictureString;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public MultipartFile getUploadImage() {
		return uploadImage;
	}

	public void setUploadImage(MultipartFile uploadImage) {
		this.uploadImage = uploadImage;
	}

}
