package com.group5.springboot.service.chat;

import com.group5.springboot.dao.chat.ChatDao;
import com.group5.springboot.dao.user.UserDao;
import com.group5.springboot.dto.chat.CreateReplyRequest;
import com.group5.springboot.dto.chat.CreateTopPostRequest;
import com.group5.springboot.dto.chat.UpdatePostRequest;
import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.utils.HtmlSanitizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {
	private final ChatDao chatDao;
	private final UserDao userDao;


	@Autowired
	public ChatServiceImpl(ChatDao chatDao, UserDao userDao) {
		this.chatDao = chatDao;
		this.userDao = userDao;
	}


	@Override
	public void insertTopPostAndRedundancy(String u_ID, CreateTopPostRequest data) {
		// 1. apply domain rule
		var sanitizedData = sanitizeConts(data);
		
		// 2. apply data & relationship to a new entity
		var entity = applyToEntity(u_ID, sanitizedData);
		
		// 3. persist
		chatDao.insertChat(entity);
		chatDao.insertFirstChatReply(entity);
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
	public void insertChatReply(String U_ID, CreateReplyRequest data) {
		CreateReplyRequest sanitizedData = sanitizeConts(data);
		Chat_Reply entity = applyToEntity(U_ID, sanitizedData);

		chatDao.insertChatReply(entity);
	}

	@Override
	public void deleteChatReply(int c_IDr) {
		chatDao.deleteChatReply(c_IDr);
	}

	/**
	 * @return the merged entity (write-only)
	 **/
	@Override
	public Chat_Reply updateChatReply(UpdatePostRequest data) {
		var sanitizedData = sanitizeConts(data);
		var topPostRedundancy = applyToEntity(sanitizedData);

		chatDao.updateChatReply(topPostRedundancy);

		return topPostRedundancy;
	}


	// utilities
	private CreateTopPostRequest sanitizeConts(CreateTopPostRequest rawTopPost) {
		String sanitized = HtmlSanitizerUtil.sanitize(rawTopPost.getC_Conts());
		return new CreateTopPostRequest(
				rawTopPost.getC_Date(),
				rawTopPost.getC_Class(),
				rawTopPost.getC_Title(),
				sanitized
		);
	}

	private CreateReplyRequest sanitizeConts(CreateReplyRequest rawReply) {
		String sanitizedConts = HtmlSanitizerUtil.sanitize(rawReply.getC_Conts());
		return new CreateReplyRequest(
				rawReply.getC_IDr(),
				rawReply.getC_Date(),
				sanitizedConts
		);
	}

	private UpdatePostRequest sanitizeConts(UpdatePostRequest rawTopPost) {
		String sanitizedConts = HtmlSanitizerUtil.sanitize(rawTopPost.getC_Conts());
		return new UpdatePostRequest(
				rawTopPost.getC_ID(),
				rawTopPost.getC_IDr(),
				rawTopPost.getC_Date(),
				sanitizedConts
		);
	}


	// helpers
	private Chat_Info applyToEntity(String u_ID, CreateTopPostRequest data) {
		var entity = new Chat_Info();

		// apply JPA relationship
		User_Info poster = userDao.getSingleUser(u_ID);
		entity.setUser_Info(poster);
		entity.setU_ID(u_ID); // to be removed in 2.0.0-schema-redesign
		// apply data
		entity.setC_Date(data.getC_Date());
		entity.setC_Class(data.getC_Class());
		entity.setC_Title(data.getC_Title());
		entity.setC_Conts(data.getC_Conts());

		return entity;
	}

	private Chat_Reply applyToEntity(String U_ID, CreateReplyRequest data) {
		Chat_Reply entity = new Chat_Reply();

		// apply JPA relationship
		User_Info dbReplier = userDao.getSingleUser(U_ID);
		entity.setUser_Info(dbReplier);
		entity.setU_ID(U_ID); // to be removed in 2.0.0-schema-redesign
		Chat_Info dbTopPost = chatDao.selectChatById(data.getC_IDr());
		entity.setChat_Info(dbTopPost);
		entity.setC_IDr(data.getC_IDr()); // to be removed in 2.0.0-schema-redesign

		// apply data
		entity.setC_Date(data.getC_Date());
		entity.setC_Conts(data.getC_Conts());

		return entity;
	}

	private Chat_Reply applyToEntity(UpdatePostRequest data) {
		var topPostRedundancy = chatDao.selectChatReplyById(data.getC_ID());

		// update JPA relationship
		// n/a

		// update data
		topPostRedundancy.setC_Conts(data.getC_Conts());

		return topPostRedundancy;
	}
}