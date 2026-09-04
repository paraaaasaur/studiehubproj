package com.group5.springboot.controller.product;

import com.group5.springboot.dto.product.CreateRatingRequest;
import com.group5.springboot.service.product.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class RatingController {
	private final RatingService ratingService;


	@Autowired
	public RatingController(RatingService ratingService) {
		this.ratingService = ratingService;
	}


	@GetMapping(value = "/findRatingById", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> findRatingById(@RequestParam Integer p_ID){
		return ratingService.findRatingByProductID(p_ID);
	}

	@PostMapping("/saveRating")
	public String saveRatingResult(CreateRatingRequest req) {
		ratingService.saveRating(req);
		
		return "redirect:/takeClass/" + req.getP_ID();
	}
}