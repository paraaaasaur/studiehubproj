package com.group5.springboot.validate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.group5.springboot.model.user.User_Info;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// 要檢查的物件
		return User_Info.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User_Info user_Info = (User_Info)target;
		ValidationUtils.rejectIfEmpty(errors, "u_lastname",		"",  "姓氏不能空白!");
		ValidationUtils.rejectIfEmpty(errors, "u_firstname",	"",  "名字不能空白!");
		ValidationUtils.rejectIfEmpty(errors, "u_email",		"",  "信箱不能空白!");
	}

}
