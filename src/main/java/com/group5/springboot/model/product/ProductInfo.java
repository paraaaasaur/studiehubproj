package com.group5.springboot.model.product;

import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group5.springboot.model.cart.OrderInfo;

@Entity
@Table(name = "ProductInfo")
@Component
public class ProductInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer p_ID;
	private String p_Name;
	private String p_Class;
	private Integer p_Price;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date p_createDate;
	@JsonIgnore
	private Clob p_DESC;
	@JsonIgnore
	private Blob p_Img;
	@JsonIgnore
	private Blob p_Video;
	// ❗ 下面兩者，之後和 User 建關聯時應該會需要要關成false
	// ❗ @Column(insertable = true, updatable = true) 
	private String u_ID;
	private String img_mimeType;
	private String video_mimeType;
	// 以下為轉檔用
	@Transient
	private String pictureString;
	@Transient
	private MultipartFile imgFile;
	@Transient
	private MultipartFile videoFile;

	
	/**❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗*/
	// 被OrderInfo參考
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "productInfo")
	private Set<OrderInfo> orderInfoSet = new HashSet<OrderInfo>();
	public Set<OrderInfo> getOrderInfoSet() {		return orderInfoSet;	}
	public void setOrderInfoSet(Set<OrderInfo> orderInfoSet) {		this.orderInfoSet = orderInfoSet;	}
	// 去參考User_Info (※以後要和User_Info建立關聯時再開啟)
	/*
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "U_ID", referencedColumnName = "U_ID", insertable = true, updatable = true)
	private User_Info user_Info;
	public User_Info getUser_Info() {		return user_Info;	}
	public void setUser_Info(User_Info user_Info) {		this.user_Info = user_Info;	}
	*/
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

	public Clob getP_DESC() {
		return p_DESC;
	}

	public void setP_DESC(Clob p_DESC) {
		this.p_DESC = p_DESC;
	}

	public Blob getP_Img() {
		return p_Img;
	}

	public void setP_Img(Blob p_Img) {
		this.p_Img = p_Img;
	}

	public Blob getP_Video() {
		return p_Video;
	}

	public void setP_Video(Blob p_Video) {
		this.p_Video = p_Video;
	}

	public String getU_ID() {
		return u_ID;
	}

	public void setU_ID(String u_ID) {
		this.u_ID = u_ID;
	}

	public String getImg_mimeType() {
		return img_mimeType;
	}

	public void setImg_mimeType(String img_mimeType) {
		this.img_mimeType = img_mimeType;
	}

	public String getVideo_mimeType() {
		return video_mimeType;
	}

	public void setVideo_mimeType(String video_mimeType) {
		this.video_mimeType = video_mimeType;
	}

	public String getPictureString() {
		return pictureString;
	}

	public void setPictureString(String pictureString) {
		this.pictureString = pictureString;
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
		builder.append("ProductInfo [p_ID=");
		builder.append(p_ID);
		builder.append(", p_Name=");
		builder.append(p_Name);
		builder.append(", p_Class=");
		builder.append(p_Class);
		builder.append(", p_Price=");
		builder.append(p_Price);
		builder.append(", p_createDate=");
		builder.append(p_createDate);
		builder.append(", p_DESC=");
		builder.append(p_DESC);
		builder.append(", p_Img=");
		builder.append(p_Img);
		builder.append(", p_Video=");
		builder.append(p_Video);
		builder.append(", u_ID=");
		builder.append(u_ID);
		builder.append(", img_mimeType=");
		builder.append(img_mimeType);
		builder.append(", video_mimeType=");
		builder.append(video_mimeType);
		builder.append(", pictureString=");
		builder.append(pictureString);
		builder.append(", imgFile=");
		builder.append(imgFile);
		builder.append(", videoFile=");
		builder.append(videoFile);
		builder.append("]");
		return builder.toString();
	}

}
