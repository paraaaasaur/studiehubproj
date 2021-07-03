package com.group5.springboot.controller.product;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.group5.springboot.service.product.ProductServiceImpl;

@Controller
public class ProductResultController {

	@Autowired
	ProductServiceImpl productService;
	@Autowired
	ServletContext context;
	
	@GetMapping(value="/findAllProduct", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> findAll(){
		return productService.findAll();
	}
	
	@GetMapping(value = "/queryByProductName", produces ="application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object>queryByName(@RequestParam("pname")String pname) {
		return productService.queryByName(pname);
	}
}
