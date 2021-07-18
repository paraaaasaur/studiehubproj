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

@Entity @Table(name = "order_info") 
@Component
public class OrderInfo {
	
	// ※訂單相關編號有三種
	// (1) MerchantTradeNo = 顧客要記住的特店交易編號，可以用來反查綠界交易編號和order_item的內建o_id (資料庫訂單編號)
	// (2) TradeNo = 綠界交易編號(唯一)
	// (3) order_info.o_id 資料庫訂單編號
	
	// ❗ 沒有實質意義，純粹是jpa的bean一定要一個PK...
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer identity_seed;
	private Integer o_id ; // PK
	@Column(name = "ECPAY_O_ID")
	// 特店交易編號(來自ecpay的MerchantTradeNo參數)
	private String ecpay_o_id;
	// 綠界交易編號(來自ecpay的TradeNo參數)
	private String ecpay_trade_no;
	@Column(name = "P_ID", insertable = false, updatable = false)
	private Integer p_id; // FK
	private String p_name; 
	private Integer p_price; 
	@Column(name = "U_ID", insertable = false, updatable = false)
	private String u_id; // FK
	private String u_firstname; 
	private String u_lastname; 
	private String u_email; 
	@Column(columnDefinition = "NVARCHAR(100)  DEFAULT '完成'")
	//直接指定SQL的條件限制 
	private String o_status;
	@Column(insertable = false, updatable = false, columnDefinition = "SMALLDATETIME  DEFAULT getdate()")
	private String o_date; 
	private Integer o_amt;
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
	public OrderInfo() {};
	public OrderInfo(Integer o_id) {		setO_id(o_id);	}
	public OrderInfo(String o_status) {		setO_status(o_status);	}
	public OrderInfo(ProductInfo productInfo) {
		setP_id(productInfo.getP_ID());
		setP_price(productInfo.getP_Price());
		setP_name(productInfo.getP_Name());
	}
	public OrderInfo(Integer identity_seed, Integer o_id, String o_date, Integer o_amt, String ecpay_o_id, String o_status, String ecpay_trade_no,
			Integer p_id, String p_name, Integer p_price, 
			String u_id, String u_firstname, String u_lastname, String u_email
			) {
		this.identity_seed = identity_seed;
		this.o_id = o_id;
		this.ecpay_o_id = ecpay_o_id;
		this.p_id = p_id;
		this.p_name = p_name;
		this.p_price = p_price;
		this.u_id = u_id;
		this.u_firstname = u_firstname;
		this.u_lastname = u_lastname;
		this.u_email = u_email;
		this.o_status = o_status;
		this.ecpay_trade_no = ecpay_trade_no;
		this.o_date = o_date;
		this.o_amt = o_amt;
	}
	
	
	
	// getters
	public Integer getIdentity_seed() {		return identity_seed;	}
	public Integer getO_id() {		return o_id;	}
	public String getEcpay_o_id() {		return ecpay_o_id;	}
	public Integer getP_id() {		return p_id;	}
	public String getP_name() {		return p_name;	}
	public Integer getP_price() {		return p_price;	}
	public String getU_id() {		return u_id;	}
	public String getU_firstname() {		return u_firstname;	}
	public String getU_lastname() {		return u_lastname;	}
	public String getU_email() {		return u_email;	}
	public String getO_status() {		return o_status;	}
	public String getEcpay_trade_no() {		return ecpay_trade_no;	}
	public String getO_date() {		return o_date;	}
	public Integer getO_amt() {		return o_amt;	}
	
	// setters
	public void setIdentity_seed(Integer identity_seed) {		this.identity_seed = identity_seed;	}
	public void setO_id(Integer o_id) {		this.o_id = o_id;	}
	public void setEcpay_o_id(String ecpay_o_id) {		this.ecpay_o_id = ecpay_o_id;	}
	public void setP_id(Integer p_id) {		this.p_id = p_id;	}
	public void setP_name(String p_name) {		this.p_name = p_name;	}
	public void setP_price(Integer p_price) {		this.p_price = p_price;	}
	public void setU_id(String u_id) {		this.u_id = u_id;	}
	public void setU_firstname(String u_firstname) {		this.u_firstname = u_firstname;	}
	public void setU_lastname(String u_lastname) {		this.u_lastname = u_lastname;	}
	public void setU_email(String u_email) {		this.u_email = u_email;	}
	public void setO_status(String o_status) {		this.o_status = o_status;	}
	public void setEcpay_trade_no(String ecpay_trade_no) {		this.ecpay_trade_no = ecpay_trade_no;	}
	public void setO_date(String o_date) {		this.o_date = o_date;	}
	public void setO_amt(Integer o_amt) {		this.o_amt = o_amt;	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderInfo [identity_seed=");
		builder.append(identity_seed);
		builder.append(", o_id=");
		builder.append(o_id);
		builder.append(", ecpay_o_id=");
		builder.append(ecpay_o_id);
		builder.append(", ecpay_trade_no=");
		builder.append(ecpay_trade_no);
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
		builder.append(", u_email=");
		builder.append(u_email);
		builder.append(", o_status=");
		builder.append(o_status);
		builder.append(", o_date=");
		builder.append(o_date);
		builder.append(", o_amt=");
		builder.append(o_amt);
		builder.append("]");
		return builder.toString();
	}
	

	
}