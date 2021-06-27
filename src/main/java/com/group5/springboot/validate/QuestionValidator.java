package com.group5.springboot.validate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.group5.springboot.model.question.Question_Info;

@Component
public class QuestionValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Question_Info.class.isAssignableFrom(clazz); 	//寫法固定
	}

	@Override
	public void validate(Object target, Errors errors) {
		Question_Info question_Info = (Question_Info) target; //父轉子類
		ValidationUtils.rejectIfEmpty(errors, "q_class", "", "課程分類不能是空白!"); 
		ValidationUtils.rejectIfEmpty(errors, "q_type", "", "題目類型不能是空白!");
		ValidationUtils.rejectIfEmpty(errors, "q_question", "", "問題不能是空白!");
		ValidationUtils.rejectIfEmpty(errors, "q_selectionA", "", "選項A不能是空白!");
		ValidationUtils.rejectIfEmpty(errors, "q_answer", "", "正解不能是空白!");

		

	}

}
