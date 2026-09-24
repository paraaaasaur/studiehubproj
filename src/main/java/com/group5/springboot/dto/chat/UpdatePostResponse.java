package com.group5.springboot.dto.chat;

/** Mirrors {@link UpdatePostRequest}, except the {@code c_ID} field is omitted */
public final class UpdatePostResponse {
	private final Integer c_IDr;
	private final String c_Date;
	private final String c_Conts;
	// No u_ID required, because it's usually derived directly from sessions.
	// private final String u_ID;


	public UpdatePostResponse(Integer c_IDr, String c_Date, String c_Conts) {
		this.c_IDr = c_IDr;
		this.c_Date = c_Date;
		this.c_Conts = c_Conts;
	}


	public Integer getC_IDr() {
		return c_IDr;
	}

	public String getC_Date() {
		return c_Date;
	}

	public String getC_Conts() {
		return c_Conts;
	}


	@Override
	public String toString() {
		return "UpdatePostRequest{" +
			   "c_IDr=" + c_IDr +
			   ", c_Date='" + c_Date + '\'' +
			   ", c_Conts='" + c_Conts + '\'' +
			   '}';
	}
}
