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
	@Column(insertable = false, updatable = false)
	private Integer p_id; // FK
	private String p_name; 
	private Integer p_price; 
	@Column(insertable = false, updatable = false)
	private String u_id; // FK //  ⚠注意：這裡的u_id不是課程授課老師、而是購買者帳號
	private String u_firstname; 
	private String u_lastname; 
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
	public CartItem() {}
	public CartItem(Integer cart_id) { setCart_id(cart_id);}
	public CartItem(Integer cart_id, Integer p_id, String u_id, String cart_date) {
		setCart_id   ( cart_id  ) ;
		setP_id      ( p_id     ) ;
		setU_id      ( u_id     ) ;
		setCart_date ( cart_date) ;
	}
	public CartItem(Integer cart_id, Integer p_id, String p_name, Integer p_price, String u_id, String u_firstname,
			String u_lastname, String cart_date) {
		setCart_id     ( cart_id    ) ;
		setP_id        ( p_id       ) ;
		setP_name      ( p_name     ) ;
		setP_price     ( p_price    ) ;
		setU_id        ( u_id       ) ;
		setU_firstname ( u_firstname) ;
		setU_lastname  ( u_lastname ) ;
		setCart_date   ( cart_date  ) ;
	}
	// getters
	public Integer getCart_id() {		return cart_id;	}
	public Integer getP_id()        {return p_id;}
	public String  getU_id()        {return u_id;}
	public String getCart_date() {		return cart_date;	}
	public String getP_name() {		return p_name;	}
	public Integer getP_price() {		return p_price;	}
	public String getU_firstname() {		return u_firstname;	}
	public String getU_lastname() {		return u_lastname;	}
	
	// setters
	public void setP_name(String p_name) {		this.p_name = p_name;	}
	public void setP_price(Integer p_price) {		this.p_price = p_price;	}
	public void setU_firstname(String u_firstname) {		this.u_firstname = u_firstname;	}
	public void setU_lastname(String u_lastname) {		this.u_lastname = u_lastname;	}
	public void setCart_id(Integer cart_id) {		this.cart_id = cart_id;	}
	public void setP_id(Integer p_ID) {p_id = p_ID;}
	public void setU_id(String u_ID) {u_id = u_ID;}
	public void setCart_date(String cart_date) {		this.cart_date = cart_date;	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CartItem [cart_id=");
		builder.append(cart_id);
		builder.append(", p_id=");
		builder.append(p_id);
		builder.append(", p_name=");
		builder.append(p_name);
		builder.append(", p_price=");
		builder.append(p_price);
		builder.append(", u_id=");
		builder.append(u_id);
		builder.append(", u_firstname=");
		builder.append(u_firstname);
		builder.append(", u_lastname=");
		builder.append(u_lastname);
		builder.append(", cart_date=");
		builder.append(cart_date);
		builder.append("]");
		return builder.toString();
	}
	
	

	
	

	
}