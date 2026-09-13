package com.group5.springboot.dto.event;

/** Mirrors {@link CreateEventForm} except that the image field is omitted. */
public final class CreateEventView {
	private final String a_name;
	private final String a_type;
	private final String registration_starttime;
	private final String registration_endrttime;
	private final String Transienta_startTime;
	private final String Transienta_endTime;
	private final String a_address;
	private final String transientcomment;
	private final int applicants;


	private CreateEventView() {
		this(null, null, null, null, null, null, null, null, 0);
	}

	public CreateEventView(String a_name, String a_type, String registration_starttime, String registration_endrttime, String Transienta_startTime, String Transienta_endTime, String a_address, String transientcomment, int applicants) {
		this.a_name = a_name;
		this.a_type = a_type;
		this.registration_starttime = registration_starttime;
		this.registration_endrttime = registration_endrttime;
		this.Transienta_startTime = Transienta_startTime;
		this.Transienta_endTime = Transienta_endTime;
		this.a_address = a_address;
		this.transientcomment = transientcomment;
		this.applicants = applicants;
	}


	public static CreateEventView newInstance() {
		return new CreateEventView();
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


	@Override
	public String toString() {
		return "CreateEventView{" +
			   "a_name='" + a_name + '\'' +
			   ", a_type='" + a_type + '\'' +
			   ", registration_starttime='" + registration_starttime + '\'' +
			   ", registration_endrttime='" + registration_endrttime + '\'' +
			   ", Transienta_startTime='" + Transienta_startTime + '\'' +
			   ", Transienta_endTime='" + Transienta_endTime + '\'' +
			   ", a_address='" + a_address + '\'' +
			   ", transientcomment='" + transientcomment + '\'' +
			   ", applicants=" + applicants +
			   '}';
	}
}
