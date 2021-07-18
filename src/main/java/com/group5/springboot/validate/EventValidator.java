package com.group5.springboot.validate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.question.Question_Info;

@Component
public class EventValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return EventInfo.class.isAssignableFrom(clazz); 	//寫法固定
	}

	@Override
	public void validate(Object target, Errors errors) {
		EventInfo eventInfo = (EventInfo) target; //父轉子類 
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

}
