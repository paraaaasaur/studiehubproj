package com.group5.springboot.dto.product;

public final class CreateRatingRequest {
	private final Integer p_ID;
	private final String commentString;
	private final Integer ratedIndex;


	public CreateRatingRequest(Integer p_ID, String commentString, Integer ratedIndex) {
		this.p_ID = p_ID;
		this.commentString = commentString;
		this.ratedIndex = ratedIndex;
	}


	public Integer getP_ID() {
		return p_ID;
	}

	public String getCommentString() {
		return commentString;
	}

	public Integer getRatedIndex() {
		return ratedIndex;
	}


	@Override
	public String toString() {
		return "CreateRatingRequest{" +
			   "p_ID=" + p_ID +
			   ", commentString='" + commentString + '\'' +
			   ", ratedIndex=" + ratedIndex +
			   '}';
	}
}
