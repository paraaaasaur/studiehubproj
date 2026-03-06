package com.group5.springboot.service.chat;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;

@Service
public interface ChatService {
	void insertChat(Chat_Info chat_Info);
	
    void deleteChat(int c_ID);
	
	List<Chat_Info> findAllChat();
	
	Chat_Info selectChatById(int c_ID);
	
	Chat_Reply selectChatReplyById(int c_ID);
	
	List<Chat_Reply> findAllChatReply(int c_IDr);
	
	void insertFirstChatReply(Chat_Info chat_Info);
	
	void insertChatReply(Chat_Reply chat_Reply);
	
	void deleteChatReply(int c_IDr);
	
	void updateChatReply(Chat_Reply chat_Reply);

	/**
	 * Try to remove potential risk for XSS attack embedded in the HTML content
	 * user attackers might send.
	 * @param rawReply Raw user reply
	 **/
	void sanitizeConts(Chat_Reply rawReply);

	/**
	 * Try to remove potential risk for XSS attack embedded in the HTML content
	 * user attackers might send.
	 * @param rawTopPost Raw user top-post
	 **/
	void sanitizeConts(Chat_Info rawTopPost);
}