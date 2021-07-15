package com.group5.springboot.service.product;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group5.springboot.dao.product.RatingDao;
import com.group5.springboot.model.product.Rating;

@Service
@Transactional
public class RatingServiceImpl {

	@Autowired
	RatingDao ratingDao;
	
	public void saveRating(Rating rating) {
		ratingDao.saveRating(rating);
	}
	
	public Map<String, Object> findAllRating(){
		return ratingDao.findAllRating();
	}
	
	public Map<String, Object> findRatingByProductID(Integer p_ID){
		return ratingDao.findRatingByProductID(p_ID);
	}
	
}
