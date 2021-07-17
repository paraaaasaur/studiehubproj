package com.group5.springboot.controller.event;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
@Controller
public class EventJsonController {

	@Autowired	
	EventServiceImpl eventserviceImpl;
	//顯示所有活動表 回傳給前端
	@GetMapping(value = "/EventfindAll", produces = "application/json; charset=UTF8")
	//produces   它的作用是指定返回值类型，不但可以设置返回值类型还可以设定返回值的字符编码；
	public @ResponseBody Map<String, Object> EventfindAll() {
		return eventserviceImpl.EventfindAll();
	}
	
	//用Name尋找 顯示前端
	@GetMapping(value = "/queryEventByName", produces = "application/json; charset=UTF8")
	public @ResponseBody Map<String, Object> queryByName(@RequestParam("rname") String rname) {
//		@RequestParam 從前端畫面找 name = rname 的值  抓的是請求參數  /findByTypeId?rname=string
		
		return eventserviceImpl.queryByName(rname);
		
	}
	
	//用id尋找 顯示活動內容 
	@GetMapping(value = "/eventcontentjson/{a_aid}", produces = "application/json; charset=UTF8")
	public @ResponseBody EventInfo eventcontentjson(@PathVariable Long a_aid ) {
		//依照AID搜尋出一筆活動表
		EventInfo eventInfo = eventserviceImpl.findByid(a_aid);
		//用AID搜尋到的活動表 找出相關聯的 報名表 用.size 取得 跟 活動表相關聯的數量 代表有幾個報名人數
		int size=eventserviceImpl.findentryformByaidreturnsize(eventInfo);
		//把搜尋到的數量放進活動表
		eventInfo.setHavesignedup(size);
		
		return eventInfo ;
		
	}
	//用放在Session的uid尋找活動表 
	@GetMapping(value = "/Eventfindbyuid", produces = "application/json; charset=UTF8")
	public @ResponseBody Map<String, Object> Eventfindbyuid(@SessionAttribute(value = "loginBean")  User_Info user_info) {
		
		String a_uid = user_info.getU_id();
		
		return eventserviceImpl.Eventfindbyuid(a_uid);
	}
	//用報名表單的id尋找 
	@GetMapping(value = "/signupEventjson/{a_aid}", produces = "application/json; charset=UTF8")
	public @ResponseBody List<Entryform> signupEventjson(@PathVariable Long a_aid ) {
		EventInfo Event = eventserviceImpl.findByid(a_aid);
		
		List<Entryform> Entryform = eventserviceImpl.findentryformByaid(Event);

		return Entryform ;
		
	}

	
	
}
