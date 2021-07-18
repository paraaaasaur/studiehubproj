package com.group5.springboot.controller.product;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.group5.springboot.service.product.ProductServiceImpl;
import com.group5.springboot.service.product.RatingServiceImpl;

@Controller
public class ProductResultController {

	@Autowired
	ProductServiceImpl productService;
	@Autowired
	ServletContext context;
	@Autowired
	RatingServiceImpl ratingService;
	
	@GetMapping(value="/findAllProduct", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> findAll(){
		
		
		return productService.findAll();
	}
	@GetMapping(value="/findAllProductPendingAccess", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> findAllProductPendingAccess(){
		return productService.pendingAccess();
	}
	
	@GetMapping(value = "/queryByProductName", produces ="application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object>queryByName(@RequestParam String pname,@RequestParam String producttypename) {
		System.out.println("in controller");
		
		return productService.queryByName(pname,producttypename);
	}
}
