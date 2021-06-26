package com.group5.springboot.validate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.group5.springboot.model.product.ProductInfo;
@Component
public class ProductValidator implements org.springframework.validation.Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ProductInfo.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		ProductInfo productInfo = (ProductInfo) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "p_Name", "productInfo.p_Name.notempty", "課程名稱必須填寫");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "p_Price", "productInfo.p_Class.notempty", "課程價錢必須填寫");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "p_Class", "productInfo.p_Price.notempty", "課程類別必須填寫");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descString", "productInfo.descString.notempty", "課程介紹必須填寫");
	}

}
