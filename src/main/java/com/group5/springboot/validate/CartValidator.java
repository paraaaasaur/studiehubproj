package com.group5.springboot.validate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.ValidationUtils;

import com.group5.springboot.model.cart.CartItem;
@Component
public class CartValidator implements SmartValidator {
                                                                                                                                                                                                                                                                                                                                                                                                                                                       
	@Override
	public boolean supports(Class<?> clazz) {
		return CartItem.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		@SuppressWarnings("unused")
		CartItem cartItem = (CartItem) target; // ❓
//		void rejectIfEmpty(Errors errors, String field, String errorCode == 會去對應.properties裡設好的鍵值pair, String defaultMessage)
//		errorCode 放空值只用DefaultValue也完全可以
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "p_id", "cartItem.p_id.notempty", "課程編號(p_id)必須填寫(DefaultMsg)");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "u_id", "cartItem.u_id.notempty", "會員帳號(u_id)必須填寫(DefaultMsg)");
	}

	@Override
	public void validate(Object target, Errors errors, Object... validationHints) {
		// TODO Auto-generated method stub
		
	}

}
