package com.group5.springboot.validate;

import com.group5.springboot.dto.product.CreateProductForm;
import com.group5.springboot.dto.product.UpdateProductForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.List;

@Component
public class ProductValidator extends AbstractValidator {

	@Override
	public boolean supports(Class<?> clazz) {
		var supported = List.of(
				CreateProductForm.class,
				UpdateProductForm.class
		);
		return supported.contains(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "p_Name", "productInfo.p_Name.notempty", "課程名稱必須填寫");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "p_Price", "productInfo.p_Class.notempty", "課程價錢必須填寫");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "p_Class", "productInfo.p_Price.notempty", "課程類別必須填寫");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descString", "productInfo.descString.notempty", "課程介紹必須填寫");
	}

	public BindingResult validate(Object target) {
		return super.validate(target);
	}
}