package com.group5.springboot.service.question;

import com.group5.springboot.dao.question.QuestionDao;
import com.group5.springboot.dto.question.*;
import com.group5.springboot.model.question.Question_Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
	final QuestionDao questionDao;


	@Autowired
	public QuestionServiceImpl(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}


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

	@Override
	public Question_Info applyToEntity(CreateQuestionRequest data) {
		var entity = new Question_Info();
		entity.setQ_class(data.getQ_class());
		entity.setQ_type(data.getQ_type());
		entity.setQ_question(data.getQ_question());
		entity.setQ_selectionA(data.getQ_selectionA());
		entity.setQ_selectionB(data.getQ_selectionB());
		entity.setQ_selectionC(data.getQ_selectionC());
		entity.setQ_selectionD(data.getQ_selectionD());
		entity.setQ_selectionE(data.getQ_selectionE());
		entity.setMultipartFilePic(data.getMultipartFilePic());
		entity.setMultipartFileAudio(data.getMultipartFileAudio());

		// adaption-required
		String q_answer = String.join(",", data.getAnswers());
		entity.setQ_answer(q_answer);

		return entity;
	}

	@Override
	public Question_Info applyToEntity(Long q_id, UpdateQuestionRequest data) {
		var entity = questionDao.findById(q_id);
		entity.setQ_class(data.getQ_class());
		entity.setQ_type(data.getQ_type());
		entity.setQ_question(data.getQ_question());
		entity.setQ_selectionA(data.getQ_selectionA());
		entity.setQ_selectionB(data.getQ_selectionB());
		entity.setQ_selectionC(data.getQ_selectionC());
		entity.setQ_selectionD(data.getQ_selectionD());
		entity.setQ_selectionE(data.getQ_selectionE());
		entity.setAnswers(data.getAnswers());
		entity.setMultipartFilePic(data.getMultipartFilePic());
		entity.setMultipartFileAudio(data.getMultipartFileAudio());

		return entity;
	}
}