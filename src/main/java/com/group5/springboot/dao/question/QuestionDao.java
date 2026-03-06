package com.group5.springboot.dao.question;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.group5.springboot.model.question.Question_Info;

@Repository
public interface QuestionDao {
	void insertQuestion(Question_Info question_Info) ;
	
	Map<String, Object> findAllQuestions();
	
	Question_Info findById(Long q_id);

	Question_Info findApprovedById(Long q_id);

	void deleteQuestion(Question_Info question_Info);
	
	Map<String, Object> queryByName(String qname);

	void update(Question_Info question_Info);

	////送出隨機綜合題測驗題目
	Map<String, Object> sendRandomMixExam();

	////送出待審核資料
	Map<String, Object> sendVerifyQuestion();
}