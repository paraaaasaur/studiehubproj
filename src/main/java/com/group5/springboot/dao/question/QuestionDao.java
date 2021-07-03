package com.group5.springboot.dao.question;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.group5.springboot.model.question.Question_Info;

@Repository
public interface QuestionDao {
	
	////新增試題
	public void insertQuestion(Question_Info question_Info) ;
	
	////查詢所有試題
	public Map<String, Object> findAllQuestions();
	
	////查詢單筆試題
	public Question_Info findById(Long q_id);
	
	////刪除單筆會員資料
	public void deleteQuestion(Question_Info question_Info);
	
	////模糊搜尋問題內容
	public  Map<String, Object> queryByName(String qname);

	////修改試題
	public void update(Question_Info question_Info);

	////送出隨機測驗題目
	public Map<String, Object> sendRandomExam();


}