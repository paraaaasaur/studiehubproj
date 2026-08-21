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
	public CreateQuestionView mapToCreateQuestionView(CreateQuestionForm form) {
		return new CreateQuestionView(
				form.getQ_class(),
				form.getQ_type(),
				form.getQ_question(),
				form.getQ_selectionA(),
				form.getQ_selectionB(),
				form.getQ_selectionC(),
				form.getQ_selectionD(),
				form.getQ_selectionE(),
				form.getAnswers()
		);
	}

	@Override
	public Question_Info applyToEntity(CreateQuestionForm form) {
		var entity = new Question_Info();
		entity.setQ_class(form.getQ_class());
		entity.setQ_type(form.getQ_type());
		entity.setQ_question(form.getQ_question());
		entity.setQ_selectionA(form.getQ_selectionA());
		entity.setQ_selectionB(form.getQ_selectionB());
		entity.setQ_selectionC(form.getQ_selectionC());
		entity.setQ_selectionD(form.getQ_selectionD());
		entity.setQ_selectionE(form.getQ_selectionE());
		entity.setMultipartFilePic(form.getMultipartFilePic());
		entity.setMultipartFileAudio(form.getMultipartFileAudio());

		// adaption-required
		String q_answer = String.join(",", form.getAnswers());
		entity.setQ_answer(q_answer);

		return entity;
	}

	@Override
	public QuestionDetail mapToQuestionDetail(Question_Info entity) {
		return new QuestionDetail(
				entity.getQ_id(),
				entity.getQ_class(),
				entity.getQ_type(),
				entity.getQ_question(),
				entity.getQ_selectionA(),
				entity.getQ_selectionB(),
				entity.getQ_selectionC(),
				entity.getQ_selectionD(),
				entity.getQ_selectionE(),
				entity.getQ_answer(),
				entity.getQ_audioString(),
				entity.getQ_pictureString()
		);
	}

	@Override
	public UpdateQuestionView mapToUpdateQuestionView(Question_Info entity) {
		String[] answers = entity.getQ_answer().split(",");

		return new UpdateQuestionView(
				entity.getQ_id(),
				entity.getQ_class(),
				entity.getQ_type(),
				entity.getQ_question(),
				entity.getQ_selectionA(),
				entity.getQ_selectionB(),
				entity.getQ_selectionC(),
				entity.getQ_selectionD(),
				entity.getQ_selectionE(),
				answers
		);
	}

	@Override
	public UpdateQuestionView mapToUpdateQuestionView(Long q_id, UpdateQuestionForm form) {
		return new UpdateQuestionView(
				q_id,
				form.getQ_class(),
				form.getQ_type(),
				form.getQ_question(),
				form.getQ_selectionA(),
				form.getQ_selectionB(),
				form.getQ_selectionC(),
				form.getQ_selectionD(),
				form.getQ_selectionE(),
				form.getAnswers()
		);
	}

	@Override
	public Question_Info applyToEntity(Long q_id, UpdateQuestionForm form) {
		var entity = questionDao.findById(q_id);
		entity.setQ_class(form.getQ_class());
		entity.setQ_type(form.getQ_type());
		entity.setQ_question(form.getQ_question());
		entity.setQ_selectionA(form.getQ_selectionA());
		entity.setQ_selectionB(form.getQ_selectionB());
		entity.setQ_selectionC(form.getQ_selectionC());
		entity.setQ_selectionD(form.getQ_selectionD());
		entity.setQ_selectionE(form.getQ_selectionE());
		entity.setAnswers(form.getAnswers());
		entity.setMultipartFilePic(form.getMultipartFilePic());
		entity.setMultipartFileAudio(form.getMultipartFileAudio());

		return entity;
	}
}