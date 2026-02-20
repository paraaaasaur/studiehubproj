package com.group5.springboot.service.product;

import java.util.*;

import com.group5.springboot.annotation.dev.DeprecatedDetail;
import com.group5.springboot.dto.product.ProductSearchCriteria;
import com.group5.springboot.model.product.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group5.springboot.dao.product.ProductDaoImpl;
import com.group5.springboot.model.product.ProductInfo;

@Service
@Transactional
public class ProductServiceImpl {
	
	@Autowired
	ProductDaoImpl productDao;

	// 儲存資料
		public void save(ProductInfo productInfo,String u_ID) {
			productDao.save(productInfo,u_ID);
		}

		// 搜尋全部資料
		public Map<String, Object> findAll(){
			return productDao.findAll();
		}

		// 名字模糊搜尋
		@Deprecated
		@DeprecatedDetail(removeIn = "1.0.2", reason = "no usage", replaceWith = "#guestSearch, #adminSearch")
		public Map<String, Object> queryByName(String p_Name, String typeName){
			return productDao.queryByName(p_Name, typeName);
		}

		public List<ProductInfo> guestSearch(ProductSearchCriteria criteria, boolean includeRating) {
			criteria.setApproved(true);

			return productDao.search(criteria, includeRating);
		}

		public List<ProductInfo> adminSearch(ProductSearchCriteria criteria, boolean includeRating) {
			return productDao.search(criteria, includeRating);
		}

		/**<li>Calculate the average rated index value for a product.</li>
		 * <li>When a rated index is not present (null), it is ruled out
		 * from the calculation.</li>
		 *
		 * @return the floor value from the float number result of calculation</li>
		 * @throws org.hibernate.LazyInitializationException when the child entities
		 * ({@link Rating} Set) are not specified to be fetched for their parent entity
		 * ({@link ProductInfo}), they stay lazy and accesses are forbidden by Hibernate
		 **/
		public int getAverageRatedIndex(ProductInfo product) {
			double averageRatedIndex = product.getP_Rating().stream()
					.map(Rating::getRatedIndex)
					.filter(Objects::nonNull)
					.mapToInt(Integer::intValue)
					.average().orElse(0.0);

			return (int) Math.floor(averageRatedIndex);
		}

		// findbyp_id
		public ProductInfo findByProductID(Integer p_ID) {
			return productDao.findByProductID(p_ID);
		}
		

		// update
		public void update(ProductInfo productInfo) {
			productDao.update(productInfo);
		}

		// delete product
		public void deleteProduct(Integer p_ID) {
			productDao.deleteProduct(p_ID);
		}

		// check if product is exist
		public boolean isProductExist(ProductInfo productInfo) {
			return false;
		}
		
		public Map<String, Object> pendingAccess(){
			return productDao.pendingAccess();
		}
		public Integer stars(Integer p_ID) {
			return productDao.stars(p_ID);
		}

}
