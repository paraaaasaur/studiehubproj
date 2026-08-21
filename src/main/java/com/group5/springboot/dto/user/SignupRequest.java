package com.group5.springboot.dto.user;

import com.group5.springboot.model.user.User_Info;

public final class SignupRequest {
	private final String u_id;
	private final String u_psw;
	private final String u_lastname;
	private final String u_firstname;
	private final String u_email;


	public SignupRequest(String u_id, String u_psw, String u_lastname, String u_firstname, String u_email) {
		this.u_id = u_id;
		this.u_psw = u_psw;
		this.u_lastname = u_lastname;
		this.u_firstname = u_firstname;
		this.u_email = u_email;
	}


	public String getU_id() {
		return u_id;
	}

	public String getU_psw() {
		return u_psw;
	}

	public String getU_lastname() {
		return u_lastname;
	}

	public String getU_firstname() {
		return u_firstname;
	}

	public String getU_email() {
		return u_email;
	}


	// map to entity
	public User_Info toEntity() {
		User_Info user_info = new User_Info();
		user_info.setU_id(u_id);
		user_info.setU_psw(u_psw);
		user_info.setU_lastname(u_lastname);
		user_info.setU_firstname(u_firstname);
		user_info.setU_email(u_email);

		return user_info;
	}


	@Override
	public String toString() {
		return "SignupRequest{" +
			   "u_id='" + u_id + '\'' +
			   ", u_psw='" + u_psw + '\'' +
			   ", u_lastname='" + u_lastname + '\'' +
			   ", u_firstname='" + u_firstname + '\'' +
			   ", u_email='" + u_email + '\'' +
			   '}';
	}
}