package com.group5.springboot.service.chat;

import com.group5.springboot.dto.chat.CreateReplyRequest;
import com.group5.springboot.dto.chat.CreateTopPostRequest;
import com.group5.springboot.dto.chat.UpdatePostRequest;
import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;

import java.util.List;

public interface ChatService {
	void insertTopPostAndRedundancy(String u_ID, CreateTopPostRequest data);
	
    void deleteChat(int c_ID);
	
	List<Chat_Info> findAllChat();
	
	Chat_Info selectChatById(int c_ID);
	
	Chat_Reply selectChatReplyById(int c_ID);
	
	List<Chat_Reply> findAllChatReply(int c_IDr);
	
	void insertChatReply(String U_ID, CreateReplyRequest data);
	
	void deleteChatReply(int c_IDr);
	
	Chat_Reply updateChatReply(UpdatePostRequest data);
}