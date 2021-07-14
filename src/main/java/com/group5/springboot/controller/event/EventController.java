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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.event.EventServiceImpl;
import com.group5.springboot.utils.SystemUtils;

@Controller
public class EventController {

	@Autowired
	EventServiceImpl EventService;

	@Autowired
	ServletContext context;

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
			                      @SessionAttribute(value = "loginBean")  User_Info user_info,
			                      Model m,
			                      RedirectAttributes ra) {

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
//			
//			eventinfo.setA_uid(user_info.getU_id());
			
			eventinfo.setVerification("N");
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
	public String updateSaveEvent(@ModelAttribute("EventInfo") EventInfo eventinfo, RedirectAttributes ra) {

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
			eventinfo.setVerification("N");
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
		model.addAttribute("successMessage",eventinfo.getA_name() + "刪除成功");
		return "redirect:/userAllEvent";	
		
		}
	//管理者刪除
		@GetMapping("/deleteadminEvent/{a_aid}")
		public String deleteadminEvent(@PathVariable Long a_aid, Model model,RedirectAttributes ra) {
			EventInfo eventinfo = EventService.findByid(a_aid);
			EventService.deletdate(eventinfo);
			model.addAttribute("successMessage",eventinfo.getA_name() + "刪除成功");
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
		model.addAttribute("successMessage",eventinfo.getA_name() + "驗證成功");
		return "redirect:/managerAllEvent";	
		}
	
	
    //當活動內容頁按下我要報名後執行
	@GetMapping("/signupclick/{a_aid}")
	public String signupclick(@PathVariable Long a_aid, Model model,
			                  @SessionAttribute(value = "loginBean")  User_Info user_info) {

		EventInfo eventcontent = EventService.findByid(a_aid);
		//搜尋此筆 EventInfo 
		Entryform Entryform = new Entryform();
		//打開報名表 
		Entryform.setEventInfo(eventcontent);
		Entryform.setE_id(user_info.getU_id());
		Entryform.setE_lastname(user_info.getU_lastname());
		Entryform.setE_firstname(user_info.getU_firstname());
		Entryform.setE_tel(user_info.getU_tel());
		Entryform.setE_email(user_info.getU_email());
		
		EventService.saveEntryform(Entryform);

		return "event/eventcontent";
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
		model.addAttribute("signupEvent",Event);
		return "event/signupEvent";
		
	}
	
	
	

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
		map.put("線上活動", "線上活動");
		map.put("研討會", "研討會");
		map.put("課程", "課程");
		map.put("講座", "講座");
		map.put("分享會", "分享會");
		return map;
    }	

}
