package com.group5.springboot.validate;

import com.group5.springboot.dto.cart.CreateCartItemRequest;
import com.group5.springboot.dto.cart.UpdateCartItemRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.Optional;
import java.util.stream.Stream;

@Component
public class CartValidator extends AbstractValidator {

	@Override
	public boolean supports(Class<?> clazz) {
		Optional<?> any = Stream.of(CreateCartItemRequest.class, UpdateCartItemRequest.class)
				.filter(c -> c.isAssignableFrom(clazz))
				.findAny();
		
		return any.isPresent();
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "p_id", "cartItem.p_id.notempty", "課程編號(p_id)必須填寫(DefaultMsg)");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "u_id", "cartItem.u_id.notempty", "會員帳號(u_id)必須填寫(DefaultMsg)");
	}

	public BindingResult validate(Object target) {
		return super.validate(target);
	}
}