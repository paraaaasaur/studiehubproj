package com.group5.springboot.dao.chat;

import java.util.List;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;

public interface ChatDao {
	void insertChat(Chat_Info chat);
	
    void deleteChat(int c_ID);
	
	List<Chat_Info> findAllChat();
	
	Chat_Info selectChatById(int c_ID);
	
	List<Chat_Reply> findAllChatReply(int c_IDr);
	
	Chat_Reply selectChatReplyById(int c_ID);
	
	void insertFirstChatReply(Chat_Info chat);
	
	void insertChatReply(Chat_Reply chat);
	
	void deleteChatReply(int c_IDr);
	
	void updateChatReply(Chat_Reply chat);
}