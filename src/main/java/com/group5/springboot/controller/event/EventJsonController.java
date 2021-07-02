package com.group5.springboot.controller.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.group5.springboot.service.event.EventServiceImpl;
@Controller
public class EventJsonController {

	@Autowired	
	EventServiceImpl eventserviceImpl;
	
	@GetMapping(value = "/EventfindAll", produces = "application/json; charset=UTF8")
	//produces   它的作用是指定返回值类型，不但可以设置返回值类型还可以设定返回值的字符编码；
	public @ResponseBody Map<String, Object> EventfindAll() {
		return eventserviceImpl.EventfindAll();
	}
	@GetMapping(value = "/queryEventByName", produces = "application/json; charset=UTF8")
	public @ResponseBody Map<String, Object> queryByName(@RequestParam("rname") String rname) {
//		@RequestParam 從前端畫面找 name = rname 的值  抓的是請求參數  /findByTypeId?rname=string
		return eventserviceImpl.queryByName(rname);
		
	}
}
