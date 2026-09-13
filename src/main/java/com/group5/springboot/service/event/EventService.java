package com.group5.springboot.service.event;

import com.group5.springboot.dto.event.CreateEventForm;
import com.group5.springboot.dto.event.CreateEventView;
import com.group5.springboot.dto.event.UpdateEventForm;
import com.group5.springboot.dto.event.UpdateEventView;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;

import java.util.List;
import java.util.Map;

public interface EventService {
	void saveEvent(EventInfo eventinfo);

	Map<String, Object> Eventfindbyuid(String a_uid);

	Map<String, Object> EventfindAll();

	List<EventInfo> adminSearch(String rname, Boolean approved, boolean includeEntryforms);

	List<EventInfo> guestEventfindAll();

	Map<String, Object> queryByName(String rname);

	List<EventInfo> userSearch(String rname, String u_id);

	EventInfo findByid(Long id);

	EventInfo guestFindByid(Long id);

	void update(EventInfo eventinfo);

	void deletdate(EventInfo eventinfo);

	void saveEntryform(EventInfo eventInfo, User_Info user_info);

	List<Entryform> findentryformByaid(EventInfo eventinfo);

	void deleteEntryformByid(long id);

	boolean isEntryformExist(EventInfo eventInfo,User_Info user_info);

	int findentryformByaidreturnsize(EventInfo eventinfo);

	EventInfo applyToEntity(CreateEventForm form);

	CreateEventView mapToCreateEventView(CreateEventForm form);

	UpdateEventView mapToUpdateEventView(EventInfo entity);

	UpdateEventView mapToUpdateEventView(Long a_aid, UpdateEventForm form);

	EventInfo applyToEntity(Long a_aid, UpdateEventForm form);
}