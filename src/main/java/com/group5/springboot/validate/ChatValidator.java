package com.group5.springboot.validate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.group5.springboot.model.chat.Chat_Reply;

@Component
public class ChatValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Chat_Reply.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Chat_Reply chat_Reply = (Chat_Reply)target;
		ValidationUtils.rejectIfEmpty(errors, "c_IDr",		"",  "不能空白!");
		ValidationUtils.rejectIfEmpty(errors, "c_Date",		"",  "不能空白!");
		ValidationUtils.rejectIfEmpty(errors, "U_ID",		"",  "不能空白!");
		ValidationUtils.rejectIfEmpty(errors, "c_Conts",		"",  "回覆不能空白!");
		
	}

}
