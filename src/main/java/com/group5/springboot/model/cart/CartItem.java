package com.group5.springboot.model.cart;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;

// ❗ 我是希望有ON CASCADE SET NULL 但JPA好像沒有建置這功能
@Entity @Table(name = "cart_item")
@Component
public class CartItem {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	// ❗ 沒有什麼實質意義
	private Integer cart_id ; // PK
	@Column(name = "P_ID", insertable = false, updatable = false)
	private Integer p_id; // FK
	@Column(name = "U_ID", insertable = false, updatable = false)
	private String u_id; // FK //  ⚠注意：這裡的u_id不是課程授課老師、而是購買者帳號
	@Column(insertable = false, updatable = false, columnDefinition = "SMALLDATETIME  DEFAULT getdate()")	
	private String cart_date;
	/*********************************************************************/
	// 去參考User_Info
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY) 	
	@JoinColumn(name = "U_ID", referencedColumnName = "U_ID", insertable = true, updatable = true )
	//name(OrderInf裡的外來鍵)  referencedColumnName(USER的主鍵) insertable 是否可以帶值進來(true可以) save的時候把user的uid存進去OrderInfo
	//cascade = CascadeType.All, 是否連帶操作(刪除)
	private User_Info user_Info;
	public User_Info getUser_Info() {return user_Info;}
	public void setUser_Info(User_Info user_Info) {this.user_Info = user_Info;}
	// 去參考ProductInfo
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "P_ID", referencedColumnName = "P_ID", insertable = true, updatable = true)
	private ProductInfo productInfo;
	public ProductInfo getProductInfo() {return productInfo;}
	public void setProductInfo(ProductInfo productInfo) {this.productInfo = productInfo;}
	/*********************************************************************/
	
	// constructors
	public CartItem() {};
	public CartItem(Integer cart_id, Integer p_id, String u_id, String cart_date) {
		setCart_id   ( cart_id  ) ;
		setP_id      ( p_id     ) ;
		setU_id      ( u_id     ) ;
		setCart_date ( cart_date) ;
	}
	
	// getters
	public Integer getCart_id() {		return cart_id;	}
	public Integer getP_id()        {return p_id;}
	public String  getU_id()        {return u_id;}
	public String getCart_date() {		return cart_date;	}
	
	// setters
	public void setCart_id(Integer cart_id) {		this.cart_id = cart_id;	}
	public void setP_id(Integer p_ID) {p_id = p_ID;}
	public void setU_id(String u_ID) {u_id = u_ID;}
	public void setCart_date(String cart_date) {		this.cart_date = cart_date;	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cart [getCart_id()=");
		builder.append(getCart_id());
		builder.append(", getP_id()=");
		builder.append(getP_id());
		builder.append(", getU_id()=");
		builder.append(getU_id());
		builder.append(", getCart_date()=");
		builder.append(getCart_date());
		builder.append("]");
		return builder.toString();
	}
	
	

	
}