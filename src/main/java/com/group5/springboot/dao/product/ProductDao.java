package com.group5.springboot.dao.product;

import java.util.List;
import java.util.Map;

import com.group5.springboot.dto.product.ProductSearchCriteria;
import com.group5.springboot.model.product.ProductInfo;

public interface ProductDao {
	void save(ProductInfo productInfo,String u_ID);

	Map<String, Object> findAll();

	List<ProductInfo> search(ProductSearchCriteria criteria, boolean includeRating);

	ProductInfo findByProductID(Integer p_ID);

	void update(ProductInfo productInfo);

	void deleteProduct(Integer p_ID);

	Map<String, Object> pendingAccess();
}