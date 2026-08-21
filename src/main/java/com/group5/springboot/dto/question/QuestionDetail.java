package com.group5.springboot.dto.question;

public final class QuestionDetail {
	private final Long q_id;
	private final String q_class;
	private final String q_type;
	private final String q_question;
	private final String q_selectionA;
	private final String q_selectionB;
	private final String q_selectionC;
	private final String q_selectionD;
	private final String q_selectionE;
	private final String q_answer;
	private final String q_audioString;
	private final String q_pictureString;


	public QuestionDetail(Long q_id, String q_class, String q_type, String q_question, String q_selectionA, String q_selectionB, String q_selectionC, String q_selectionD, String q_selectionE, String q_answer, String q_audioString, String q_pictureString) {
		this.q_id = q_id;
		this.q_class = q_class;
		this.q_type = q_type;
		this.q_question = q_question;
		this.q_selectionA = q_selectionA;
		this.q_selectionB = q_selectionB;
		this.q_selectionC = q_selectionC;
		this.q_selectionD = q_selectionD;
		this.q_selectionE = q_selectionE;
		this.q_answer = q_answer;
		this.q_audioString = q_audioString;
		this.q_pictureString = q_pictureString;
	}


	public Long getQ_id() {
		return q_id;
	}

	public String getQ_class() {
		return q_class;
	}

	public String getQ_type() {
		return q_type;
	}

	public String getQ_question() {
		return q_question;
	}

	public String getQ_selectionA() {
		return q_selectionA;
	}

	public String getQ_selectionB() {
		return q_selectionB;
	}

	public String getQ_selectionC() {
		return q_selectionC;
	}

	public String getQ_selectionD() {
		return q_selectionD;
	}

	public String getQ_selectionE() {
		return q_selectionE;
	}

	public String getQ_answer() {
		return q_answer;
	}

	public String getQ_audioString() {
		return q_audioString;
	}

	public String getQ_pictureString() {
		return q_pictureString;
	}


	@Override
	public String toString() {
		return "QuestionDetail{" +
			   "q_id=" + q_id +
			   ", q_class='" + q_class + '\'' +
			   ", q_type='" + q_type + '\'' +
			   ", q_question='" + q_question + '\'' +
			   ", q_selectionA='" + q_selectionA + '\'' +
			   ", q_selectionB='" + q_selectionB + '\'' +
			   ", q_selectionC='" + q_selectionC + '\'' +
			   ", q_selectionD='" + q_selectionD + '\'' +
			   ", q_selectionE='" + q_selectionE + '\'' +
			   ", q_answer='" + q_answer + '\'' +
			   ", q_audioString='" + q_audioString + '\'' +
			   ", q_pictureString='" + q_pictureString + '\'' +
			   '}';
	}
}