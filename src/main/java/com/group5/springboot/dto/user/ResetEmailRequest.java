package com.group5.springboot.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class ResetEmailRequest {
	private final String u_email;


	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public ResetEmailRequest(String u_email) {
		this.u_email = u_email;
	}


	public String getU_email() {
		return u_email;
	}


	@Override
	public String toString() {
		return "ResetEmailRequest{" +
			   "u_email='" + u_email + '\'' +
			   '}';
	}
}
