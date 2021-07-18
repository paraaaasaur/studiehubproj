package com.group5.springboot.dao.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
		String hql = "from ProductInfo where p_Status = 1";
		List list = em.createQuery(hql).getResultList();
		ArrayList<Integer> ratedIndexList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String rating = "select AVG(ratedIndex) from Rating where p_ID = " + String.valueOf((i+1));
			Integer ratedIndex = (Integer)em.createNativeQuery(rating).getSingleResult();
			ratedIndexList.add(ratedIndex);
		}
		map.put("ratedIndex", ratedIndexList);
		map.put("size",list.size());
		map.put("list", list);
		return map;
	}

	@Override
	public Map<String, Object> queryByName(String p_Name, String typeName) {

//		HashMap<String, Object> map = new HashMap<>();
//		String hql = "FROM ProductInfo p WHERE p.p_Name like :name";
//		List<ProductInfo> list = em.createQuery(hql, ProductInfo.class)
//					  .setParameter("name", "%"+p_Name+"%")
//					  .getResultList();
//		String typeName = "from ProductInfo p where p.p_Class like :typename";
//		List<ProductInfo> typeResult = em.createQuery(typeName, ProductInfo.class).setParameter("typename", "%"+p_Name+"%").getResultList();
//		map.put("size", list.size());
//		map.put("typeName", typeResult);
//		map.put("list", list);
//		map.put("typeNameSize", typeResult.size());
		if (p_Name == "") {
			p_Name = "null";
		}
		HashMap<String, Object> map = new HashMap<>();
		String hql = "from ProductInfo p where p.p_Name like:name or p_Class like :typeName";
		List list = em.createQuery(hql).setParameter("name", "%"+p_Name+"%").setParameter("typeName", typeName).getResultList();
		ArrayList<Integer> ratedIndexList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String rating = "select AVG(ratedIndex) from Rating where p_ID = " + String.valueOf(list.get(i));
			Integer ratedIndex = (Integer)em.createNativeQuery(rating).getSingleResult();
			ratedIndexList.add(ratedIndex);
		}
		map.put("ratedIndex", ratedIndexList);
		map.put("list", list);
		map.put("size",list.size());
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

	@Override
	public Map<String, Object> pendingAccess() {
		HashMap<String, Object> map = new HashMap<>();
		String hql = "from ProductInfo where p_Status = 0";
		List list = em.createQuery(hql).getResultList();
		map.put("size",list.size());
		map.put("list", list);
		return map;
	}

	@Override
	public Integer stars(Integer p_ID) {
		
		String id = String.valueOf(p_ID);
		String hql = "select AVG(ratedIndex) from Rating where p_ID ="+id;
		System.out.println(hql);
		Integer star = (Integer) em.createNativeQuery(hql).getSingleResult();
		
		return star;
	}

}
