package com.group5.springboot.service.question;

import java.util.Map;

import com.group5.springboot.model.question.Question_Info;

public interface QuestionService {
	void insertQuestion(Question_Info question_Info) ;
	
	Map<String, Object> findAllQuestions();

	Question_Info findById(Long q_id);

	Question_Info findApprovedById(Long q_id);
	
	void deleteQuestion(Question_Info question_Info);
	
	Map<String, Object> queryByName(String qname);
	
	void update(Question_Info question_Info);
	
	////送出隨機綜合題測驗題目
	Map<String, Object> sendRandomMixExam();

	////回傳待審核資料
	Map<String, Object> sendVerifyQuestion();
}