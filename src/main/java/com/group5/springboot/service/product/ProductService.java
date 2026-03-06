package com.group5.springboot.service.product;

import com.group5.springboot.dto.product.ProductSearchCriteria;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.product.Rating;

import java.util.List;
import java.util.Map;

public interface ProductService {
	void save(ProductInfo productInfo,String u_ID);

	Map<String, Object> findAll();

	List<ProductInfo> guestSearch(ProductSearchCriteria criteria, boolean includeRating);

	List<ProductInfo> adminSearch(ProductSearchCriteria criteria, boolean includeRating);

	/**<li>Calculate the average rated index value for a product.</li>
	 * <li>When a rated index is not present (null), it is ruled out
	 * from the calculation.</li>
	 *
	 * @return the floor value from the float number result of calculation</li>
	 * @throws org.hibernate.LazyInitializationException when the child entities
	 * ({@link Rating} Set) are not specified to be fetched for their parent entity
	 * ({@link ProductInfo}), they stay lazy and accesses are forbidden by Hibernate
	 **/
	int getAverageRatedIndex(ProductInfo product);

	ProductInfo findByProductID(Integer p_ID);

	void update(ProductInfo productInfo);

	void deleteProduct(Integer p_ID);

	Map<String, Object> pendingAccess();
}