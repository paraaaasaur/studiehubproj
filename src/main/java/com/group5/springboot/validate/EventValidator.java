package com.group5.springboot.validate;

import com.group5.springboot.dto.event.CreateEventForm;
import com.group5.springboot.dto.event.UpdateEventForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.List;

@Component
public class EventValidator extends AbstractValidator {

	@Override
	public boolean supports(Class<?> clazz) {
		var supported = List.of(CreateEventForm.class, UpdateEventForm.class);
		return supported.contains(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "a_name",                 "event.a_name.notempty", "活動名稱為必填!");
		ValidationUtils.rejectIfEmpty(errors, "a_type",                 "event.a_type.notempty", "活動類型為必填!");
		ValidationUtils.rejectIfEmpty(errors, "a_address",              "event.a_address.notempty", "活動地址為必填!");
		ValidationUtils.rejectIfEmpty(errors, "transientcomment",       "event.transientcomment.notempty", "活動內容為必填!");
		ValidationUtils.rejectIfEmpty(errors, "applicants",             "event.applicants.notempty", "活動上限人數為必填!");
		ValidationUtils.rejectIfEmpty(errors, "registration_starttime", "event.registration_starttime.notempty", "活動報名開始時間為必填!");
		ValidationUtils.rejectIfEmpty(errors, "registration_endrttime", "event.registration_endrttime.notempty", "活動報名結束時間為必填!");
		ValidationUtils.rejectIfEmpty(errors, "Transienta_startTime",   "event.Transienta_startTime.notempty", "活動開始時間為必填!");
		ValidationUtils.rejectIfEmpty(errors, "Transienta_endTime",     "event.Transienta_endTime.notempty", "活動結束時間為必填!");
	}

	@Override
	public BindingResult validate(Object target) {
		return super.validate(target);
	}
}