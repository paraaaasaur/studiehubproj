package com.group5.springboot.dao.chat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.group5.springboot.model.chat.Chat_Info;

@Repository
public interface ChatDao {
	
	public void insertChat(Chat_Info chat);
	
    public void deleteChat(int c_ID);
	
    public void updateChat(Chat_Info chat);
	
	public List<Chat_Info> findAllChat();
	
	public Chat_Info selectChatById(int c_ID);
	
	public void createChatTable(String table_Name);
	
	public void deleteChatTable(String table_Name);

}
