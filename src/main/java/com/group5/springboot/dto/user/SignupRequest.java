package com.group5.springboot.dto.user;

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