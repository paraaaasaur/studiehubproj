package com.group5.springboot.service.chat;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group5.springboot.model.chat.Chat_Info;

@Service
public interface ChatService {
	
	public void insertChat(Chat_Info chat_Info);
	
    public void deleteChat(int c_ID);
	
    public void updateChat(Chat_Info chat_Info);
	
	public List<Chat_Info> findAllChat();
	
	public Chat_Info selectChatById(int c_ID);

}
