package com.group5.springboot.dao.chat;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.chat.Chat_Info;

@Repository
public class ChatDaoImpl implements ChatDao{
	
	@Autowired
	EntityManager em;
	
	@Autowired
	Chat_Info chat_Info;

	@Override
	public void insertChat(Chat_Info chat_Info) {
		em.persist(chat_Info);
	}

	@Override
	public void deleteChat(int c_ID) {
		em.remove(em.find(Chat_Info.class, c_ID));
	}

	@Override
	public void updateChat(Chat_Info chat_Info) {
		em.merge(chat_Info);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Chat_Info> findAllChat() {
		String sql = "from Chat_Info";
		List<Chat_Info> chat_Info = em.createQuery(sql).getResultList();
		return chat_Info;
	}

	@Override
	public Chat_Info selectChatById(int c_ID) {
		Chat_Info chat_Info = em.find(Chat_Info.class, c_ID);
		return chat_Info;
	}

}
