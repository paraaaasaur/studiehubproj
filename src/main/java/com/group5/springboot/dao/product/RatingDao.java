package com.group5.springboot.dao.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.product.Rating;
@Repository
public class RatingDao {
	
	@Autowired
	EntityManager em;
	
	public void saveRating(Rating rating) {
		em.persist(rating);
	}
	
	public Map<String, Object> findAllRating(){
		HashMap<String, Object> map = new HashMap<>();
		String hql = "from Rating";
		List list = em.createQuery(hql).getResultList();
		map.put("size", list.size());
		map.put("list", list);
		return map;
	}
	
	public Map<String, Object> findRatingByProductID(Integer p_ID) {
		HashMap<String, Object> map = new HashMap<>();
		String id = String.valueOf(p_ID);
		String hql = "from Rating where p_ID = "+id;
		List list = em.createQuery(hql).getResultList();
		map.put("size", list.size());
		map.put("list", list);
		return map;
		
				
	}

}
