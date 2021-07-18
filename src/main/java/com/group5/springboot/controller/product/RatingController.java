package com.group5.springboot.controller.product;

import java.sql.Clob;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.group5.springboot.model.product.Rating;
import com.group5.springboot.service.product.ProductServiceImpl;
import com.group5.springboot.service.product.RatingServiceImpl;
import com.group5.springboot.utils.SystemUtils;

@Controller
public class RatingController {

	@Autowired
	RatingServiceImpl ratingService;
	@Autowired
	ServletContext context;
	@Autowired
	ProductServiceImpl productService;
	
	@GetMapping(value = "/findRatingById", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> findRatingById(@RequestParam Integer p_ID){
		return ratingService.findRatingByProductID(p_ID);
	}
	
	@GetMapping(value = "/ratingAVG", produces = "application/json; charset=UTF-8")
	public @ResponseBody Integer ratingAVG(@RequestParam Integer p_ID){
		
		return ratingService.ratingAVG(p_ID);
		
	}
	
	
	@PostMapping("/saveRating")
	public String saveRatingResult(@RequestParam(value = "p_ID") Integer p_ID,@RequestParam String commentString,@RequestParam Integer ratedIndex) {
		Rating rating = new Rating();
		Clob clob = SystemUtils.stringToClob(commentString);
		rating.setComment(clob);
		rating.setRatedIndex(ratedIndex);
		rating.setP_ID(p_ID);
		ratingService.saveRating(rating);
		
		return "product/Product";
	}
}
