package com.group5.springboot.service.product;

import com.group5.springboot.model.product.Rating;

import java.util.Map;

public interface RatingService {
	void saveRating(Rating rating);

	Map<String, Object> findRatingByProductID(Integer p_ID);
}