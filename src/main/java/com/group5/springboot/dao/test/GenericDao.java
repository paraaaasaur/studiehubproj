package com.group5.springboot.dao.test;

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