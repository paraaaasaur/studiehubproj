package com.group5.springboot.validate;

import com.group5.springboot.dto.question.CreateQuestionForm;
import com.group5.springboot.dto.question.HasFieldAnswers;
import com.group5.springboot.dto.question.UpdateQuestionForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.List;

@Component
public class QuestionValidator extends AbstractValidator {
	@Override
	public boolean supports(Class<?> clazz) {
		var supported = List.of(CreateQuestionForm.class, UpdateQuestionForm.class);

		return supported.contains(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "q_class", "", "課程分類不能是空白!");
		ValidationUtils.rejectIfEmpty(errors, "q_type", "", "題目類型不能是空白!");
		ValidationUtils.rejectIfEmpty(errors, "q_question", "", "問題不能是空白!");
		ValidationUtils.rejectIfEmpty(errors, "q_selectionA", "", "選項A不能是空白!");
		ValidationUtils.rejectIfEmpty(errors, "q_selectionB", "", "選項B不能是空白!");
		ValidationUtils.rejectIfEmpty(errors, "q_selectionC", "", "選項C不能是空白!");
		ValidationUtils.rejectIfEmpty(errors, "q_selectionD", "", "選項D不能是空白!");

		if (target instanceof HasFieldAnswers) {
			String[] answers = ((HasFieldAnswers) target).getAnswers();
			if (answers.length == 0) {
				errors.rejectValue("answers", "", "正解不能是空白!");
			}
		}
	}

	@Override
	public BindingResult validate(Object target) {
		return super.validate(target);
	}
}