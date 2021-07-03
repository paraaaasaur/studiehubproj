package com.group5.springboot.dao.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.product.ProductInfo;

@Repository
public class ProductDaoImpl implements ProductDao {
	@Autowired
	EntityManager em;
	

	@Override
	public void save(ProductInfo productInfo) {
		em.persist(productInfo);
	}

	@Override
	public Map<String, Object> findAll() {
		HashMap<String, Object> map = new HashMap<>();
		String hql = "from ProductInfo";
		List list = em.createQuery(hql).getResultList();
		map.put("size",list.size());
		map.put("list", list);
		return map;
	}

	@Override
	public Map<String, Object> queryByName(String p_Name) {

		HashMap<String, Object> map = new HashMap<>();
		String hql = "FROM ProductInfo p WHERE p.p_Name like :name";
		List<ProductInfo> list = em.createQuery(hql, ProductInfo.class)
					  .setParameter("name", "%"+p_Name+"%")
					  .getResultList();
		map.put("size", list.size());
		map.put("list", list);
		return map;
	}

	@Override
	public ProductInfo findByProductID(Integer p_ID) {
		return em.find(ProductInfo.class, p_ID);
	}

	@Override
	public void update(ProductInfo productInfo) {
		em.merge(productInfo);
	}

	@Override
	public void deleteProduct(Integer p_ID) {
		ProductInfo productInfo = em.find(ProductInfo.class, p_ID);
		em.remove(productInfo);
	}

	@Override
	public boolean isProductExist(ProductInfo productInfo) {
		// TODO Auto-generated method stub
		return false;
	}

}
