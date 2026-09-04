package com.group5.springboot.dto.user;

public final class LoginRequest {
	private final String u_id;
	private final String u_psw;
	private final String rememberMe;


	public LoginRequest(String u_id, String u_psw, String rememberMe) {
		this.u_id = u_id;
		this.u_psw = u_psw;
		this.rememberMe = rememberMe;
	}


	public String getU_id() {
		return u_id;
	}

	public String getU_psw() {
		return u_psw;
	}

	public String getRememberMe() {
		return rememberMe;
	}


	@Override
	public String toString() {
		return "LoginRequest{" +
			   "u_id='" + u_id + '\'' +
			   ", u_psw='" + u_psw + '\'' +
			   ", rememberMe='" + rememberMe + '\'' +
			   '}';
	}
}