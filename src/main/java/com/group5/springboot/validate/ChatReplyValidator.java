package com.group5.springboot.validate;

import com.group5.springboot.dto.chat.UpdatePostRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public class ChatReplyValidator extends AbstractValidator {

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(UpdatePostRequest.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "c_IDr",		"",  "不能空白!");
		ValidationUtils.rejectIfEmpty(errors, "c_Date",		"",  "不能空白!");
		ValidationUtils.rejectIfEmpty(errors, "c_Conts",		"",  "回覆不能空白!");
		// [1] Missing c_ID validation, but since it's a MAJOR change, it is not allowed until 2.0.0
		// [2] U_ID validation is omitted because it's auth-driven (using session).
		// However, it's harmless for frontend to send it.

	}

	public BindingResult validate(Object target) {
		return super.validate(target);
	}
}
