package com.group5.springboot.controller.product;

import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.product.Rating;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public final class ProductTestUtils {
	private static int simepleSalt = 0;

	/**
	 * <li>u_ID = null or uploader.getU_id()</li>
	 * <li>p_Name = How to massage an emotion supporting croc</li>
	 * <li>p_Price = 1500</li>
	 * <li>DescString = An AWESOME course you should take!</li>
	 * <li>imgFile = {@code new MockMultipartFile("imgFile", "mock-image.jpg", MediaType.IMAGE_JPEG_VALUE, "mock-image-content".getBytes());}</li>
	 * <li>videoFile = {@code new MockMultipartFile("videoFile", "mock-video.mp4", "video/mp4", "mock-video-content".getBytes());}</li>
	 **/
	public static ProductInfo aRandomProduct() {
		ProductInfo productInfo = new ProductInfo();
		MultipartFile mockImage = new MockMultipartFile("imgFile", "mock-image.jpg", MediaType.IMAGE_JPEG_VALUE, "mock-image-content".getBytes());
		MultipartFile mockVideo = new MockMultipartFile("videoFile", "mock-video.mp4", "video/mp4", "mock-video-content".getBytes());
		{
			productInfo.setP_Name(getRandomProductName());
			productInfo.setP_Price(new Random().nextInt(10000));
			productInfo.setP_Class("法文");
			productInfo.setDescString("An AWESOME course you should take!");
			productInfo.setImgFile(mockImage);
			productInfo.setVideoFile(mockVideo);
		}

		return productInfo;
	}

	public static Rating aCustomRating(String commentString, int ratedIndex) {
		Rating rating = new Rating();
		rating.setCommentString(commentString);
		rating.setRatedIndex(ratedIndex);

		return rating;
	}


	// meme data
	private static final List<String> productNameCandidates = new ArrayList<>(List.of(
			"How to Massage an Emotion-Supporting Croc",
			"Zen and the Art of Debugging Your Life",
			"Beginner’s Guide to Arguing with Your Wi-Fi Router",
			"Advanced Procrastination with Professional Results",
			"Confidence Boosting for Overworked Houseplants",
			"How to Politely Threaten a Vending Machine",
			"Emotional Damage Control for Broken Java Developers",
			"Reacting to React: A Course in Controlled Screaming",
			"JSP Survival Skills for the Modern Human",
			"Talking to Rubber Ducks: Dialogues That Heal",
			"Caffeine Management Strategies for Night Owls",
			"Meditation for People Who Hate Meditation",
			"Negotiating Peace Between Your CSS Files",
			"Anger Management for Keyboard Smashers",
			"How to Teach Your Laptop Not to Overheat",
			"The Philosophy of The Missing Semicolon",
			"Creating Happy Databases (Before They Create You)",
			"Friendship 101: Befriending Your MockMvc",
			"Mastering the Art of Selective Hearing in Standups",
			"Turning Panic Into Productivity: A Field Guide"
	));

	private static String getRandomProductName() {
		Collections.shuffle(productNameCandidates);
		return productNameCandidates.get(0) + ": " + simepleSalt++;
	}
}