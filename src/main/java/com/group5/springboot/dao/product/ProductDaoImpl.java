package com.group5.springboot.dao.product;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

import com.group5.springboot.dto.product.ProductSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.product.ProductInfo;

@Repository
public class ProductDaoImpl implements ProductDao {
	@Autowired
	EntityManager em;
	

	@Override
	public void save(ProductInfo productInfo,String u_ID) {
		productInfo.setU_ID(u_ID);
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
	public List<ProductInfo> search(ProductSearchCriteria criteria, boolean includeRating) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProductInfo> cq = cb.createQuery(ProductInfo.class);
		Root<ProductInfo> from = cq.from(ProductInfo.class);


		// ---------- 1. optional FETCH options ----------
		if (includeRating) {
			from.fetch("p_Rating", JoinType.LEFT);
		}


		// ---------- 2. Dynamic WHERE conditions ----------
		List<Predicate> mainPredicates = new ArrayList<>();
		cb.conjunction();
		List<Predicate> keywordPredicateParts = new ArrayList<>();

		// main predicate group
		Boolean approved = criteria.getApproved();
		if (approved != null) {
			mainPredicates.add(cb.equal(
					from.get("p_Status"),
					approved? 1 : 0
			));
		}

		// [optional] keyword predicate parts
		// (1/2)
		String pname = criteria.getPname();
		if (pname != null && !pname.isEmpty()) {
			keywordPredicateParts.add(cb.like(
					cb.lower(from.get("p_Name")),
					"%" + pname.toLowerCase() + "%"
			));
		}

		// (2/2)
		String ptype = criteria.getProducttypename();
		if (ptype != null && !ptype.isEmpty()) {
			keywordPredicateParts.add(cb.like(
					cb.lower(from.get("p_Class")),
					"%" + ptype.toLowerCase() + "%"
			));
		}


		if (!keywordPredicateParts.isEmpty()) {
			var keywordPredicate = cb.or(keywordPredicateParts.toArray(Predicate[]::new));
			mainPredicates.add(keywordPredicate);
		}

		cq.where(mainPredicates.toArray(Predicate[]::new));


		// ---------- 3. Query( ^)o(^ ) ----------
		cq.select(from);


		return em.createQuery(cq).getResultList();
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
