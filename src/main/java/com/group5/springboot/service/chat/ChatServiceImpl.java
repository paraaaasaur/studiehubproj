package com.group5.springboot.service.chat;

import java.util.List;

import javax.transaction.Transactional;

import com.group5.springboot.utils.HtmlSanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group5.springboot.dao.chat.ChatDao;
import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {
	final ChatDao chatDao;


	@Autowired
	public ChatServiceImpl(ChatDao chatDao) {
		this.chatDao = chatDao;
	}


	@Override
	public void insertChat(Chat_Info chat_Info) {
		chatDao.insertChat(chat_Info);
	}

	@Override
	public void deleteChat(int c_ID) {
		chatDao.deleteChat(c_ID);
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
	public Chat_Reply selectChatReplyById(int c_ID) {
		return chatDao.selectChatReplyById(c_ID);
	}

	@Override
	public List<Chat_Reply> findAllChatReply(int c_IDr) {
		return chatDao.findAllChatReply(c_IDr);
	}
	
	@Override
	public void insertFirstChatReply(Chat_Info chat_Info) {
		chatDao.insertFirstChatReply(chat_Info);
	}

	@Override
	public void insertChatReply(Chat_Reply chat_Reply) {
		chatDao.insertChatReply(chat_Reply);
	}

	@Override
	public void deleteChatReply(int c_IDr) {
		chatDao.deleteChatReply(c_IDr);
	}

	@Override
	public void updateChatReply(Chat_Reply chat_Reply) {
		chatDao.updateChatReply(chat_Reply);
	}

	@Override
	public void sanitizeConts(Chat_Reply rawReply) {
		String sanitized = HtmlSanitizerUtil.sanitize(rawReply.getC_Conts());
		rawReply.setC_Conts(sanitized);
	}

	@Override
	public void sanitizeConts(Chat_Info rawTopPost) {
		String sanitized = HtmlSanitizerUtil.sanitize(rawTopPost.getC_Conts());
		rawTopPost.setC_Conts(sanitized);
	}
}