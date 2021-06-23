package com.group5.springboot.model.cart;

import java.io.Serializable;

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
public class OrderInfo implements Serializable{
	private static final long serialVersionUID = 1422113491878164504L;
	
	@Id @Column(name = "O_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer o_id ; // PK
	@Column(name = "P_ID", insertable = false, updatable = false) 
	private Integer p_id; // FK
	@Column(name = "P_NAME")
	private String p_name; 
	@Column(name = "P_PRICE")
	private Integer p_price; 
	@Column(name = "U_ID", insertable = false, updatable = false) 
	private String u_id; // FK
	@Column(name = "U_FIRSTNAME")
	private String u_firstname; 
	@Column(name = "U_LASTNAME")
	private String u_lastname; 
	@Column(name = "U_EMAIL")
	private String u_email; 
	@Column(name = "O_STATUS")
	private String o_status;
	@Column(name = "O_DATE", insertable = false, updatable = true)
	private String o_date; // Date()會不會更好？
	@Column(name = "O_AMT")
	private Integer o_amt;
	/*********************************************************************/
	// 去參考User_Info
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name = "U_ID", referencedColumnName = "U_ID", insertable = true, updatable = true)
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
	public OrderInfo(Integer p_ID, String p_Name, Integer p_Price, String u_ID, String u_FirstName,
			String u_LastName, String u_Email, String o_Status, String o_Date, Integer o_Amt) {
//		setO_id         (o_ID       );
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
		builder.append("Order [o_id=");
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
		builder.append("]");
		return builder.toString();
	}
	
	// 為了配合for迴圈的懶人用方法之一...
	public String take(int SqlIndex) {
		String returnedString = null;
		switch (SqlIndex) {
		case 1:
			returnedString = String.valueOf(getO_id());
			break;
		case 2:
			returnedString = String.valueOf(getP_id());
			break;
		case 3:
			returnedString = getP_name();
			break;
		case 4:
			returnedString = String.valueOf(getP_price());
			break;
		case 5:
			returnedString = getU_id();
			break;
		case 6:
			returnedString = getU_firstname();
			break;
		case 7:
			returnedString = getU_lastname();
			break;
		case 8:
			returnedString = getU_email();
			break;
		case 9:
			returnedString = getO_status();
			break;
		case 10:
			returnedString = getO_date();
			break;
		case 11:
			returnedString = String.valueOf(getO_amt());
			break;

		default:
			break;
		}
		return returnedString;
	}
	
	// 為了配合for迴圈的懶人用方法之二...
	public void assign(int SQLindex, String value) {
		switch (SQLindex) {
		case 1:
			setO_id(Integer.parseInt(value));
			break;
		case 2:
			setP_id(Integer.parseInt(value));
			break;
		case 3:
			setP_name(value);
			break;
		case 4:
			setP_price(Integer.parseInt(value));
			break;
		case 5:
			setU_id(value);
			break;
		case 6:
			setU_firstname(value);
			break;
		case 7:
			setU_lastname(value);
			break;
		case 8:
			setU_email(value);
			break;
		case 9:
			setO_status(value);
			break;
		case 10:
			setO_date(value);
			break;
		case 11:
			setO_amt(Integer.parseInt(value));
			break;

		default:
			break;
		}
	}
	
}