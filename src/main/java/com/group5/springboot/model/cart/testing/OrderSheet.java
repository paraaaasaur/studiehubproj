package com.group5.springboot.model.cart.testing;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity @Table(name = "order_sheet") 
@Component
public class OrderSheet {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer o_id ; // PK
	@Column(columnDefinition = "NVARCHAR(100)  DEFAULT '完成'")
	private String o_status;
	@Column(insertable = false, updatable = true, columnDefinition = "SMALLDATETIME  DEFAULT getdate()")
	private String o_date; 
	private Integer o_amt;
	
	/*********************************************************************/
	// 被OrderDetail參考
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, mappedBy = "orderSheet")
	private Set<OrderDetail> orderDetailSet = new HashSet<OrderDetail>(); 
	public Set<OrderDetail> getOrderDetailSet() {		return orderDetailSet;	}
	public void setOrderDetailSet(Set<OrderDetail> orderDetailSet) {		this.orderDetailSet = orderDetailSet;	}
	/*********************************************************************/
	
	// constructors
	public OrderSheet() {};
	public OrderSheet(Integer o_id) {		setO_id(o_id);	}
	public OrderSheet(String o_status) {		setO_status(o_status);	}
	public OrderSheet(Integer o_id, String o_status, String o_date, Integer o_amt) {
		this.o_id = o_id;
		this.o_status = o_status;
		this.o_date = o_date;
		this.o_amt = o_amt;
	}
	
	// getters
	public Integer getO_id()        {return o_id;}
	public String  getO_status()    {return o_status;}
	public String  getO_date()      {return o_date;}
	public Integer getO_amt()       {return o_amt;}
	
	// setters
	public void setO_id(Integer o_ID) {	o_id = o_ID;}
	public void setO_status(String o_Status) {o_status = o_Status;}
	public void setO_date(String o_Date) {o_date = o_Date;}
	public void setO_amt(Integer o_Amt) {o_amt = o_Amt;}
	

	
}