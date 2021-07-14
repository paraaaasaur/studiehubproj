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
	String hql = "FROM Question_Info p WHERE p.q_question like :q_question ";
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
	
	////送出隨機測驗題目
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> sendRandomExam() {
		Map<String, Object> map = new HashMap<>();
		String hql = "FROM Question_Info ORDER BY NEWID()";
		List<Question_Info> list = em.createQuery(hql).setMaxResults(4).getResultList();
		//設定隨機抽樣數量:setMaxResults()
		System.out.println("list.get(0)=" + list.get(0));
		map.put("size", list.size()); 
		//較沒啥意義，再看要改啥
		map.put("list", list); 
		 return map;
	}
	
}
