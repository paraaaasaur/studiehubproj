package com.group5.springboot.service.chat;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;

@Service
public interface ChatService {
	
	public void insertChat(Chat_Info chat_Info);
	
    public void deleteChat(int c_ID);
	
    public void updateChat(Chat_Info chat_Info);
	
	public List<Chat_Info> findAllChat();
	
	public Chat_Info selectChatById(int c_ID);
	
	public List<Chat_Reply> findAllChatReply(int c_IDr);
	
	public void insertFirstChatReply(Chat_Info chat_Info);
	
	public void insertChatReply(Chat_Reply chat_Reply);
	
	public void deleteChatReply(int c_IDr);

}
