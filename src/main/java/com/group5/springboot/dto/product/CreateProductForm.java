package com.group5.springboot.dto.product;

import org.springframework.web.multipart.MultipartFile;

public final class CreateProductForm {
	private final String u_ID;
	private final String p_Name;
	private final String p_Class;
	private final Integer p_Price;
	private final String descString;
	private final MultipartFile imgFile;
	private final MultipartFile videoFile;


	public CreateProductForm(String u_ID, String p_Name, String p_Class, Integer p_Price, String descString, MultipartFile imgFile, MultipartFile videoFile) {
		this.u_ID = u_ID;
		this.p_Name = p_Name;
		this.p_Class = p_Class;
		this.p_Price = p_Price;
		this.descString = descString;
		this.imgFile = imgFile;
		this.videoFile = videoFile;
	}


	public String getU_ID() {
		return u_ID;
	}

	public String getP_Name() {
		return p_Name;
	}

	public String getP_Class() {
		return p_Class;
	}

	public Integer getP_Price() {
		return p_Price;
	}

	public String getDescString() {
		return descString;
	}

	public MultipartFile getImgFile() {
		return imgFile;
	}

	public MultipartFile getVideoFile() {
		return videoFile;
	}


	@Override
	public String toString() {
		return "CreateProductForm{" +
			   "u_ID='" + u_ID + '\'' +
			   ", p_Name='" + p_Name + '\'' +
			   ", p_Class='" + p_Class + '\'' +
			   ", p_Price=" + p_Price +
			   ", descString='" + descString + '\'' +
			   ", imgFile=" + imgFile +
			   ", videoFile=" + videoFile +
			   '}';
	}
}