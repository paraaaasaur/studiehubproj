package com.group5.springboot.service.product;

import com.group5.springboot.dao.product.RatingDao;
import com.group5.springboot.dto.product.CreateRatingRequest;
import com.group5.springboot.model.product.Rating;
import com.group5.springboot.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Clob;
import java.util.Map;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {
	final RatingDao ratingDao;


	@Autowired
	public RatingServiceImpl(RatingDao ratingDao) {
		this.ratingDao = ratingDao;
	}


	@Override
	public void saveRating(CreateRatingRequest data) {
		var rating = applyToEntity(data);

		ratingDao.saveRating(rating);
	}

	@Override
	public Map<String, Object> findRatingByProductID(Integer p_ID) {
		return ratingDao.findRatingByProductID(p_ID);
	}


	// helpers
	private Rating applyToEntity(CreateRatingRequest data) {
		Rating rating = new Rating();
		
		Clob clob = SystemUtils.stringToClob(data.getCommentString());
		rating.setComment(clob);
		rating.setRatedIndex(data.getRatedIndex());
		rating.setP_ID(data.getP_ID());
		
		return rating;
	}
}