package com.group5.springboot.service.product;

import com.group5.springboot.dto.product.CreateRatingRequest;

import java.util.Map;

public interface RatingService {
	void saveRating(CreateRatingRequest data);

	Map<String, Object> findRatingByProductID(Integer p_ID);
}