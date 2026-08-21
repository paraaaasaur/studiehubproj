package com.group5.springboot.dto.question;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public final class CreateQuestionForm implements HasFieldAnswers {
	private final String q_class;
	private final String q_type;
	private final String q_question;
	private final String q_selectionA;
	private final String q_selectionB;
	private final String q_selectionC;
	private final String q_selectionD;
	private final String q_selectionE;
	private final String[] answers;
	private final MultipartFile multipartFilePic;
	private final MultipartFile multipartFileAudio;


	public CreateQuestionForm(String q_class, String q_type, String q_question, String q_selectionA, String q_selectionB, String q_selectionC, String q_selectionD, String q_selectionE, String[] answers, MultipartFile multipartFilePic, MultipartFile multipartFileAudio) {
		this.q_class = q_class;
		this.q_type = q_type;
		this.q_question = q_question;
		this.q_selectionA = q_selectionA;
		this.q_selectionB = q_selectionB;
		this.q_selectionC = q_selectionC;
		this.q_selectionD = q_selectionD;
		this.q_selectionE = q_selectionE;
		this.answers = answers;
		this.multipartFilePic = multipartFilePic;
		this.multipartFileAudio = multipartFileAudio;
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

	@Override
	public String[] getAnswers() {
		return answers;
	}

	public MultipartFile getMultipartFilePic() {
		return multipartFilePic;
	}

	public MultipartFile getMultipartFileAudio() {
		return multipartFileAudio;
	}


	@Override
	public String toString() {
		return "CreateQuestionForm{" +
			   "q_class='" + q_class + '\'' +
			   ", q_type='" + q_type + '\'' +
			   ", q_question='" + q_question + '\'' +
			   ", q_selectionA='" + q_selectionA + '\'' +
			   ", q_selectionB='" + q_selectionB + '\'' +
			   ", q_selectionC='" + q_selectionC + '\'' +
			   ", q_selectionD='" + q_selectionD + '\'' +
			   ", q_selectionE='" + q_selectionE + '\'' +
			   ", answers=" + Arrays.toString(answers) +
			   ", multipartFilePic=" + multipartFilePic +
			   ", multipartFileAudio=" + multipartFileAudio +
			   '}';
	}
}