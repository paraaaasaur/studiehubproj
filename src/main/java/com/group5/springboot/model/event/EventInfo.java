package com.group5.springboot.model.event;

import java.io.CharArrayWriter;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@JsonFormat(pattern = "yyyy/MM/dd/HH:mm",timezone="GMT+8" )
	Timestamp a_startTime;           // 活動開始時間
	@JsonFormat(pattern = "yyyy/MM/dd/HH:mm",timezone="GMT+8" )
	Timestamp a_endTime;             // 活動結束時間
	@JsonFormat(pattern = "yyyy/MM/dd/HH:mm",timezone="GMT+8" )
	Timestamp creationTime;          // 創建活動的時間戳
	@JsonFormat(pattern = "yyyy/MM/dd/HH:mm",timezone="GMT+8" )
	Timestamp a_registration_starttime; //活動報名開始時間
	@JsonFormat(pattern = "yyyy/MM/dd/HH:mm",timezone="GMT+8" )
	Timestamp a_registration_endrttime; //活動報名結束時間
	
	
	Clob comment;                    // 活動說明欄位  getcomment 已轉換成String 可以回傳給 json了~
	String verification ;
	@JsonIgnore
	@OneToMany(mappedBy = "eventInfo" , cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE} , fetch = FetchType.EAGER)
	private  List<Entryform>entryforms;
	int applicants ;                 //報名人數上限 
	int havesignedup ;               //報名表的人數
	String expired ;                 //活動過期
	String uidname ;

	
	
//===========暫時存放區===========	
	@Transient
	MultipartFile eventImage;        //儲存圖片暫時存放區 要轉換型態放進資料庫
	@Transient
	String transientcomment;         //上傳說明暫時存放區 要轉換型態放進資料庫
	@Transient
	String transienta_startTime;     //活動時間的暫時存放區 要跟存進資料庫的時間分開 不然搜尋全部會有問題(get方法重疊)
	@Transient
	String transienta_endTime;       //活動時間的暫時存放區 要跟存進資料庫的時間分開 不然搜尋全部會有問題(get方法重疊)
	@Transient
	String registration_starttime;   //報名活動時間的暫時存放區 要跟存進資料庫的時間分開 不然搜尋全部會有問題(get方法重疊)
	@Transient
	String registration_endrttime;   //報名活動時間的暫時存放區 要跟存進資料庫的時間分開 不然搜尋全部會有問題(get方法重疊)

	

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


//開始時間
	public Timestamp getA_startTime() {
		return a_startTime;
		
	}

	public void setA_startTime(Timestamp a_startTime) {
		this.a_startTime = a_startTime;
	}

	public Timestamp getA_endTime() {
		return a_endTime;
	}
//結束時間
	public void setA_endTime(Timestamp a_endTime) {
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
	
	
	
	
	
	
	//活動開始時間
	public String getTransienta_startTime() {
		
//		System.out.println("a_endTime="+a_startTime);
		if(a_startTime!=null) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//定义格式，不显示毫秒
		String new_a_endTime = df.format(a_startTime);
		new_a_endTime=new_a_endTime.replaceAll(" ", "T");//替换方法
//        System.out.println("轉換格式後 = " + new_a_endTime);
        return new_a_endTime;
		}else {
	        	return "" ;
	    }
		
	}
	//活動開始時間
	public void setTransienta_startTime(String transienta_startTime) {
		
		 SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		 transienta_startTime=transienta_startTime.replaceAll("T", " ");//替换方法
	   
			try {
	            java.util.Date date = format1.parse(transienta_startTime);
				Timestamp createTime = new Timestamp(date.getTime());
				this.a_startTime = createTime;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	//活動結束時間
	public String getTransienta_endTime() {
		
//		System.out.println("a_endTime="+a_endTime);
		if(a_endTime!=null) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//定义格式，不显示毫秒
		String new_a_endTime = df.format(a_endTime);
		new_a_endTime=new_a_endTime.replaceAll(" ", "T");//替换方法
//        System.out.println("轉換格式後 = " + new_a_endTime);
        return new_a_endTime;
		}else {
	        	return "" ;
	    }
		
	}
	//活動結束時間
	public void setTransienta_endTime(String transienta_endTime) {
		
		 SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		 transienta_endTime=transienta_endTime.replaceAll("T", " ");//替换方法
	   
			try {
	            java.util.Date date = format1.parse(transienta_endTime);
				Timestamp createTime = new Timestamp(date.getTime());
				this.a_endTime = createTime;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}
	
	

	public List<Entryform> getEntryforms() {
		return entryforms;
	}

	public void setEntryforms(List<Entryform> entryforms) {
		this.entryforms = entryforms;
	}

	public int getApplicants() {
		return applicants;
	}

	public void setApplicants(int applicants) {
		this.applicants = applicants;
	}

	public int getHavesignedup() {
		return havesignedup;
	}

	public void setHavesignedup(int havesignedup) {
		this.havesignedup = havesignedup;
	}

	
	
	
	
	////新增時間部分
	
	
	
	
	
	
	
	public Timestamp getA_registration_starttime() {
		return a_registration_starttime;
	}

	public void setA_registration_starttime(Timestamp a_registration_starttime) {
		this.a_registration_starttime = a_registration_starttime;
	}
	
	

	public Timestamp getA_registration_endrttime() {
		return a_registration_endrttime;
	}

	public void setA_registration_endrttime(Timestamp a_registration_endrttime) {
		this.a_registration_endrttime = a_registration_endrttime;
	}
	
	
	
	
	
	
	

	public String getRegistration_starttime() {
		
//		System.out.println("a_endTime="+a_startTime);
		if(a_registration_starttime!=null) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//定义格式，不显示毫秒
		String new_a_registration_starttime = df.format(a_registration_starttime);
		new_a_registration_starttime=new_a_registration_starttime.replaceAll(" ", "T");//替换方法
//        System.out.println("轉換格式後 = " + new_a_endTime);
        return new_a_registration_starttime;
		}else {
	        	return "" ;
	    }	
	}

	public void setRegistration_starttime(String registration_starttime) {

		 SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		 registration_starttime=registration_starttime.replaceAll("T", " ");//替换方法
	   
			try {
	            java.util.Date date = format1.parse(registration_starttime);
				Timestamp createTime = new Timestamp(date.getTime());
				this.a_registration_starttime = createTime;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
	}

	public String getRegistration_endrttime() {
//		System.out.println("a_endTime="+a_startTime);
		if(a_registration_endrttime!=null) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//定义格式，不显示毫秒
		String new_a_registration_endrttime = df.format(a_registration_endrttime);
		new_a_registration_endrttime=new_a_registration_endrttime.replaceAll(" ", "T");//替换方法
//        System.out.println("轉換格式後 = " + new_a_endTime);
        return new_a_registration_endrttime;
		}else {
	        	return "" ;
	    }
	}

	public void setRegistration_endrttime(String registration_endrttime) {

		 SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		 registration_endrttime=registration_endrttime.replaceAll("T", " ");//替换方法
	   
			try {
	            java.util.Date date = format1.parse(registration_endrttime);
				Timestamp createTime = new Timestamp(date.getTime());
				this.a_registration_endrttime = createTime;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	public String getUidname() {
		return uidname;
	}

	public void setUidname(String uidname) {
		this.uidname = uidname;
	}


	
	


	
	






	
}
