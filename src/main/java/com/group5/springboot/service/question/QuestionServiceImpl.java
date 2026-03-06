package com.group5.springboot.service.question;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.group5.springboot.dao.question.QuestionDao;
import com.group5.springboot.model.question.Question_Info;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
	@Autowired QuestionDao questionDao;


	@Override
	public void insertQuestion(Question_Info question_Info) {
		questionDao.insertQuestion(question_Info);
	}
	
	@Override
	public Map<String, Object> findAllQuestions() {
		return questionDao.findAllQuestions();
	}
	
	@Override
	public Question_Info findById(Long q_id) {
		return questionDao.findById(q_id);
	}

	@Override
	public Question_Info findApprovedById(Long q_id) {
		Question_Info question;
		try {
			question = questionDao.findApprovedById(q_id);
		// Spring's auto translation for NoResultException
		} catch (EmptyResultDataAccessException e) {
			System.err.println("> Question#" + q_id + " not found");
			question = null;
		}

		return question;
	}

	@Override
	public void deleteQuestion(Question_Info question_Info) {
		questionDao.deleteQuestion(question_Info);
	}
	
	@Override
	public Map<String, Object> queryByName(String qname) {
		return questionDao.queryByName(qname);
	}
	
	public void update(Question_Info question_Info) {
		questionDao.update(question_Info);
	}
	
	////送出隨機綜合測驗題目
	@Override
	public Map<String, Object> sendRandomMixExam() {
		return questionDao.sendRandomMixExam();
	}
	
	////送出待審核資料
	@Override
	public Map<String, Object> sendVerifyQuestion() {
		return questionDao.sendVerifyQuestion();
	}
}