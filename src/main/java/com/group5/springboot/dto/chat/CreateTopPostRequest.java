package com.group5.springboot.dto.chat;

public final class CreateTopPostRequest {
	private final String c_Date;
	private final String c_Class;
	private final String c_Title;
	private final String c_Conts;
	// No u_ID required, because it's usually derived directly from sessions.
	// private final String u_ID;


	public CreateTopPostRequest(String c_Date, String c_Class, String c_Title, String c_Conts) {
		this.c_Date = c_Date;
		this.c_Class = c_Class;
		this.c_Title = c_Title;
		this.c_Conts = c_Conts;
	}


	public String getC_Date() {
		return c_Date;
	}

	public String getC_Class() {
		return c_Class;
	}

	public String getC_Title() {
		return c_Title;
	}

	public String getC_Conts() {
		return c_Conts;
	}


	@Override
	public String toString() {
		return "CreateTopPostRequest{" +
			   "c_Date='" + c_Date + '\'' +
			   ", c_Class='" + c_Class + '\'' +
			   ", c_Title='" + c_Title + '\'' +
			   ", c_Conts='" + c_Conts + '\'' +
			   '}';
	}
}
