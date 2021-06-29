package com.group5.springboot.dao.event;



import java.util.Map;

import org.springframework.stereotype.Repository;

import com.group5.springboot.model.event.EventInfo;

@Repository
public interface EventDao {


	
	//把新增頁面POST請求 儲存進資料庫	
	public void saveEvent(EventInfo eventinfo);
	//搜尋所有+回傳list數量(AJAX)
	public Map<String, Object>  EventfindAll();
    //模糊搜尋(AJAX)
	public Map<String, Object> queryByName(String rname);
	//用a_aid搜尋(修改表單)
	public EventInfo findByid(Long id);
	//修改表單
	public void update(EventInfo eventinfo) ;
	//刪除
	public void deletdate(EventInfo eventinfo);

	
	
	
}
