package com.group5.springboot.dao.chat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;

@Repository
public interface ChatDao {
	
	public void insertChat(Chat_Info chat);
	
    public void deleteChat(int c_ID);
	
    public void updateChat(Chat_Info chat);
	
	public List<Chat_Info> findAllChat();
	
	public Chat_Info selectChatById(int c_ID);
	
	public List<Chat_Reply> findAllChatReply(int c_IDr);
	
	public void insertFirstChatReply(Chat_Info chat);
	
	public void insertChatReply(Chat_Reply chat);
	
	public void deleteChatReply(int c_IDr);

}
