package com.group5.springboot.dto;

import com.group5.springboot.model.event.EventInfo;
import org.springframework.web.multipart.MultipartFile;

public final class CreateEventRequest {
	private String a_name;
	private String a_type;
	private String registration_starttime;
	private String registration_endrttime;
	private String transienta_startTime;
	private String transienta_endTime;
	private String a_address;
	private String transientcomment;
	private int applicants;
	private MultipartFile eventImage;


	public CreateEventRequest() {}

	public CreateEventRequest(String a_name, String a_type, String registration_starttime, String registration_endrttime, String transienta_startTime, String transienta_endTime, String a_address, String transientcomment, int applicants, MultipartFile eventImage) {
		this.a_name = a_name;
		this.a_type = a_type;
		this.registration_starttime = registration_starttime;
		this.registration_endrttime = registration_endrttime;
		this.transienta_startTime = transienta_startTime;
		this.transienta_endTime = transienta_endTime;
		this.a_address = a_address;
		this.transientcomment = transientcomment;
		this.applicants = applicants;
		this.eventImage = eventImage;
	}


	public String getA_name() {
		return a_name;
	}

	public void setA_name(String a_name) {
		this.a_name = a_name;
	}

	public String getA_type() {
		return a_type;
	}

	public void setA_type(String a_type) {
		this.a_type = a_type;
	}

	public String getRegistration_starttime() {
		return registration_starttime;
	}

	public void setRegistration_starttime(String registration_starttime) {
		this.registration_starttime = registration_starttime;
	}

	public String getRegistration_endrttime() {
		return registration_endrttime;
	}

	public void setRegistration_endrttime(String registration_endrttime) {
		this.registration_endrttime = registration_endrttime;
	}

	public String getTransienta_startTime() {
		return transienta_startTime;
	}

	public void setTransienta_startTime(String transienta_startTime) {
		this.transienta_startTime = transienta_startTime;
	}

	public String getTransienta_endTime() {
		return transienta_endTime;
	}

	public void setTransienta_endTime(String transienta_endTime) {
		this.transienta_endTime = transienta_endTime;
	}

	public String getA_address() {
		return a_address;
	}

	public void setA_address(String a_address) {
		this.a_address = a_address;
	}

	public String getTransientcomment() {
		return transientcomment;
	}

	public void setTransientcomment(String transientcomment) {
		this.transientcomment = transientcomment;
	}

	public int getApplicants() {
		return applicants;
	}

	public void setApplicants(int applicants) {
		this.applicants = applicants;
	}

	public MultipartFile getEventImage() {
		return eventImage;
	}

	public void setEventImage(MultipartFile eventImage) {
		this.eventImage = eventImage;
	}

	@Override
	public String toString() {
		return "CreateEventRequest{" +
			   "a_name='" + a_name + '\'' +
			   ", a_type='" + a_type + '\'' +
			   ", registration_starttime='" + registration_starttime + '\'' +
			   ", registration_endrttime='" + registration_endrttime + '\'' +
			   ", transienta_startTime='" + transienta_startTime + '\'' +
			   ", transienta_endTime='" + transienta_endTime + '\'' +
			   ", a_address='" + a_address + '\'' +
			   ", transientcomment='" + transientcomment + '\'' +
			   ", applicants=" + applicants + '\'' +
			   ", eventImage=" + eventImage +
			   '}';
	}

	public EventInfo toEntity() {
		var newEventInfo = new EventInfo();
		newEventInfo.setA_name(a_name);
		newEventInfo.setA_type(a_type);
		newEventInfo.setRegistration_starttime(registration_starttime);
		newEventInfo.setRegistration_endrttime(registration_endrttime);
		newEventInfo.setTransienta_startTime(transienta_startTime);
		newEventInfo.setTransienta_endTime(transienta_endTime);
		newEventInfo.setA_address(a_address);
		newEventInfo.setTransientcomment(transientcomment);
		newEventInfo.setApplicants(applicants);
		newEventInfo.setEventImage(eventImage);
		return newEventInfo;
	}
}
