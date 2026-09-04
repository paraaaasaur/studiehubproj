package com.group5.springboot.service.question;

import com.group5.springboot.dto.question.*;
import com.group5.springboot.model.question.Question_Info;

import java.util.Map;

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

	CreateQuestionView mapToCreateQuestionView(CreateQuestionForm form);

	Question_Info applyToEntity(CreateQuestionForm form);

	QuestionDetail mapToQuestionDetail(Question_Info entity);

	UpdateQuestionView mapToUpdateQuestionView(Question_Info entity);

	UpdateQuestionView mapToUpdateQuestionView(Long q_id, UpdateQuestionForm form);

	Question_Info applyToEntity(Long q_id, UpdateQuestionForm form);
}