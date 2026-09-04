package com.group5.springboot.dto.user;

/**
 * Note: Unused field {@code old_psw} (1.1.0)
 **/
public final class ChangePasswordRequest {
	private final String old_psw;
	private final String u_psw;
	private final String cfm_psw; // unnecessary; belongs to frontend utility

	public ChangePasswordRequest(String old_psw, String u_psw, String cfm_psw) {
		this.old_psw = old_psw;
		this.u_psw = u_psw;
		this.cfm_psw = cfm_psw;
	}

	public String getOld_psw() {
		return old_psw;
	}

	public String getU_psw() {
		return u_psw;
	}

	public String getCfm_psw() {
		return cfm_psw;
	}

	@Override
	public String toString() {
		return "ChangePasswordRequest{" +
			   "old_psw='" + old_psw + '\'' +
			   ", u_psw='" + u_psw + '\'' +
			   ", cfm_psw='" + cfm_psw + '\'' +
			   '}';
	}
}