package com.group5.springboot.service.chat;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group5.springboot.dao.chat.ChatDao;
import com.group5.springboot.model.chat.Chat_Info;

@Service
@Transactional
public class ChatServiceImpl implements ChatService{

	@Autowired
	ChatDao chatDao;
	
	@Override
	public void insertChat(Chat_Info chat_Info) {
		chatDao.insertChat(chat_Info);
	}

	@Override
	public void deleteChat(int c_ID) {
		chatDao.deleteChat(c_ID);
	}

	@Override
	public void updateChat(Chat_Info chat_Info) {
		chatDao.updateChat(chat_Info);
	}

	@Override
	public List<Chat_Info> findAllChat() {
		return chatDao.findAllChat();
	}

	@Override
	public Chat_Info selectChatById(int c_ID) {
		return chatDao.selectChatById(c_ID);
	}

	@Override
	public void createChatTable(String table_Name) {
		chatDao.createChatTable(table_Name);
	}

	@Override
	public void deleteChatTable(String table_Name) {
		chatDao.deleteChatTable(table_Name);
	}

}
