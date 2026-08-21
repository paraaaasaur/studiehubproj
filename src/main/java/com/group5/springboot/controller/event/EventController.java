package com.group5.springboot.controller.event;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.config.StorageConfigProperties;
import com.group5.springboot.dto.event.CreateEventForm;
import com.group5.springboot.dto.event.CreateEventView;
import com.group5.springboot.dto.event.UpdateEventForm;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.event.EventService;
import com.group5.springboot.utils.ResourceLocationResolver;
import com.group5.springboot.validate.EventValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@Controller
public class EventController {
	final EventService eventService;
	final EventValidator eventValidator;

	private final String IMAGE_STORAGE_DIR;
	private final String IMAGE_URL_BASE;


	@Autowired
	public EventController(EventService eventService, EventValidator eventValidator, StorageConfigProperties props) {
		this.eventService = eventService;
		this.eventValidator = eventValidator;
		IMAGE_STORAGE_DIR = props.getEventImageUploadStorageDir();
		IMAGE_URL_BASE = StorageConfigProperties.storagePathToViewAndDbUrl(IMAGE_STORAGE_DIR);
	}


	@RequiresUser
	@GetMapping("/insertEvent")
	public String insertEvent(Model model) {
		addCreateEventAttributes(model);
		return "events/add";
	}

	@RequiresUser
	@GetMapping("/userAllEvent")
	public String userAllEvent() {
		return "events/my-list";
	}

	@RequiresAdmin
	@GetMapping("/adminAllEvent")
	public String queryRestaurant() {
		return "events/admin/list";
	}

	@GetMapping("/eventindex")
	public String eventindex() {
		return "events/list";
	}

	@RequiresAdmin
	@GetMapping("/managerAllEvent")
	public String managerAllEvent() {
		return "events/admin/pending-list";
	}

	@RequiresUser
	@PostMapping("/insertEvent")
	public String insertSaveEvent(
			CreateEventForm form,
			@SessionAttribute User_Info loginBean,
			RedirectAttributes ra, Model model
	) {
		var errors = eventValidator.validate(form);
		if (errors.hasErrors()) {
			readdCreateEventAttributes(model, form, errors);
			return "events/add";
		}

		EventInfo eventinfo = eventService.applyToEntity(form);

		eventService.saveEvent(eventinfo);
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
			
			eventinfo.setUidname(loginBean.getU_lastname()+loginBean.getU_firstname());
			eventinfo.setExpired("未過期");
			eventinfo.setVerification("N");
			eventinfo.setA_uid(loginBean.getU_id());
			eventService.saveEvent(eventinfo);
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
		addUpdateEventAttributes(model, a_aid);
		return "events/edit";
	}

	@RequiresUser
	@PostMapping("/updateEvent/{a_aid}")
	public String updateSaveEvent(
			@PathVariable Long a_aid, UpdateEventForm form,
			@SessionAttribute User_Info loginBean,
			RedirectAttributes ra, Model model
	) {
		var errors = eventValidator.validate(form);
		if (errors.hasErrors()) {
			readdUpdateEventAttributes(model, a_aid, form, errors);
			return "events/edit";
		}

		var eventinfo = eventService.applyToEntity(a_aid, form);

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

			eventinfo.setUidname(loginBean.getU_lastname()+loginBean.getU_firstname());
			eventinfo.setExpired("未過期");
			eventinfo.setVerification("N");
			eventService.update(eventinfo);
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
		EventInfo eventinfo = eventService.findByid(a_aid);
		eventService.deletdate(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "下架成功");
		return "redirect:/userAllEvent";
	}

	@RequiresAdmin
	@GetMapping("/deleteadminEvent/{a_aid}")
	public String deleteadminEvent(@PathVariable Long a_aid, RedirectAttributes ra) {
		EventInfo eventinfo = eventService.findByid(a_aid);
		eventService.deletdate(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "下架成功");
		return "redirect:/adminAllEvent";
	}

	@GetMapping("/Selecteventcontent/{a_aid}")
	public String Selecteventcontent() {
		return "events/detail";
	}
	
	@RequiresAdmin
	@GetMapping("/verification/{a_aid}")
	public String verification(@PathVariable Long a_aid, RedirectAttributes ra) {
		EventInfo eventinfo = eventService.findByid(a_aid);
		eventinfo.setVerification("Y");
		eventService.update(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "發布成功");
		return "redirect:/managerAllEvent";	
	}

	@RequiresAdmin
	@GetMapping("/deleteverification/{a_aid}")
	public String deleteverification(@PathVariable Long a_aid, RedirectAttributes ra) {
		EventInfo eventinfo = eventService.findByid(a_aid);
		eventService.deletdate(eventinfo);
		ra.addFlashAttribute("successMessage",eventinfo.getA_name() + "已被駁回");
		return "redirect:/managerAllEvent";
	}
	
	@RequiresUser
	@GetMapping("/signupclick/{a_aid}")
	public @ResponseBody Map<String, String> signupclick(@PathVariable Long a_aid, @SessionAttribute User_Info loginBean) {
		Map<String , String> map = new HashMap<>();
		 
		EventInfo eventInfo = eventService.findByid(a_aid);

		boolean isEntryformExist = eventService.isEntryformExist(eventInfo, loginBean);
		if (!isEntryformExist) {
			if (eventInfo.getA_registration_endrttime().getTime() <= new Date().getTime()) {
				map.put("Time", "這個活動報名時間結束了,報名失敗");
				return map;
			}
			if (!(eventInfo.getEntryforms().size() >= eventInfo.getApplicants())) {
				eventService.saveEntryform(eventInfo, loginBean);
				int size = eventService.findentryformByaidreturnsize(eventInfo);
				eventInfo.setHavesignedup(size);
				eventService.saveEvent(eventInfo);
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
		EventInfo Event = eventService.findByid(a_aid);
		model.addAttribute("signupEvent",Event);
		
		return "events/registration/list";
	}

	@RequiresUser
	@GetMapping("/deletesignupEvent/{e_id}/{a_id}")
	public String signupEvent(
			@PathVariable Long e_id,
			@PathVariable Long a_id,
			Model model
	) {
		EventInfo Event = eventService.findByid(a_id);
		eventService.deleteEntryformByid(e_id);

		int size = eventService.findentryformByaidreturnsize(Event);
		Event.setHavesignedup(size);
		eventService.saveEvent(Event);
		
		model.addAttribute("signupEvent",Event);
		
		return "events/registration/list";
	}


	// model attrs
	public Map<String, String> eventtype() {
		Map<String, String> map = new HashMap<>();
		
		map.put("研討會", "研討會");
		map.put("線下課程", "線下課程");
		map.put("講座", "講座");
		map.put("分享會", "分享會");

		return map;
	}

	// helpers
	private void readdCreateEventAttributes(Model model, CreateEventForm form, BindingResult errors) {
		var view = eventService.mapToCreateEventView(form);

		model.addAttribute("eventtype", eventtype());
		model.addAttribute("createEventView", view);
		model.addAttribute(MODEL_KEY_PREFIX + "createEventView", errors);
	}

	private void addCreateEventAttributes(Model model) {
		model.addAttribute("createEventView", CreateEventView.newInstance());
		model.addAttribute("eventtype", eventtype());
	}

	private void addUpdateEventAttributes(Model model, Long a_aid) {
		var entity = eventService.findByid(a_aid);
		var view = eventService.mapToUpdateEventView(entity);

		model.addAttribute("eventtype", eventtype());
		model.addAttribute("updateEventView", view);
	}

	private void readdUpdateEventAttributes(Model model, Long a_aid, UpdateEventForm form, BindingResult errors) {
		var view = eventService.mapToUpdateEventView(a_aid, form);

		model.addAttribute("eventtype", eventtype());
		model.addAttribute("updateEventView", view);
		model.addAttribute(MODEL_KEY_PREFIX + "updateEventView", errors);
	}
}