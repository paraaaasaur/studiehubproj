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

// Cart = ArrayList<ProductBean> = ArrayList<CartItem>
// OrderBean = cart +- 一些額外資訊
@Entity @Table(name = "order_info") 
@Component
public class OrderInfo {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer o_id ; // PK
	@Column(name = "P_ID", insertable = false, updatable = false) 
	private Integer p_id; // FK
	private String p_name; 
	private Integer p_price; 
	@Column(name = "U_ID", insertable = false, updatable = false) 
	private String u_id; // FK
	private String u_firstname; 
	private String u_lastname; 
	private String u_email; 
	@Column(columnDefinition = "NVARCHAR(100)  DEFAULT 'DONE'", insertable = false, updatable = false)
	//直接指定SQL的條件限制 
	private String o_status;
	@Column(insertable = false, updatable = false, columnDefinition = "SMALLDATETIME  DEFAULT getdate()")
	
	private String o_date; // ❗Date()會不會更好？
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
	/** 不要用這個，因為o_id現在是用IDENTITY(1, 1)去產生的，所以不想要手動指定 */
	public OrderInfo(Integer o_ID, Integer p_ID, String p_Name, Integer p_Price, String u_ID, String u_FirstName,
			String u_LastName, String u_Email, String o_Status, String o_Date, Integer o_Amt) {
		setO_id         (o_ID       );
		setP_id         (p_ID       );
		setP_name       (p_Name     );
		setP_price      (p_Price    );
		setU_id         (u_ID       );
		setU_firstname  (u_FirstName);
		setU_lastname   (u_LastName );
		setU_email      (u_Email    );
		setO_status     (o_Status   );
		setO_date       (o_Date     );
		setO_amt        (o_Amt      );
	}
	
	public OrderInfo(Integer oid) {
		setO_id(oid);
	}
	// getters
	public Integer getO_id()        {return o_id;}
	public Integer getP_id()        {return p_id;}
	public String  getP_name()      {return p_name;}
	public Integer getP_price()     {return p_price;}
	public String  getU_id()        {return u_id;}
	public String  getU_firstname() {return u_firstname;}
	public String  getU_lastname()  {return u_lastname;}
	public String  getU_email()     {return u_email;}
	public String  getO_status()    {return o_status;}
	public String  getO_date()      {return o_date;}
	public Integer getO_amt()       {return o_amt;}
	
	// setters
	public void setO_id(Integer o_ID) {	o_id = o_ID;}
	public void setP_id(Integer p_ID) {p_id = p_ID;}
	public void setP_name(String p_Name) {p_name = p_Name;}
	public void setP_price(Integer p_Price) {p_price = p_Price;}
	public void setU_id(String u_ID) {u_id = u_ID;}
	public void setU_firstname(String u_FirstName) {u_firstname = u_FirstName;}
	public void setU_lastname(String u_LastName) {u_lastname = u_LastName;}
	public void setU_email(String u_Email) {u_email = u_Email;}
	public void setO_status(String o_Status) {o_status = o_Status;}
	public void setO_date(String o_Date) {o_date = o_Date;}
	public void setO_amt(Integer o_Amt) {o_amt = o_Amt;}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderInfo [o_id=");
		builder.append(o_id);
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
		builder.append(", user_Info=");
		builder.append(user_Info);
		builder.append(", productInfo=");
		builder.append(productInfo);
		builder.append("]");
		return builder.toString();
	}
	
}