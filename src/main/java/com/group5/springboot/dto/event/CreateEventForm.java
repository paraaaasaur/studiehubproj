package com.group5.springboot.dto.event;

import org.springframework.web.multipart.MultipartFile;

public final class CreateEventForm {
	private final String a_name;
	private final String a_type;
	private final String registration_starttime;
	private final String registration_endrttime;
	private final String transienta_startTime;
	private final String transienta_endTime;
	private final String a_address;
	private final String transientcomment;
	private final int applicants;
	private final MultipartFile eventImage;


	public CreateEventForm(String a_name, String a_type, String registration_starttime, String registration_endrttime, String transienta_startTime, String transienta_endTime, String a_address, String transientcomment, int applicants, MultipartFile eventImage) {
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

	public String getA_type() {
		return a_type;
	}

	public String getRegistration_starttime() {
		return registration_starttime;
	}

	public String getRegistration_endrttime() {
		return registration_endrttime;
	}

	public String getTransienta_startTime() {
		return transienta_startTime;
	}

	public String getTransienta_endTime() {
		return transienta_endTime;
	}

	public String getA_address() {
		return a_address;
	}

	public String getTransientcomment() {
		return transientcomment;
	}

	public int getApplicants() {
		return applicants;
	}

	public MultipartFile getEventImage() {
		return eventImage;
	}


	@Override
	public String toString() {
		return "CreateEventForm{" +
			   "a_name='" + a_name + '\'' +
			   ", a_type='" + a_type + '\'' +
			   ", registration_starttime='" + registration_starttime + '\'' +
			   ", registration_endrttime='" + registration_endrttime + '\'' +
			   ", transienta_startTime='" + transienta_startTime + '\'' +
			   ", transienta_endTime='" + transienta_endTime + '\'' +
			   ", a_address='" + a_address + '\'' +
			   ", transientcomment='" + transientcomment + '\'' +
			   ", applicants=" + applicants +
			   ", eventImage=" + eventImage +
			   '}';
	}
}
