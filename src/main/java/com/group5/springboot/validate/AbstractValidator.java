package com.group5.springboot.validate;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

public abstract class AbstractValidator implements Validator {
	/** 1. Binds errors onto the param {@code target} if any error is present.<br>
	 * 2. returns {@link BindingResult}. */
	public BindingResult validate(Object target) {
		DataBinder binder = new DataBinder(target);
		binder.setValidator(this);
		binder.validate();

		return binder.getBindingResult();
	}
}