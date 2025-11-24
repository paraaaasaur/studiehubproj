package com.group5.springboot.dao.test;

import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.product.Rating;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

/** In courtesy of da almighty chatgpt */
@Profile("test")
@Repository
@Transactional
public class GenericDao {
	private final EntityManager em;


	@Autowired
	public GenericDao(EntityManager em) {
		this.em = em;
	}


	// -----------------------------------------
	// Basic operations
	// -----------------------------------------
	public <T, ID> T find(Class<T> clazz, ID id) {
		return em.find(clazz, id);
	}

	public <T> List<T> findAll(Class<T> clazz) {
		return em.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz).getResultList();
	}

	public <T> T save(T entity) {
		em.persist(entity);
		em.flush();   // forces DB write
		return entity;
	}

	public <T> T update(T entity) {
		T merged = em.merge(entity);
		em.flush();
		return merged;
	}

	public <T> void delete(T entity) {
		em.remove(em.contains(entity) ? entity : em.merge(entity));
		em.flush();
	}

	public <T> int deleteAll(Class<T> clazz) {
		return em.createQuery("DELETE FROM " + clazz.getSimpleName()).executeUpdate();
	}

	public ProductInfo saveProductButSkipStorage(ProductInfo rawProduct, User_Info uploader) {
		// real
		rawProduct.setUser_Info(uploader);
		rawProduct.setP_Img("dummy/path/to/image.jpg");
		rawProduct.setP_Video("dummy/path/to/video.mp4");
		rawProduct.setP_Status(0);

		// schema design defect(2.0.0)
		rawProduct.setP_createDate(new Date()); // can be defaulted at db level

		// jpa defects(2.0.0)
		rawProduct.setU_ID(uploader.getU_id()); // redundant from denormalized column
		rawProduct.setP_DESC(SystemUtils.stringToClob(rawProduct.getDescString())); // use lob

		em.persist(rawProduct);

		return rawProduct;
	}

	public ProductInfo adminApprovesProduct(ProductInfo dbProduct) {
		ProductInfo merged = em.merge(dbProduct);
		merged.setP_Status(1);

		return merged;
	}

	public Rating saveRating(Rating rawRating, ProductInfo product) {
		rawRating.setProdcuInfo(product); // missing
		rawRating.setRatedIndex(rawRating.getRatedIndex());
		rawRating.setP_ID(product.getP_ID()); // redundant
		rawRating.setComment(SystemUtils.stringToClob(rawRating.getCommentString())); // should be lob

		em.persist(rawRating); // new -> managed

		return rawRating;
	}

	// -----------------------------------------
	// Useful helpers for integration tests
	// -----------------------------------------

	/** Reload entity from DB, bypassing persistence context cache */
	public <T, ID> T reload(Class<T> clazz, ID id) {
		em.flush();
		em.clear();
		return em.find(clazz, id);
	}

	/** For test cases where you need full refresh (rare but sometimes needed) */
	public void flush() {
		em.flush();
	}

	public void clear() {
		em.clear();
	}

	public void flushAndClear() {
		em.flush();
		em.clear();
	}

}