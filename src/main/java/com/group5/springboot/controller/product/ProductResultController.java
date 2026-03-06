package com.group5.springboot.controller.product;

import java.util.List;
import java.util.Map;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.dto.product.ProductSearchCriteria;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductResultController {
	final ProductService productService;


	@Autowired
	public ProductResultController(ProductService productService) {
		this.productService = productService;
	}


	@GetMapping(value="/findAllProduct", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> findAll(){
		return productService.findAll();
	}

	@RequiresAdmin
	@GetMapping(value="/findAllProductPendingAccess", produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> findAllProductPendingAccess(){
		return productService.pendingAccess();
	}
	
	@GetMapping(value = "/queryByProductName", produces ="application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object>queryByName(ProductSearchCriteria criteria) {
		var products = productService.guestSearch(criteria, true);

		int[] ratedIndices = products.stream()
				.mapToInt(productService::getAverageRatedIndex)
				.toArray();

		return Map.of(
				"ratedIndex", ratedIndices,
				"list", products,
				"size", products.size()
		);
	}

	@RequiresAdmin
	@GetMapping(value = "/admin/products", produces ="application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> adminFindProducts(ProductSearchCriteria criteria, boolean includeRating) {
		List<ProductInfo> products = productService.adminSearch(criteria, includeRating);

		return Map.of(
				"list", products,
				"size", products.size()
		);
	}
}