package com.group5.springboot.dto.product;

public final class ProductDetail {
	private final Integer p_ID;
	private final String p_Name;
	private final Integer p_Status;
	private final String p_Video;
	private final String p_Img;
	private final String p_DESC;


	public ProductDetail(Integer p_ID, String p_Name, Integer p_Status, String p_Video, String p_Img, String p_DESC) {
		this.p_ID = p_ID;
		this.p_Name = p_Name;
		this.p_Status = p_Status;
		this.p_Video = p_Video;
		this.p_Img = p_Img;
		this.p_DESC = p_DESC;
	}


	public Integer getP_ID() {
		return p_ID;
	}

	public String getP_Name() {
		return p_Name;
	}

	public Integer getP_Status() {
		return p_Status;
	}

	public String getP_Video() {
		return p_Video;
	}

	public String getP_Img() {
		return p_Img;
	}

	public String getP_DESC() {
		return p_DESC;
	}


	@Override
	public String toString() {
		return "ProductDetail{" +
			   "p_ID=" + p_ID +
			   ", p_Name='" + p_Name + '\'' +
			   ", p_Status=" + p_Status +
			   ", p_Video='" + p_Video + '\'' +
			   ", p_Img='" + p_Img + '\'' +
			   ", p_DESC='" + p_DESC + '\'' +
			   '}';
	}
}