package com.group5.springboot.validate;

import com.group5.springboot.dto.user.ProfileForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public class UserValidator extends AbstractValidator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "u_lastname", "", "姓氏不能空白!");
		ValidationUtils.rejectIfEmpty(errors, "u_firstname", "", "名字不能空白!");
		ValidationUtils.rejectIfEmpty(errors, "u_email", "", "信箱不能空白!");
	}

	public BindingResult validate(Object target) {
		return super.validate(target);
	}
}