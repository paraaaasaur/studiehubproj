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

@Entity @Table(name = "order_detail") 
@Component
public class OrderDetail {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer od_id;
	@Column(name = "o_id", insertable = false, updatable = false)
	private Integer o_id ; // PK
	@Column(name = "p_id", insertable = false, updatable = false)
	private Integer p_id; // FK
	@Column(name = "u_id", insertable = false, updatable = false)
	private String u_id; // FK
	/*********************************************************************/
	// 去參考Order_Info
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "o_id", referencedColumnName = "o_id", insertable = true, updatable = true)
	private OrderSheet orderSheet;
	public OrderSheet getOrderSheet() {return this.orderSheet;}
	public void setOrderSheet(OrderSheet orderSheet) {this.orderSheet = orderSheet;}
	// 去參考User_Info
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY) 	
	@JoinColumn(name = "u_id", referencedColumnName = "u_id", insertable = true, updatable = true )
	//name(OrderInf裡的外來鍵)  referencedColumnName(USER的主鍵) insertable 是否可以帶值進來(true可以) save的時候把user的uid存進去OrderInfo
	//cascade = CascadeType.All, 是否連帶操作(刪除)
	private User_Info user_Info;
	public User_Info getUser_Info() {return user_Info;}
	public void setUser_Info(User_Info user_Info) {this.user_Info = user_Info;}
	// 去參考ProductInfo
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "p_id", referencedColumnName = "p_id", insertable = true, updatable = true)
	private ProductInfo productInfo;
	public ProductInfo getProductInfo() {return productInfo;}
	public void setProductInfo(ProductInfo productInfo) {this.productInfo = productInfo;}
	/*********************************************************************/
	
	// constructors
	public OrderDetail() {};
	public OrderDetail(Integer o_id) {		setO_id(o_id);	}
	public OrderDetail(Integer o_id, Integer p_id, String u_id) {
		this.o_id = o_id;
		this.p_id = p_id;
		this.u_id = u_id;
	}
	// getters
	public Integer getO_id()        {return o_id;}
	public Integer getP_id()        {return p_id;}
	public String  getU_id()        {return u_id;}
	
	// setters
	public void setO_id(Integer o_ID) {	o_id = o_ID;}
	public void setP_id(Integer p_ID) {p_id = p_ID;}
	public void setU_id(String u_ID) {u_id = u_ID;}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderDetail [o_id=");
		builder.append(o_id);
		builder.append(", p_id=");
		builder.append(p_id);
		builder.append(", u_id=");
		builder.append(u_id);
		builder.append("]");
		return builder.toString();
	}
	

	
}