package com.group5.springboot.controller.event;

import java.util.List;
import java.util.Map;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.exception.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.event.EventServiceImpl;

import javax.servlet.http.HttpServletResponse;

@Controller
public class EventJsonController {
	@Autowired EventServiceImpl eventserviceImpl;


	@RequiresAdmin
	@GetMapping(value = "/EventfindAll", produces = "application/json; charset=UTF8")
	public @ResponseBody Map<String, Object> EventfindAll() {
		return eventserviceImpl.EventfindAll();
	}

	@GetMapping(value = "/guest/EventfindAll", produces = "application/json; charset=UTF8")
	public @ResponseBody Map<String, Object> guestEventfindAll() {
		var publicEvents = eventserviceImpl.guestEventfindAll();

		return Map.of(
				"list", publicEvents,
				"size", publicEvents.size()
		);
	}

	@RequiresAdmin
	@GetMapping("/admin/events")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> adminFindEvents(String rname, Boolean approved, boolean includeEntryforms) {
		var events = eventserviceImpl.adminSearch(rname, approved, includeEntryforms);

		return ResponseEntity.ok(Map.of(
				"list", events,
				"size", events.size()
		));
	}
	
	@RequiresAdmin
	@GetMapping(value = "/queryEventByName", produces = "application/json; charset=UTF8")
	public @ResponseBody Map<String, Object> queryByName(@RequestParam("rname") String rname) {
		return eventserviceImpl.queryByName(rname);
		
	}

	@RequiresUser
	@GetMapping("/me/events")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> userFindEvents(String rname, @SessionAttribute("loginBean") User_Info loginBean) {
		List<EventInfo> userEvents = eventserviceImpl.userSearch(rname, loginBean.getU_id());

		return ResponseEntity.ok(Map.of(
				"list", userEvents,
				"size", userEvents.size()
		));
	}
	
	@GetMapping(value = "/eventcontentjson/{a_aid}", produces = "application/json; charset=UTF8")
	public @ResponseBody EventInfo eventcontentjson(@PathVariable Long a_aid, HttpServletResponse res) {
		EventInfo eventInfo = null;
		try {
			eventInfo = eventserviceImpl.guestFindByid(a_aid);
			int size = eventserviceImpl.findentryformByaidreturnsize(eventInfo);
			eventInfo.setHavesignedup(size);
		} catch (AccessDeniedException e) {
			res.setStatus(403);
			res.setContentType("application/json");
		}

		return eventInfo ;
		
	}

	@RequiresUser
	@GetMapping(value = "/Eventfindbyuid", produces = "application/json; charset=UTF8")
	public @ResponseBody Map<String, Object> Eventfindbyuid(@SessionAttribute(value = "loginBean")  User_Info user_info) {
		String a_uid = user_info.getU_id();
		
		return eventserviceImpl.Eventfindbyuid(a_uid);
	}

	@RequiresUser
	@GetMapping(value = "/signupEventjson/{a_aid}", produces = "application/json; charset=UTF8")
	public @ResponseBody List<Entryform> signupEventjson(@PathVariable Long a_aid ) {
		EventInfo Event = eventserviceImpl.findByid(a_aid);
		
		List<Entryform> Entryform = eventserviceImpl.findentryformByaid(Event);

		return Entryform ;
	}
}