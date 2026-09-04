package com.group5.springboot.dto.user;

import java.sql.Date;

/** 
 * Difference between {@link UpdateProfileRequest}:<br>
 * - Has user ID field<br>
 * - No media field (x1)<br>
 */
public final class UpdateProfileForm {
	private final String u_id;
	private final String u_lastname;
	private final String u_firstname;
	private final String u_address;
	private final String u_email;
	private final String u_tel;
	private final Date u_birthday;
	private final String u_gender;


	public UpdateProfileForm(String u_id, String u_lastname, String u_firstname, String u_address, String u_email, String u_tel, Date u_birthday, String u_gender) {
		this.u_id = u_id;
		this.u_lastname = u_lastname;
		this.u_firstname = u_firstname;
		this.u_address = u_address;
		this.u_email = u_email;
		this.u_tel = u_tel;
		this.u_birthday = u_birthday;
		this.u_gender = u_gender;
	}


	public String getU_id() {
		return u_id;
	}

	public String getU_lastname() {
		return u_lastname;
	}

	public String getU_firstname() {
		return u_firstname;
	}

	public String getU_address() {
		return u_address;
	}

	public String getU_email() {
		return u_email;
	}

	public String getU_tel() {
		return u_tel;
	}

	public Date getU_birthday() {
		return u_birthday;
	}

	public String getU_gender() {
		return u_gender;
	}


	@Override
	public String toString() {
		return "UpdateProfileForm{" +
			   "u_id='" + u_id + '\'' +
			   ", u_lastname='" + u_lastname + '\'' +
			   ", u_firstname='" + u_firstname + '\'' +
			   ", u_address='" + u_address + '\'' +
			   ", u_email='" + u_email + '\'' +
			   ", u_tel='" + u_tel + '\'' +
			   ", u_birthday='" + u_birthday + '\'' +
			   ", u_gender='" + u_gender + '\'' +
			   '}';
	}
}