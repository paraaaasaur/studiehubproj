package com.group5.springboot.service.product;

import java.util.Map;

import com.group5.springboot.dao.product.RatingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group5.springboot.model.product.Rating;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {
	@Autowired RatingDao ratingDao;


	@Override
	public void saveRating(Rating rating) {
		ratingDao.saveRating(rating);
	}

	@Override
	public Map<String, Object> findRatingByProductID(Integer p_ID) {
		return ratingDao.findRatingByProductID(p_ID);
	}
}