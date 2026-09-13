package com.group5.springboot.dto.event;

import com.group5.springboot.model.event.EventInfo;
import org.springframework.web.multipart.MultipartFile;

/** Mirrors {@link CreateEventForm} except that this has the image field. */
public final class CreateEventRequest {
	private final String a_name;
	private final String a_type;
	private final String registration_starttime;
	private final String registration_endrttime;
	private final String Transienta_startTime;
	private final String Transienta_endTime;
	private final String a_address;
	private final String transientcomment;
	private final int applicants;
	private final MultipartFile eventImage;


	public CreateEventRequest(String a_name, String a_type, String registration_starttime, String registration_endrttime, String Transienta_startTime, String Transienta_endTime, String a_address, String transientcomment, int applicants, MultipartFile eventImage) {
		this.a_name = a_name;
		this.a_type = a_type;
		this.registration_starttime = registration_starttime;
		this.registration_endrttime = registration_endrttime;
		this.Transienta_startTime = Transienta_startTime;
		this.Transienta_endTime = Transienta_endTime;
		this.a_address = a_address;
		this.transientcomment = transientcomment;
		this.applicants = applicants;
		this.eventImage = eventImage;
	}


	/**
	 * - For legacy use only.<br>
	 * - Doesn't adapt; blindly copies values to corresponding fields
	 * of the entity without considering representation boundaries.<br>
	 */
	public EventInfo dumpToEntity() {
		var entity = new EventInfo();
		entity.setA_name(this.getA_name());
		entity.setA_type(this.getA_type());
		entity.setRegistration_starttime(this.getRegistration_starttime());
		entity.setRegistration_endrttime(this.getRegistration_endrttime());
		entity.setTransienta_startTime(this.getTransienta_startTime());
		entity.setTransienta_endTime(this.getTransienta_endTime());
		entity.setA_address(this.getA_address());
		entity.setTransientcomment(this.getTransientcomment());
		entity.setApplicants(this.getApplicants());
		entity.setEventImage(this.getEventImage());

		return entity;
	}

	// getters
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
		return Transienta_startTime;
	}

	public String getTransienta_endTime() {
		return Transienta_endTime;
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
		return "CreateEventRequest{" +
			   "a_name='" + a_name + '\'' +
			   ", a_type='" + a_type + '\'' +
			   ", registration_starttime='" + registration_starttime + '\'' +
			   ", registration_endrttime='" + registration_endrttime + '\'' +
			   ", Transienta_startTime='" + Transienta_startTime + '\'' +
			   ", Transienta_endTime='" + Transienta_endTime + '\'' +
			   ", a_address='" + a_address + '\'' +
			   ", transientcomment='" + transientcomment + '\'' +
			   ", applicants=" + applicants +
			   ", eventImage=" + eventImage +
			   '}';
	}
}
