package com.group5.springboot.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.service.product.ProductServiceImpl;

@Controller
public class ProductController {
	
	@Autowired
	ProductServiceImpl productService;
	
	@GetMapping("insertProduct")
	public String addProduct() {
		return "product/insertProduct";
	}

	@PostMapping("insertProduct")
	public String saveProduct(@ModelAttribute("productInfo")ProductInfo productInfo,BindingResult result,RedirectAttributes ra) {
		
		return "/";
	}
	
	@ModelAttribute("productInfo")
	public ProductInfo getProductInfo(@RequestParam(value = "p_ID",required = false)Integer p_ID) {
		ProductInfo productInfo = null;
		if (p_ID != null) {
			productInfo = productService.findByProductID(p_ID);
		}else {
			productInfo = new ProductInfo();
		}
		return productInfo;
	}
}
