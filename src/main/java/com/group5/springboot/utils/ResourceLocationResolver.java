package com.group5.springboot.utils;

public final class ResourceLocationResolver {
	/** default display image if an event was created without an image. */
	public static final String EVENT_NO_IMAGE_URL =
//			"src/main/resources/" +
			"static/images/enevt/MemberImagexx.png";

	/** default display image if a user hasn't uploaded an avatar. */
	public static final String USER_NO_IMAGE_URL =
//			"src/main/resources/" +
			"static/images/NoImage.png";

	/** default display image if a question was created without an image. */
	public static final String QUESTION_NO_IMAGE_URL =
//			"src/main/resources/" +
			"static/images/question/NoQuestionImage.png";
}