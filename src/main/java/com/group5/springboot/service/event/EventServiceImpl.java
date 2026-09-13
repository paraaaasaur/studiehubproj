package com.group5.springboot.service.event;

import com.group5.springboot.dao.event.EventDao;
import com.group5.springboot.dto.event.CreateEventForm;
import com.group5.springboot.dto.event.CreateEventView;
import com.group5.springboot.dto.event.UpdateEventForm;
import com.group5.springboot.dto.event.UpdateEventView;
import com.group5.springboot.exception.AccessDeniedException;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {
	final EventDao EventDao;


	@Autowired
	public EventServiceImpl(EventDao eventDao) {
		EventDao = eventDao;
	}


	@Override
	public void saveEvent(EventInfo eventinfo) {
		EventDao.saveEvent(eventinfo);
	}

	@Override
	public Map<String, Object> Eventfindbyuid(String a_uid) {
		return EventDao.EventfindBYuid(a_uid);
	}

	@Override
	public Map<String, Object> EventfindAll() {
		return EventDao.EventfindAll();
	}

	@Override
	public List<EventInfo> adminSearch(String rname, Boolean approved, boolean includeEntryforms) {
		return EventDao.search(rname, null, approved, includeEntryforms);
	}

	@Override
	public List<EventInfo> guestEventfindAll() {
		Map<String, Object> map = EventDao.EventfindAll();
		List<EventInfo> events = (List<EventInfo>) map.get("list");

		var publicEvents = events.stream()
				.filter(e -> "Y".equalsIgnoreCase(e.getVerification()))
				.collect(Collectors.toList());

		return publicEvents;
	}

	@Override
	public Map<String, Object> queryByName(String rname) {
		return EventDao.queryByName(rname);
	}

	@Override
	public List<EventInfo> userSearch(String rname, String u_id) {
		return EventDao.search(rname, u_id, null, false);
	}

	@Override
	public EventInfo findByid(Long id) {
		return EventDao.findByid(id);
	}

	@Override
	public EventInfo guestFindByid(Long id) {
		EventInfo publicEvent = EventDao.findByid(id);
		if (publicEvent != null && "N".equalsIgnoreCase(publicEvent.getVerification())) {
			throw new AccessDeniedException("Requested event#" + id + " is not public yet; need approval from an admin!");
		}

		return publicEvent;
	}

	@Override
	public void update(EventInfo eventinfo) {
		EventDao.update(eventinfo);
	}

	@Override
	public void deletdate(EventInfo eventinfo) {
		EventDao.deletdate(eventinfo);
	}

	@Override
	public void saveEntryform(EventInfo eventInfo, User_Info user_info) {
		EventDao.saveEntryform(eventInfo, user_info);
	}

	@Override
	public List<Entryform> findentryformByaid(EventInfo eventinfo) {
		return EventDao.findentryformByaid(eventinfo);
	}

	@Override
	public void deleteEntryformByid(long id) {
		EventDao.deleteEntryformByid(id);
	}

	@Override
	public boolean isEntryformExist(EventInfo eventInfo, User_Info user_info) {
		return EventDao.isEntryformExist(eventInfo, user_info);
	}

	@Override
	public int findentryformByaidreturnsize(EventInfo eventinfo) {
		return EventDao.findentryformByaidreturnsize(eventinfo);
	}

	@Override
	public EventInfo applyToEntity(CreateEventForm form) {
		var entity = new EventInfo();
		entity.setA_name(form.getA_name());
		entity.setA_type(form.getA_type());
		entity.setRegistration_starttime(form.getRegistration_starttime());
		entity.setRegistration_endrttime(form.getRegistration_endrttime());
		entity.setTransienta_startTime(form.getTransienta_startTime());
		entity.setTransienta_endTime(form.getTransienta_endTime());
		entity.setA_address(form.getA_address());
		entity.setTransientcomment(form.getTransientcomment());
		entity.setApplicants(form.getApplicants());
		entity.setEventImage(form.getEventImage());

		return entity;
	}

	@Override
	public CreateEventView mapToCreateEventView(CreateEventForm form) {
		return new CreateEventView(
				form.getA_name(),
				form.getA_type(),
				form.getRegistration_starttime(),
				form.getRegistration_endrttime(),
				form.getTransienta_startTime(),
				form.getTransienta_endTime(),
				form.getA_address(),
				form.getTransientcomment(),
				form.getApplicants()
		);
	}

	@Override
	public UpdateEventView mapToUpdateEventView(EventInfo entity) {
		return new UpdateEventView(
				entity.getA_aid(),
				entity.getA_name(),
				entity.getA_type(),
				entity.getRegistration_starttime(),
				entity.getRegistration_endrttime(),
				entity.getTransienta_startTime(),
				entity.getTransienta_endTime(),
				entity.getA_address(),
				entity.getTransientcomment(),
				entity.getApplicants()
		);
	}

	@Override
	public UpdateEventView mapToUpdateEventView(Long a_aid, UpdateEventForm form) {
		return new UpdateEventView(
				a_aid,
				form.getA_name(),
				form.getA_type(),
				form.getRegistration_starttime(),
				form.getRegistration_endrttime(),
				form.getTransienta_startTime(),
				form.getTransienta_endTime(),
				form.getA_address(),
				form.getTransientcomment(),
				form.getApplicants()
		);
	}

	@Override
	public EventInfo applyToEntity(Long a_aid, UpdateEventForm form) {
		var entity = EventDao.findByid(a_aid);
		entity.setA_name(form.getA_name());
		entity.setA_type(form.getA_type());
		entity.setRegistration_starttime(form.getRegistration_starttime());
		entity.setRegistration_endrttime(form.getRegistration_endrttime());
		entity.setTransienta_startTime(form.getTransienta_startTime());
		entity.setTransienta_endTime(form.getTransienta_endTime());
		entity.setA_address(form.getA_address());
		entity.setTransientcomment(form.getTransientcomment());
		entity.setApplicants(form.getApplicants());
		entity.setEventImage(form.getEventImage());

		return entity;
	}
}