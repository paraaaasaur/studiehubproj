package com.group5.springboot.service.product;

import java.util.Map;

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
		public Map<String, Object> queryByName(String p_Name, String typeName){
			return productDao.queryByName(p_Name, typeName);
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
