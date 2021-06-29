package com.group5.springboot.model.event;

import java.io.CharArrayWriter;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.group5.springboot.utils.SystemUtils;

@Entity
@Table(name = "EventInfo")
@Component
public class EventInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long a_aid;                      // 主鍵
	String a_uid;                    // 外來鍵
	String a_name;                   // 活動名稱
	String a_address;                // 活動地址
	String a_type;                   // 活動類型
	String a_picturepath;            // 圖片路徑
	Date a_startTime;                // 活動開始時間
	Date a_endTime;                  // 活動結束時間
	Timestamp creationTime;          // 創建活動的時間戳
	Clob comment;                    // 活動說明欄位  getcomment 已轉換成String 可以回傳給 json了~
	
	
	
//===========暫時存放區===========	
	@Transient
	MultipartFile eventImage;        //儲存圖片暫時存放區 要轉換型態放進資料庫
	@Transient
	String transientcomment;         //上傳說明暫時存放區 要轉換型態放進資料庫
	 
	
	

	public EventInfo() {
		super();
//		System.out.println("是用建構方法===========");
	}

	public Long getA_aid() {
		return a_aid;
	}

	public void setA_aid(Long a_aid) {
		this.a_aid = a_aid;
	}

	public String getA_uid() {
		return a_uid;
	}

	public void setA_uid(String a_uid) {
		this.a_uid = a_uid;
	}

	public String getA_name() {
//		System.out.println("是用GET方法===========");
		return a_name;
	}

	public void setA_name(String a_name) {
//		System.out.println("是用SET方法===========");
		this.a_name = a_name;
	}

	public String getA_address() {
		return a_address;
	}

	public void setA_address(String a_address) {
		this.a_address = a_address;
	}

	public String getA_type() {
		return a_type;
	}

	public void setA_type(String a_type) {
		this.a_type = a_type;
	}

	public Date getA_startTime() {
		return a_startTime;
	}

	public void setA_startTime(Date a_startTime) {
		this.a_startTime = a_startTime;
	}

	public Date getA_endTime() {
		return a_endTime;
	}

	public void setA_endTime(Date a_endTime) {
		this.a_endTime = a_endTime;
	}
	

	public String getA_picturepath() {
		return a_picturepath;
	}

	public void setA_picturepath(String a_picturepath) {
		this.a_picturepath = a_picturepath;
	}

	public MultipartFile getEventImage() {
		return eventImage;
	}

	public void setEventImage(MultipartFile eventImage) {
		this.eventImage = eventImage;
	}
	

	public String getComment() {
		//新增縫縫表單的時候 他會先來getComment  他無法識別clob型態 所以要轉成String 讓縫縫表單可以識別
		//再送JSON的時候也會來getComment  所以轉成String  
		String resultr="";
		try {
		Reader readet=comment.getCharacterStream();
		CharArrayWriter caw = new CharArrayWriter();
		int len = 0;
		char[] c = new char[8192];
		while ((len = readet.read(c)) != -1) {
			caw.write(c, 0, len); 
		}		
		resultr = new String(caw.toCharArray());
		}catch(Exception e) {
			
			e.printStackTrace();
		};
		return resultr;
	}

	public void setComment(String comment) {
		
		Clob Clobcomment = SystemUtils.stringToClob(comment);
		//修改縫縫表單 送出的時候走set  然後縫縫表單上的欄位是String 所以要轉型成clob型態 才能送出修改縫縫表單
		this.comment = Clobcomment;
	}

	public String getTransientcomment() {
		return transientcomment;
	}

	public void setTransientcomment(String transientcomment) {
		this.transientcomment = transientcomment;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}





	
}
