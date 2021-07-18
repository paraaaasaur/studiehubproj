package com.group5.springboot.dao.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.question.Question_Info;

@Repository
public class QuestionDaoImpl implements QuestionDao {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	Question_Info question_Info;
	
	////新增試題
	@Override
	public void insertQuestion(Question_Info question_Info) {
		em.persist(question_Info);
		}
	
	////查詢所有試題
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findAllQuestions() {
		Map<String, Object> map = new HashMap<>();
		String hql = "from Question_Info";
		List<Question_Info> list = em.createQuery(hql).getResultList();
		System.out.println("list.get(0)=" + list.get(0));
		map.put("size", list.size()); 
		map.put("list", list); 
		 return map;
	}
	
	////查詢單筆試題
	public Question_Info findById(Long q_id) {
		return em.find(Question_Info.class, q_id);
	}
	
	////刪除
	@Override
	public void deleteQuestion(Question_Info question_Info) {
		em.remove(question_Info);
	}
	
	////模糊搜尋問題內容
	@Override
	public  Map<String, Object> queryByName(String qname){
	Map<String, Object> map = new HashMap<>();
	String hql = "FROM Question_Info p WHERE p.q_question like :q_question AND p.verification='Y' ";
	List<Question_Info> list =  em.createQuery(hql, Question_Info.class)
	         .setParameter("q_question", "%" +  qname + "%")
	         .getResultList();
	map.put("size", list.size());
	map.put("list", list);		
	return map;
}
	
	////修改試題
	@Override
	public void update(Question_Info question_Info) {
		em.merge(question_Info);
	}
	
	////送出隨機測驗題目(隨機x筆)
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> sendRandomExam() {
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM Question_Info ORDER BY NEWID()";
		List<Question_Info> list = em.createQuery(hql).setMaxResults(4).getResultList();
		//設定隨機抽樣數量:setMaxResults()
		System.out.println("list.get(0)=" + list.get(0));
		map.put("size", list.size()); 
		map.put("list", list); 
		 return map;
	}
	
	////送出隨機綜合題
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> sendRandomMixExam() {
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM Question_Info WHERE q_Type='聽力題' ORDER BY NEWID()";
		List<Question_Info> list = em.createQuery(hql).setMaxResults(4).getResultList();
		//設定隨機抽樣數量:setMaxResults()
		System.out.println("list.get(0)=" + list.get(0));
		
		String hql2 = "FROM Question_Info WHERE q_Type='多選題' ORDER BY NEWID()";
		List<Question_Info> list2 = em.createQuery(hql2).setMaxResults(3).getResultList();
		//設定隨機抽樣數量:setMaxResults()
		System.out.println("list2.get(0)=" + list2.get(0));
		
		String hql3 = "FROM Question_Info WHERE q_Type='單選題' ORDER BY NEWID()";
		List<Question_Info> list3 = em.createQuery(hql3).setMaxResults(3).getResultList();
		//設定隨機抽樣數量:setMaxResults()
		System.out.println("list3.get(0)=" + list3.get(0));
		
		list.addAll(list2);
		list.addAll(list3);
	
		map.put("size", list.size()); 
		map.put("list", list); 
		 return map;
	}
	
	////送出待審核資料
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> sendVerifyQuestion() {
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM Question_Info where verification = 'N'";
		List<Question_Info> list = em.createQuery(hql).getResultList();
		map.put("size", list.size()); 
		map.put("list", list); 
		 return map;
	}
	
}
