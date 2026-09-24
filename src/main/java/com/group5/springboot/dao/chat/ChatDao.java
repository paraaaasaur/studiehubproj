package com.group5.springboot.dao.chat;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;

import java.util.List;

public interface ChatDao {
	/** Insert a top-post as a {@link Chat_Info}. */
	void insertChat(Chat_Info chat);
	
    void deleteChat(int c_ID);
	
	List<Chat_Info> findAllChat();
	
	Chat_Info selectChatById(int c_ID);
	
	List<Chat_Reply> findAllChatReply(int c_IDr);
	
	Chat_Reply selectChatReplyById(int c_ID);

	/**
	 * (pre-2.0.0 legacy behavior)
	 * Insert a top-post's redundancy as a {@link Chat_Reply}, which gets stored 
	 * as the first record when querying the {@code chat_reply} DB table.
	 **/
	void insertFirstChatReply(Chat_Info chat);
	
	void insertChatReply(Chat_Reply chat);
	
	void deleteChatReply(int c_IDr);
	
	void updateChatReply(Chat_Reply chat);
}