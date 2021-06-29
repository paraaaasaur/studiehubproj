package com.group5.springboot.service.event;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group5.springboot.dao.event.EventDao;
import com.group5.springboot.model.event.EventInfo;



@Service
@Transactional
public class EventServiceImpl {
	
	@Autowired
	EventDao EventDao;
	//新增儲存
	public void saveEvent(EventInfo eventinfo) {
		
		EventDao.saveEvent(eventinfo);
		
	};
	//搜尋全部
	public Map<String, Object>  EventfindAll(){
		
		return EventDao.EventfindAll();
	};
    //模糊搜尋 
	public Map<String, Object> queryByName(String rname){
		 
		return EventDao.queryByName(rname);
	};
	//用a_aid搜尋(修改表單)
	public EventInfo findByid(Long id) {
		
		return EventDao.findByid(id);
	};
	//修改
	public void update(EventInfo eventinfo) {
		 EventDao.update(eventinfo);
	} ;

	public void deletdate(EventInfo eventinfo) {

		EventDao.deletdate(eventinfo);
	}
	
}
