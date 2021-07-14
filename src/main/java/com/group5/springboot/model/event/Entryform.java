package com.group5.springboot.model.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Entryform")
@Component
public class Entryform {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String e_id; // 會員帳號
	private String e_tel; // 會員電話
	private String e_email; // 會員信箱
	private String e_lastname; // 姓
	private String e_firstname; // 名
//	@JsonIgnore
	@ManyToOne
	private EventInfo eventInfo;

	public Entryform() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getE_id() {
		return e_id;
	}

	public void setE_id(String e_id) {
		this.e_id = e_id;
	}

	public String getE_tel() {
		return e_tel;
	}

	public void setE_tel(String e_tel) {
		this.e_tel = e_tel;
	}

	public String getE_email() {
		return e_email;
	}

	public void setE_email(String e_email) {
		this.e_email = e_email;
	}

	public String getE_lastname() {
		return e_lastname;
	}

	public void setE_lastname(String e_lastname) {
		this.e_lastname = e_lastname;
	}

	public String getE_firstname() {
		return e_firstname;
	}

	public void setE_firstname(String e_firstname) {
		this.e_firstname = e_firstname;
	}

	public EventInfo getEventInfo() {
		return eventInfo;
	}

	public void setEventInfo(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
	}

}
