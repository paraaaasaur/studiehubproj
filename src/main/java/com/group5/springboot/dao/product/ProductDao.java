package com.group5.springboot.dao.product;

import java.util.Map;

import com.group5.springboot.model.product.ProductInfo;

public interface ProductDao {

	// 儲存資料
	public void save(ProductInfo productInfo);

	// 搜尋全部資料
	Map<String, Object> findAll();

	// 名字模糊搜尋
	public Map<String, Object> queryByName(String p_Name);

	// findbyp_id
	ProductInfo findByProductID(Integer p_ID);

	// update
	public void update(ProductInfo productInfo);

	// delete product
	public void deleteProduct(Integer p_ID);

	// check if product is exist
	public boolean isProductExist(ProductInfo productInfo);
	//等待審核
	public Map<String, Object> pendingAccess();
	
	public Integer stars(Integer p_ID);

}
