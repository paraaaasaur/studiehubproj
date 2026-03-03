package com.group5.springboot.controller.event;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.config.StorageConfigProperties;
import com.group5.springboot.utils.ResourceLocationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.event.EventServiceImpl;
import com.group5.springboot.validate.EventValidator;

@Controller
public class EventController {
	@Autowired EventServiceImpl EventService;
	@Autowired EventValidator eventValidator;

	private final String IMAGE_STORAGE_DIR;
	private final String IMAGE_URL_BASE;


	@Autowired
	public EventController(StorageConfigProperties props) {
		IMAGE_STORAGE_DIR = props.getEventImageUploadStorageDir();
		IMAGE_URL_BASE = StorageConfigProperties.storagePathToViewAndDbUrl(IMAGE_STORAGE_DIR);
	}


	@RequiresUser
	@GetMapping("/insertEvent")
	public String insertEvent() {
		return "event/insertEvent";
	}

	@RequiresUser
	@GetMapping("/userAllEvent")
	public String userAllEvent() {
		return "event/userAllEvent";
	}

	@RequiresAdmin
	@GetMapping("/adminAllEvent")
	public String queryRestaurant() {
		return "event/adminAllEvent";
	}

	@GetMapping("/eventindex")
	public String eventindex() {
		return "event/eventindex";
	}

	@RequiresAdmin
	@GetMapping("/managerAllEvent")
	public String managerAllEvent() {
		return "event/managerAllEvent";
	}

	@RequiresUser
	@PostMapping("/insertEvent")
	public String insertSaveEvent(
			@ModelAttribute("EventInfo") EventInfo eventinfo,
			BindingResult result,
			@SessionAttribute(value = "loginBean") User_Info user_info,
			RedirectAttributes ra
	) {
		eventValidator.validate(eventinfo, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤" + error);
			}
			return "event/insertEvent";
		}

		EventService.saveEvent(eventinfo);
		String Transientcomment = eventinfo.getTransientcomment();
		eventinfo.setComment(Transientcomment);
		eventinfo.setCreationTime(new Timestamp(System.currentTimeMillis()));

		String name = "";
		try {
			MultipartFile eventinfoImage = eventinfo.getEventImage();
			name = eventinfoImage.getOriginalFilename();
			String ext = StringUtils.getFilenameExtension(name);
			File imageFoldet = new File(IMAGE_STORAGE_DIR);
			if (!imageFoldet.exists()) {
				imageFoldet.mkdirs();
			}

			if (eventinfoImage != null && eventinfoImage.getSize() > 0 && ext != null) {
				// 路徑要加上時間戳記 避免重複名稱修改問題
				String stamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				String imageFilename = "MemberImage_" + eventinfo.getA_aid() + stamp + "." + ext;
				File file = new File(imageFoldet, imageFilename);
				eventinfo.setA_picturepath(IMAGE_URL_BASE + "/" + imageFilename);
				eventinfoImage.transferTo(file);
			} else {
				eventinfo.setA_picturepath(ResourceLocationResolver.EVENT_NO_IMAGE_URL);
			}
			
			eventinfo.setUidname(user_info.getU_lastname()+user_info.getU_firstname());
			eventinfo.setExpired("未過期");
			eventinfo.setVerification("N");
			eventinfo.setA_uid(user_info.getU_id());
			EventService.saveEvent(eventinfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳發生異常:" + e.getMessage());
		}

		ra.addFlashAttribute("successMessage", eventinfo.getA_name() + "新增成功");

		return "redirect:/userAllEvent";
	}

	@RequiresUser
	@GetMapping("/updateEvent/{a_aid}")
	public String SendEditPage(@PathVariable Long a_aid, Model model) {
		EventInfo eventinfo = EventService.findByid(a_aid);
		model.addAttribute("EventInfo", eventinfo);
		return "event/editEvent";
	}

	@RequiresUser
	@PostMapping("/updateEvent/{a_aid}")
	public String updateSaveEvent(@ModelAttribute("EventInfo") EventInfo eventinfo,BindingResult result, RedirectAttributes ra,  @SessionAttribute(value = "loginBean")  User_Info user_info) {
		eventValidator.validate(eventinfo, result);
		if (result.hasErrors()) {
			result.getAllErrors().forEach(System.err::println);
			return "event/editEvent";
		}

		eventinfo.setCreationTime(new Timestamp(System.currentTimeMillis()));

		String name = "";
		try {
			MultipartFile eventinfoImage = eventinfo.getEventImage();
			name = eventinfoImage.getOriginalFilename();
			String ext = StringUtils.getFilenameExtension(name);
			File imageFoldet = new File(IMAGE_STORAGE_DIR);

			if (!imageFoldet.exists()) {
				imageFoldet.mkdirs();
			}

			if (eventinfoImage != null && eventinfoImage.getSize() > 0 && ext != null) {
				// 路徑要加上時間戳記 避免重複名稱修改問題
				String stamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				final String imageFilename = "MemberImage_" + eventinfo.getA_aid() + stamp + "." + ext;
				File file = new File(imageFoldet, imageFilename);
				eventinfo.setA_picturepath(IMAGE_URL_BASE + "/" + imageFilename);
				eventinfoImage.transferTo(file);
			}

			eventinfo.setUidname(user_info.getU_lastname()+user_info.getU_firstname());
			eventinfo.setExpired("未過期");
			eventinfo.setVerification("N");
			EventService.update(eventinfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳發生異常:" + e.getMessage());
		}

		ra.addFlashAttribute("successMessage", eventinfo.getA_name() + "修改成功");

		return "redirect:/userAllEvent";
	}

	@RequiresUser
	@GetMapping("/deleteEvent/{a_aid}")
	public String deleteEditPage(@PathVariable Long a_aid, RedirectAttributes ra) {
		EventInfo eventinfo = EventService.findByid(a_aid);
		EventService.deletdate(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "下架成功");
		return "redirect:/userAllEvent";
	}

	@RequiresAdmin
	@GetMapping("/deleteadminEvent/{a_aid}")
	public String deleteadminEvent(@PathVariable Long a_aid, RedirectAttributes ra) {
		EventInfo eventinfo = EventService.findByid(a_aid);
		EventService.deletdate(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "下架成功");
		return "redirect:/adminAllEvent";
	}

	@GetMapping("/Selecteventcontent/{a_aid}")
	public String Selecteventcontent(@PathVariable Long a_aid,Model model) {
		EventInfo eventcontent = EventService.findByid(a_aid);
		model.addAttribute("eventcontent", eventcontent);

		return "event/eventcontent";	
	}
	
	@RequiresAdmin
	@GetMapping("/verification/{a_aid}")
	public String verification(@PathVariable Long a_aid, RedirectAttributes ra) {
		EventInfo eventinfo = EventService.findByid(a_aid);
		eventinfo.setVerification("Y");
		EventService.update(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "發布成功");
		return "redirect:/managerAllEvent";	
	}

	@RequiresAdmin
	@GetMapping("/deleteverification/{a_aid}")
	public String deleteverification(@PathVariable Long a_aid, RedirectAttributes ra) {
		EventInfo eventinfo = EventService.findByid(a_aid);
		EventService.deletdate(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "已被駁回");
		return "redirect:/managerAllEvent";
	}
	
	@RequiresUser
	@GetMapping("/signupclick/{a_aid}")
	public @ResponseBody Map<String, String> signupclick(
			@PathVariable Long a_aid,
			@SessionAttribute(value = "loginBean") User_Info user_info
	) {
		Map<String , String> map = new HashMap<>();
		 
		EventInfo eventInfo = EventService.findByid(a_aid);

		boolean isEntryformExist = EventService.isEntryformExist(eventInfo, user_info);
		if (!isEntryformExist) {
			if (eventInfo.getA_registration_endrttime().getTime() <= new Date().getTime()) {
				map.put("Time", "這個活動報名時間結束了,報名失敗");
				return map;
			}
			if (!(eventInfo.getEntryforms().size() >= eventInfo.getApplicants())) {
				EventService.saveEntryform(eventInfo, user_info);
				int size = EventService.findentryformByaidreturnsize(eventInfo);
				eventInfo.setHavesignedup(size);
				EventService.saveEvent(eventInfo);
			} else {
				map.put("Exceed", "這個活動報名已經額滿,報名失敗");
				return map ;
			}
		
			map.put("succes", "報名成功");
		} else {
			map.put("fail", "你已經報名過了喔~");
		}

		return map;
	}

	@RequiresUser
	@GetMapping("/signupEvent/{a_aid}")
	public String signupEvent(@PathVariable Long a_aid, Model model) {
		EventInfo Event = EventService.findByid(a_aid);
		model.addAttribute("signupEvent",Event);
		
		return "event/signupEvent";	
	}

	@RequiresUser
	@GetMapping("/deletesignupEvent/{e_id}/{a_id}")
	public String signupEvent(
			@PathVariable Long e_id,
			@PathVariable Long a_id,
			Model model
	) {
		EventInfo Event = EventService.findByid(a_id);
		EventService.deleteEntryformByid(e_id);

		int size = EventService.findentryformByaidreturnsize(Event);
		Event.setHavesignedup(size);
		EventService.saveEvent(Event);
		
		model.addAttribute("signupEvent",Event);
		
		return "event/signupEvent";
	}
	

	// ==================== @ModelAttribute ====================
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
    public Map<String, String> eventtype(){
		Map<String, String> map = new HashMap<>();
		
		map.put("研討會", "研討會");
		map.put("線下課程", "線下課程");
		map.put("講座", "講座");
		map.put("分享會", "分享會");

		return map;
    }
}