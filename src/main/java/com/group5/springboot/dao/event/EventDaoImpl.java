package com.group5.springboot.dao.event;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;

@Repository
public class EventDaoImpl implements EventDao {
	@Autowired EntityManager em;


	@Override
	public void saveEvent(EventInfo eventinfo) {
		em.persist(eventinfo);
	}

	@Override
	public Map<String, Object> EventfindAll() {
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM EventInfo";
		List<EventInfo> list = em.createQuery(hql).getResultList();
		Collections.reverse(list);

		for (int i = 0; i <= list.size() - 1; i++) {
			EventInfo aaa = list.get(i);
			if (aaa.getA_endTime().getTime()<= new Date().getTime()) {
				aaa.setExpired("已過期");
				saveEvent(aaa);
			}
		}

		map.put("size", list.size());
		map.put("list", list);

		return map;
	}

	@Override
	public Map<String, Object> EventfindBYuid(String a_uid) {
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM EventInfo e WHERE e.a_uid = :uid ";
		List<EventInfo> list = em.createQuery(hql)
	                           .setParameter("uid", a_uid)
	                           .getResultList();

		map.put("size", list.size());
		map.put("list", list);

		return map;
	}

	@Override
	public Map<String, Object> queryByName(String rname) {
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM EventInfo e WHERE e.a_name like :name ";
		List<EventInfo> list =  em.createQuery(hql, EventInfo.class)
				.setParameter("name", "%" +  rname + "%")
				.getResultList();

		map.put("size", list.size());
		map.put("list", list);

		return map;
	}

	@Override
	public List<EventInfo> search(String rname, String u_id, Boolean approved, boolean includeEntryform) {
		var cb = em.getCriteriaBuilder();
		var cq = cb.createQuery(EventInfo.class);
		var from = cq.from(EventInfo.class);


		// ---------- 1. fetch option ----------
		if (includeEntryform) {
			from.fetch("entryforms", JoinType.LEFT);
		}


		// ---------- 2. dynamic filtering ----------
		Predicate predicate = cb.conjunction();

		// a. reviewed or not
		// y == approved only, n == unreviewed only, null = both
		if (approved != null) {
			predicate = cb.and(
					predicate,
					cb.equal(
							cb.lower(from.get("verification")),
							approved? "y" : "n"
					));
		}

		// b. keyword like filtering
		if (rname != null && !rname.isEmpty()) {
			predicate = cb.and(
					predicate,
					// partial match
					cb.like(
							cb.lower(from.get("a_name")),
							"%" + rname.toLowerCase() + "%"
					));
		}

		if (u_id != null && !u_id.isEmpty()) {
			predicate = cb.and(
					predicate,
					// full match
					cb.equal(
							cb.lower(from.get("a_uid")),
							u_id.toLowerCase()
					)
			);
		}

		cq.where(predicate);


		// ---------- 3. query ( ^)o(^ ) ----------
		cq.select(from);

		return em.createQuery(cq).getResultList();
	}

	@Override
	public EventInfo findByid(Long id) {
		return em.find(EventInfo.class, id);
	}

	@Override
	public void update(EventInfo eventinfo) {
		em.merge(eventinfo);
	}

	@Override
	public void deletdate(EventInfo eventinfo) {
		em.remove(eventinfo);
	}

	@Override
	public void saveEntryform(EventInfo eventInfo, User_Info user_info) {
		Entryform Entryform = new Entryform();

		Entryform.setEventInfo(eventInfo);
		Entryform.setE_id(user_info.getU_id());
		Entryform.setE_lastname(user_info.getU_lastname());
		Entryform.setE_firstname(user_info.getU_firstname());
		Entryform.setE_tel(user_info.getU_tel());
		Entryform.setE_email(user_info.getU_email());
		
		em.persist(Entryform);
	}

	@Override
	public List<Entryform> findentryformByaid(EventInfo eventinfo) {
		String hql = "FROM Entryform e WHERE e.eventInfo = :aid ";
		List<Entryform> list = em.createQuery(hql)
                                 .setParameter("aid", eventinfo)
                                 .getResultList();

		return list;
	}

	@Override
	public void deleteEntryformByid(long id) {
		Entryform entryform = em.find(Entryform.class, id);
		
		Query query = em.createQuery("DELETE Entryform e WHERE e.id = :id");
		query.setParameter("id", entryform.getId());
		query.executeUpdate();
	}

	@Override
	public boolean isEntryformExist(EventInfo eventInfo, User_Info user_info) {
		boolean exist = false;
		
		try {
			String uid = user_info.getU_id();

			Query query = em.createQuery("FROM Entryform e WHERE e.eventInfo = :aid and e.e_id = :eid");
			query.setParameter("aid", eventInfo);
			query.setParameter("eid", uid);
			query.getSingleResult();

			exist = true ;
		} catch (NoResultException ex) {
			// do nothing
		}
		
		return exist;
	}

	@Override
	public int findentryformByaidreturnsize(EventInfo eventinfo) {
		String hql = "FROM Entryform e WHERE e.eventInfo = :aid ";
		int size = em.createQuery(hql).setParameter("aid", eventinfo).getResultList().size();

		return size;
	}
}