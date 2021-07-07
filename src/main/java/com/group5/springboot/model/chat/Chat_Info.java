package com.group5.springboot.model.chat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "chat_Info")
@Component
public class Chat_Info {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer c_ID;
	private String c_Date;
	private String c_Class;
	private String c_Title;
	private String c_Conts;
	private String u_ID;
	
	public Integer getC_ID() {
		return c_ID;
	}
	public void setC_ID(Integer c_ID) {
		this.c_ID = c_ID;
	}
	public String getC_Date() {
		return c_Date;
	}
	public void setC_Date(String c_Date) {
		this.c_Date = c_Date;
	}
	public String getC_Class() {
		return c_Class;
	}
	public void setC_Class(String c_Class) {
		this.c_Class = c_Class;
	}
	public String getC_Title() {
		return c_Title;
	}
	public void setC_Title(String c_Title) {
		this.c_Title = c_Title;
	}
	public String getC_Conts() {
		return c_Conts;
	}
	public void setC_Conts(String c_Conts) {
		this.c_Conts = c_Conts;
	}
	public String getU_ID() {
		return u_ID;
	}
	public void setU_ID(String u_ID) {
		this.u_ID = u_ID;
	}

}
