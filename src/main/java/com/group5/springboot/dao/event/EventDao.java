package com.group5.springboot.dao.event;

import java.util.List;
import java.util.Map;

import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;

public interface EventDao {
	void saveEvent(EventInfo eventinfo);

	Map<String, Object>  EventfindAll();

	Map<String, Object> queryByName(String rname);

	/**
	 * @param rname partial match
	 * @param u_id full match
	 * @param approved true, false, null <=> filter verification = "Y", "N", both
	 * @param includeEntryform whether include the child entity {@link Entryform} Set
	 **/
	List<EventInfo> search(String rname, String u_id, Boolean approved, boolean includeEntryform);

	Map<String, Object>  EventfindBYuid(String a_uid);

	EventInfo findByid(Long id);

	void update(EventInfo eventinfo) ;

	void deletdate(EventInfo eventinfo);

	void saveEntryform(EventInfo eventInfo , User_Info user_info);

	List<Entryform> findentryformByaid(EventInfo eventinfo);

	void deleteEntryformByid(long id);

	boolean isEntryformExist(EventInfo eventInfo , User_Info user_info) ;

	int findentryformByaidreturnsize(EventInfo eventinfo);
}