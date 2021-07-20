package com.group5.springboot.controller.event;

import java.io.File;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.event.Sendmessage;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.event.EventServiceImpl;
import com.group5.springboot.utils.SystemUtils;
import com.group5.springboot.validate.EventValidator;

@Controller
public class EventController {

	@Autowired
	EventServiceImpl EventService;

	@Autowired
	ServletContext context;
	
	@Autowired
	EventValidator eventValidator;

	// 從網頁首頁跳到老師的首頁
	@GetMapping("/NewFile")
	public String NewFile() {
		return "event/NewFile";
	}
	// 從網頁首頁跳到老師的首頁
	@GetMapping("/Eventindex")
	public String Eventindex() {
		return "event/index";
	}

	// 從老師網頁跳到新增活動頁面
	@GetMapping("/insertEvent")
	public String insertEvent(Model model) {
		// 縫縫表單已有 EventInfo物件
		return "event/insertEvent";
	}

	@GetMapping("/success")
	public String querysuccess() {
		return "event/success";
	}

	// 個人搜尋全部活動
	@GetMapping("/userAllEvent")
	public String userAllEvent() {
		return "event/userAllEvent";
	}
	// 管理者搜尋全部活動
		@GetMapping("/adminAllEvent")
		public String queryRestaurant() {
			return "event/adminAllEvent";
		}
	// 活動首頁
	@GetMapping("/eventindex")
	public String eventindex() {
		return "event/eventindex";
	}
	//搜尋全部需要驗證的畫面
	@GetMapping("/managerAllEvent")
	public String managerAllEvent() {
		return "event/managerAllEvent";
	}
	

	// 新增表單送出
	@PostMapping("/insertEvent")
	public String insertSaveEvent(@ModelAttribute("EventInfo") EventInfo eventinfo,
			                      BindingResult result,
			                      @SessionAttribute(value = "loginBean")  User_Info user_info,
			                      Model m,
			                      RedirectAttributes ra) {
		
		eventValidator.validate(eventinfo, result);
		//6/23上午10講解
        //表單送出的資料型態 一定要符合資料庫物件的型態 
		if (result.hasErrors()) {

			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {

				System.out.println("有錯誤" + error);
			}
          //如果有錯誤 就把錯誤訊息在帶一次新增送出的表單 新增表單裡有  <form:errors path='name' cssClass="error"/> 讀取錯誤訊息
			return "event/insertEvent";

		}

		EventService.saveEvent(eventinfo);
		// 先儲存取得主鍵
		String Transientcomment = eventinfo.getTransientcomment();
		// 抓取放在縫縫表單的Transientcomment欄位
		eventinfo.setComment(Transientcomment);
		// 把縫縫表單Transientcomment欄位放的字串 放到clob裡面
		// 送到setComment 方法裡面已經將字串轉成clob型態了
		eventinfo.setCreationTime(new Timestamp(System.currentTimeMillis()));
		// 放進時間戳記
        
		String name = "";
		String mimeType = "";
		try {
			MultipartFile eventinfoImage = eventinfo.getEventImage();
			name = eventinfoImage.getOriginalFilename();
			// 取得檔名
			mimeType = context.getMimeType(name);
			// 取得 mimeType 怕有人傳沒副檔名的資料
			String ext = SystemUtils.getExtFilename(name);
			// 取得副檔名
			String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
			// 得到classes/static地址
			String savePath = path + File.separator + "eventimages";
			// 儲存路徑classes/static/images
			String url_path = File.separator + "eventimages" + File.separator;
			// 讀取檔案路徑 /images/eventimages 為了放進資料庫
			String stamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			// 路徑要加上時間戳記 避免重複名稱修改問題
			File imageFoldet = new File(savePath);
			// 儲存資料夾的位置 File型態
			if (!imageFoldet.exists()) {
				// 看看有沒有這個儲存資料夾的位置 沒有就建立新的
				imageFoldet.mkdirs();
			}
			System.out.println("eventinfoImage=" + eventinfoImage);
			System.out.println("size=" + eventinfoImage.getSize());
			System.out.println("mimeType=" + mimeType);
			System.out.println("ext=" + ext);

			if (eventinfoImage != null && eventinfoImage.getSize() > 0 && ext != null) {
				// 如果有傳圖就執行放入圖片的步驟 
				// ext != null 怕上傳的檔案沒有副檔名
				File file = new File(imageFoldet, "MemberImage_" + eventinfo.getA_aid() + stamp + ext);
				// 儲存資料夾的位置 跟 儲存的文件名稱
				eventinfo.setA_picturepath(url_path + "MemberImage_" + eventinfo.getA_aid() + stamp + ext);
				// /eventimages/MemberImage_1 + 時間戳記.jpg
				// 放進資料庫的路徑
				eventinfoImage.transferTo(file);
				// 放進資料夾
			} else {
				eventinfo.setA_picturepath(url_path + "MemberImagexx.png");
				// 如果沒有傳圖就用預設
			}
			
//			User_Info user_info = (User_Info) m.getAttribute("loginBean") ;
			eventinfo.setUidname(user_info.getU_lastname()+user_info.getU_firstname());
//			eventinfo.setA_uid(user_info.getU_id());
			eventinfo.setExpired("未過期");
			//新增時加入未過期  在 admin管理者顯示回傳所有活動表前端時 去判斷有無過期  
			eventinfo.setVerification("N");
			//新增時加入N  讓  admin管理者 驗證時將N改成Y 顯示在使用者活動區 
			eventinfo.setA_uid(user_info.getU_id());
			System.out.println("user_info.getU_id()======"+user_info.getU_id());
			EventService.saveEvent(eventinfo);
			
			

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳發生異常:" + e.getMessage());
		}
		ra.addFlashAttribute("successMessage", eventinfo.getA_name() + "新增成功");
		// 重定向用的 addAttribute 其原理是放到session中 session再跳到其他頁面時馬上被移除
		
		
		return "redirect:/userAllEvent";
	}

	// 修改表單
	// 搜尋全部網頁 把a_aid帶到網頁參數上 再用每一筆的a_aid 拉出資料 放在Model裡 讓縫縫表單讀值
	@GetMapping("/updateEvent/{a_aid}")
	public String SendEditPage(@PathVariable Long a_aid, Model model) {
		EventInfo eventinfo = EventService.findByid(a_aid);
		model.addAttribute("EventInfo", eventinfo);
		return "event/editEvent";
	}

	// 修改表單送出
	@PostMapping("/updateEvent/{a_aid}")
	public String updateSaveEvent(@ModelAttribute("EventInfo") EventInfo eventinfo,BindingResult result, RedirectAttributes ra,  @SessionAttribute(value = "loginBean")  User_Info user_info) {

//		eventValidator.validate(eventinfo, result);
//
//		if (result.hasErrors()) {
//
//			List<ObjectError> list = result.getAllErrors();
//			for (ObjectError error : list) {
//
//				System.out.println("有錯誤" + error);
//			}
//          //如果有錯誤 就把錯誤訊息在帶一次新增送出的表單 新增表單裡有  <form:errors path='name' cssClass="error"/> 讀取錯誤訊息
//			return "event/editEvent";
//
//		}
		
		
		
		
//			String comment=eventinfo.getComment();
//			Clob Clobcomment = SystemUtils.stringToClob(comment);
//			eventinfo.setComment(Clobcomment);
//===========縫縫表單送出setComment 已經將 字串 轉成clob了 所以不用上面的方法了===========
		eventinfo.setCreationTime(new Timestamp(System.currentTimeMillis()));
		// 放進時間戳記

		String name = "";
		String mimeType = "";
		try {
			MultipartFile eventinfoImage = eventinfo.getEventImage();
			name = eventinfoImage.getOriginalFilename();
			// 取得檔名
			mimeType = context.getMimeType(name);
			// 取得 mimeType 怕有人傳沒副檔名的資料
			String ext = SystemUtils.getExtFilename(name);
			// 取得副檔名
			String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
			// 得到classes/static地址
			String savePath = path + File.separator + "eventimages";
			// 儲存路徑classes/static/eventimages(資料夾)
			String url_path = File.separator + "eventimages" + File.separator;
			// 讀取檔案路徑 /images/ 為了放進資料庫
			String stamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			// 路徑要加上時間戳記 避免重複名稱修改問題
			File imageFoldet = new File(savePath);
			// 儲存資料夾的位置 File型態
			System.out.println("eventinfoImage=" + eventinfoImage);
			System.out.println("size=" + eventinfoImage.getSize());
			System.out.println("mimeType=" + mimeType);

			if (!imageFoldet.exists()) {
				// 看看有沒有這個儲存資料夾的位置 沒有就建立新的
				imageFoldet.mkdirs();
			}

			if (eventinfoImage != null && eventinfoImage.getSize() > 0 && ext != null) {
				// 如果有傳圖就執行放入圖片的步驟
				// ext != null 怕上傳的檔案沒有副檔名
				File file = new File(imageFoldet, "MemberImage_" + eventinfo.getA_aid() + stamp + ext);
				// 儲存資料夾的位置 跟 儲存的文件名稱
				eventinfo.setA_picturepath(url_path + "MemberImage_" + eventinfo.getA_aid() + stamp + ext);
				// /eventimages/MemberImage_1 + 時間戳記.jpg
				// 放進資料庫的路徑
				eventinfoImage.transferTo(file);
				// 放進資料夾
			}
			eventinfo.setUidname(user_info.getU_lastname()+user_info.getU_firstname());
			eventinfo.setExpired("未過期");
			//新增時加入未過期  在 admin管理者顯示回傳所有活動表前端時 去判斷有無過期  
			eventinfo.setVerification("N");
			//新增時加入N  讓  admin管理者 驗證時將N改成Y 顯示在使用者活動區 
			EventService.update(eventinfo);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳發生異常:" + e.getMessage());
		}
		ra.addFlashAttribute("successMessage", eventinfo.getA_name() + "修改成功");
		// 重定向用的 addAttribute 其原理是放到session中 session再跳到其他頁面時馬上被移除
		return "redirect:/userAllEvent";
	}
	//使用者刪除
	@GetMapping("/deleteEvent/{a_aid}")
	public String deleteEditPage(@PathVariable Long a_aid, Model model,RedirectAttributes ra) {
		EventInfo eventinfo = EventService.findByid(a_aid);
		EventService.deletdate(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "下架成功");
		return "redirect:/userAllEvent";	
		
		}
	//管理者刪除
		@GetMapping("/deleteadminEvent/{a_aid}")
		public String deleteadminEvent(@PathVariable Long a_aid, Model model,RedirectAttributes ra) {
			EventInfo eventinfo = EventService.findByid(a_aid);
			EventService.deletdate(eventinfo);
			ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "下架成功");
			return "redirect:/adminAllEvent";	
			
			}
	
	
	
	//顯示活動內容 把EventInfo物件帶到AJAX使用
	@GetMapping("/Selecteventcontent/{a_aid}")
	public String Selecteventcontent(@PathVariable Long a_aid,Model model) {
		
		EventInfo eventcontent = EventService.findByid(a_aid);
		model.addAttribute("eventcontent", eventcontent);
		
		return "event/eventcontent";	
		}
	
	//改變驗證
	@GetMapping("/verification/{a_aid}")
	public String verification(@PathVariable Long a_aid, Model model,RedirectAttributes ra) {
		EventInfo eventinfo = EventService.findByid(a_aid);
		eventinfo.setVerification("Y");
		EventService.update(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "發布成功");
		return "redirect:/managerAllEvent";	
		}
	//驗證失敗後刪除 
		@GetMapping("/deleteverification/{a_aid}")
		public String deleteverification(@PathVariable Long a_aid, Model model,RedirectAttributes ra) {
			EventInfo eventinfo = EventService.findByid(a_aid);
			EventService.deletdate(eventinfo);
			ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "已被駁回");
			return "redirect:/managerAllEvent";	
			}
		
	
	
    //當活動內容頁按下我要報名後執行
	@GetMapping("/signupclick/{a_aid}")
	public @ResponseBody  Map<String , String> signupclick(@PathVariable Long a_aid, Model model,
			                  @SessionAttribute(value = "loginBean")  User_Info user_info) {
		
		Map<String , String> map = new HashMap<>();
		 
		EventInfo eventInfo = EventService.findByid(a_aid);
		//搜尋此筆 EventInfo 
		

		boolean isEntryformExist = EventService.isEntryformExist(eventInfo, user_info);
		System.out.println("判斷有沒有重複================="+isEntryformExist);
		//檢查要素 當下按出報名的那一個AID搜尋出來的 活動表 + 放在Session裡的user_info裡的UID 去搜尋有無重複
		//有重複回傳true  
		//沒有重複回傳false
		System.out.println("截止日期時間"+eventInfo.getA_endTime().getTime());
		System.out.println("現在的時間間"+new Date().getTime());
		
		
		
		if(!isEntryformExist) {
			
			if(eventInfo.getA_registration_endrttime().getTime() <= new Date().getTime()) {
				//A_endTime() 截止時間  new Date() = 現在時間
				map.put("Time", "這個活動報名時間結束了,報名失敗");
				return  map ;
			}
				
				
			System.out.println("報名的人數"+eventInfo.getApplicants());
			System.out.println("人數上限"+eventInfo.getHavesignedup());

			if(!(eventInfo.getHavesignedup()>=eventInfo.getApplicants())) {
				  //  applicants=報名人數上限   havesignedup=報名的人數 
				  //  如果報名人數 大於等於 報名人數上限 就不執行 
				
		    //放進用AID搜尋過的EventInfo 跟放在 @SessionAttribute 裡的 User_Info  去關聯儲存 關聯報名表
		    EventService.saveEntryform(eventInfo, user_info) ;
		    //用AID搜尋到的活動表 找出相關聯的 報名表 用.size 取得 跟 活動表相關聯的數量 代表有幾個報名人數
		    int size=EventService.findentryformByaidreturnsize(eventInfo);
		    //把搜尋到的數量放進活動表
		    eventInfo.setHavesignedup(size);
		    //將活動表儲存 回傳頁面的時候才有報名人數
		    EventService.saveEvent(eventInfo);
			}else {
				map.put("Exceed", "這個活動報名已經額滿,報名失敗");
				return  map ;
			}
		
		map.put("succes", "報名成功");
		}else {
		map.put("fail", "你已經報名過了喔~");	
		}
         

		return map;
		}
	
	
	
	//查詢報名表
	@GetMapping("/signupEvent/{a_aid}")
	public String signupEvent(@PathVariable Long a_aid, Model model) {
		
		EventInfo Event = EventService.findByid(a_aid);
        //裝有此Event裡的Entryform
//		List<Entryform> aaa = EventService.findentryformByaid(Event);
//		
//		for (Entryform p : aaa) {
//			System.out.println(JSON.toJSONString(aaa, true));}
		
		model.addAttribute("signupEvent",Event);
		
		return "event/signupEvent";	
	}
	//執行報名表單刪除 刪除完 再把值放到model 讓signupEvent.jsp 讀值
	@GetMapping("/deletesignupEvent/{e_id}/{a_id}")
	public String signupEvent(@PathVariable Long e_id,
			                  @PathVariable Long a_id,
			                  Model model) {

		EventInfo Event = EventService.findByid(a_id);
		EventService.deleteEntryformByid(e_id);
		
		
//
	//用AID搜尋到的活動表 找出相關聯的 報名表 用.size 取得 跟 活動表相關聯的數量 代表有幾個報名人數
	int size=EventService.findentryformByaidreturnsize(Event);
	//把搜尋到的數量放進活動表
	Event.setHavesignedup(size);
    EventService.saveEvent(Event);
	
		
		model.addAttribute("signupEvent",Event);
		
		return "event/signupEvent";
		
	}
//==================7/18
	
	
	
	
	

//====================以下是@ModelAttribute()===========================	
	@ModelAttribute("EventInfo")
	public EventInfo getPlace(@RequestParam(value = "a_aid", required = false) Long a_aid) {

		EventInfo eventinfo = null;
		// 好像沒用到
		if (a_aid != null) {
			eventinfo = EventService.findByid(a_aid);
		} else {
			eventinfo = new EventInfo();
		}

		return eventinfo;

	}


	@ModelAttribute("eventtype")
    public Map<String, String>  eventtype(){
		Map<String, String> map = new HashMap<>();
		
		map.put("研討會", "研討會");
		map.put("線下課程", "線下課程");
		map.put("講座", "講座");
		map.put("分享會", "分享會");
		return map;
    }	
	
	
	@ModelAttribute("Sendmessage")
	public Sendmessage getSendmessage(@RequestParam(value = "a_aid", required = false) Long a_aid) {

		
		   
		
			Sendmessage Sendmessage = new Sendmessage();
		

		return Sendmessage;

	}

	

}
