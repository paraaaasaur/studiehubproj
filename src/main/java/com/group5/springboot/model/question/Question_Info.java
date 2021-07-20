package com.group5.springboot.model.question;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group5.springboot.utils.SystemUtilsNickUse;

	@Entity
	@Table(name="Question_Info")
	@Component
	public class Question_Info {

		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Long q_id;
		private String q_class;
	    private String q_type;
	    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
	    private String q_question;
	    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
	    private String q_selectionA;
	    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
	    private String q_selectionB;
	    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
	    private String q_selectionC;
	    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
	    private String q_selectionD;
	    @Column(columnDefinition = "NVARCHAR(255)", nullable = true)
	    private String q_selectionE;
	    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
	    private String q_answer;
	    
	    private String verification ; //預設為N，驗證後為Y

	    
	    @Transient
	    private String[] answers; //暫存多選答案陣列
	    
	    private String mimeTypePic;
	    private String mimeTypeAudio;
	    
		@JsonFormat(pattern = "yyyy-MM-dd_HH:mm:ss", timezone = "GMT+8")
	    private Timestamp createDate;  
	    
	    
	    @JsonIgnore      //前端送的東西不能直接塞在這裏面
		Blob  q_picture;
	    
	    @JsonIgnore      //前端送的東西不能直接塞在這裏面
		Blob  q_audio;

		@Transient  	 // 短暫. 臨時  <=>  Persistence: 永續儲存
		String  q_pictureString;    // data:image/gif;base64,.....
		
		@Transient  	 // 短暫. 臨時  <=>  Persistence: 永續儲存
		String  q_audioString;    // data:image/gif;base64,.....
		
		@Transient					   //省略不放到表格內
		MultipartFile multipartFilePic;   //使用者檔案上傳後,後端若沒使用MultipartFile物件去接檔案物件的話會報錯. (沒寫的話會直接丟到上面的picture的表單)
		
		@Transient					   //省略不放到表格內
		MultipartFile multipartFileAudio;   //使用者檔案上傳後,後端若沒使用MultipartFile物件去接檔案物件的話會報錯. (沒寫的話會直接丟到上面的picture的表單)
		
		
		//預設無參數建構子
		public Question_Info() {
		}

		public Question_Info(String q_class, String q_type, String q_question, String q_selectionA,
				String q_selectionB, String q_selectionC, String q_selectionD, String q_selectionE, String q_answer,String verification, String mimeTypePic,String mimeTypeAudio,
				Blob q_pictureBlob, Blob q_audioBlob, MultipartFile multipartFilePic, MultipartFile multipartFileAudio,Timestamp createDate,String[] answers) {
			super();
			this.q_class = q_class;
			this.q_type = q_type;
			this.q_question = q_question;
			this.q_selectionA = q_selectionA;
			this.q_selectionB = q_selectionB;
			this.q_selectionC = q_selectionC;
			this.q_selectionD = q_selectionD;
			this.q_selectionE = q_selectionE;
			this.q_answer = q_answer;
			this.verification = verification;
			this.mimeTypePic = mimeTypePic;
			this.mimeTypeAudio = mimeTypeAudio;
			this.q_picture = q_pictureBlob;
			this.q_audio = q_audioBlob;
			this.multipartFilePic = multipartFilePic;
			this.multipartFileAudio = multipartFileAudio;
			this.createDate = createDate;
			this.answers = answers;
		}

		public Long getQ_id() {
			return q_id;
		}

		public void setQ_id(Long q_id) {
			this.q_id = q_id;
		}

		public String getQ_class() {
			return q_class;
		}

		public void setQ_class(String q_class) {
			this.q_class = q_class;
		}

		public String getQ_type() {
			return q_type;
		}

		public void setQ_type(String q_type) {
			this.q_type = q_type;
		}

		public String getQ_question() {
			return q_question;
		}

		public void setQ_question(String q_question) {
			this.q_question = q_question;
		}

		public String getQ_selectionA() {
			return q_selectionA;
		}

		public void setQ_selectionA(String q_selectionA) {
			this.q_selectionA = q_selectionA;
		}

		public String getQ_selectionB() {
			return q_selectionB;
		}

		public void setQ_selectionB(String q_selectionB) {
			this.q_selectionB = q_selectionB;
		}

		public String getQ_selectionC() {
			return q_selectionC;
		}

		public void setQ_selectionC(String q_selectionC) {
			this.q_selectionC = q_selectionC;
		}

		public String getQ_selectionD() {
			return q_selectionD;
		}

		public void setQ_selectionD(String q_selectionD) {
			this.q_selectionD = q_selectionD;
		}

		public String getQ_selectionE() {
			return q_selectionE;
		}

		public void setQ_selectionE(String q_selectionE) {
			this.q_selectionE = q_selectionE;
		}

		public String getQ_answer() {
			return q_answer;
		}

		public void setQ_answer(String q_answer) {
			this.q_answer = q_answer;
		}
		
		public String getVerification() {
			return verification;
		}

		public void setVerification(String verification) {
			this.verification = verification;
		}

		public String getMimeTypePic() {
			return mimeTypePic;
		}

		public void setMimeTypePic(String mimeTypePic) {
			this.mimeTypePic = mimeTypePic;
		}

		public String getMimeTypeAudio() {
			return mimeTypeAudio;
		}

		public void setMimeTypeAudio(String mimeTypeAudio) {
			this.mimeTypeAudio = mimeTypeAudio;
		}

		public Blob getQ_picture() {
			return q_picture;
		}

		public void setQ_picture(Blob q_picture) {
			this.q_picture = q_picture;
		}

		public Blob getQ_audio() {
			return q_audio;
		}

		public void setQ_audio(Blob q_audio) {
			this.q_audio = q_audio;
		}

		public String getQ_pictureString() { 
			return SystemUtilsNickUse.blobToDataProtocol(mimeTypePic, q_picture);
		}
		public void setQ_pictureString(String q_pictureString) {
			this.q_pictureString = q_pictureString;
		}

		public String getQ_audioString() {
			return SystemUtilsNickUse.blobToDataProtocol(mimeTypeAudio,q_audio);
		}
		public void setQ_audioString(String q_audioString) {
			this.q_audioString = q_audioString;
		}

		

		public MultipartFile getMultipartFilePic() {
			return multipartFilePic;
		}
		public void setMultipartFilePic(MultipartFile multipartFilePic) {
			this.multipartFilePic = multipartFilePic;
		}

		public MultipartFile getMultipartFileAudio() {
			return multipartFileAudio;
		}
		public void setMultipartFileAudio(MultipartFile multipartFileAudio) {
			this.multipartFileAudio = multipartFileAudio;
		}
		
		

		public Timestamp getCreateDate() {
			return createDate;
		}
		public void setCreateDate(Timestamp createDate) {
			this.createDate = createDate;
		}
		

		public String[] getAnswers() {
			return answers;
		}

		public void setAnswers(String[] answers) {
			this.answers = answers;
		}
		
		//複選答案陣列轉字串
		@Override
		public String toString() {
			return  Arrays.toString(answers) ;
		}


		
		
	}