package com.group5.springboot.dao.chat;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;
import com.group5.springboot.model.user.User_Info;

@Repository
public class ChatDaoImpl implements ChatDao{
	
	@Autowired
	EntityManager em;
	
	@Autowired
	Chat_Info chat_Info;
	
	@Autowired
	Chat_Reply chat_Reply;

	@Override
	public void insertChat(Chat_Info chat_Info) {
		User_Info user_Info = em.find(User_Info.class, chat_Info.getU_ID());
		chat_Info.setUser_Info(user_Info);
		em.persist(chat_Info);
	}

	@Override
	public void deleteChat(int c_ID) {
		em.remove(em.find(Chat_Info.class, c_ID));
	}

	@Override
	public void updateChat(Chat_Info chat_Info) {
		em.merge(chat_Info);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Chat_Info> findAllChat() {
		String sql = "from Chat_Info";
		List<Chat_Info> chat_Info = em.createQuery(sql).getResultList();
		return chat_Info;
	}

	@Override
	public Chat_Info selectChatById(int c_ID) {
		Chat_Info chat_Info = em.find(Chat_Info.class, c_ID);
		return chat_Info;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Chat_Reply> findAllChatReply(int c_ID) {
		String sql = "from Chat_Reply where c_ID = " + c_ID;
		List<Chat_Reply> chat_Reply = em.createQuery(sql).getResultList();
		return chat_Reply;
	}
	
	@Override
	public void insertFirstChatReply(Chat_Info chat) {
		String sql = "INSERT INTO [dbo].[chat_Reply]\r\n"
				+ "           ([c_ID]\r\n"
				+ "           ,[c_Conts]\r\n"
				+ "           ,[c_Date]\r\n"
				+ "           ,[u_ID])\r\n"
				+ "     VALUES\r\n"
				+ "           ('" + chat.getC_ID() + "'\r\n"
				+ "           ,'" + chat.getC_Conts() + "'\r\n"
				+ "           ,'" + chat.getC_Date() + "'\r\n"
				+ "           ,'" + chat.getU_ID() + "')";
		em.createNativeQuery(sql).executeUpdate();
	}

	@Override
	public void insertChatReply(Chat_Reply chat_Reply) {
		User_Info user_Info = em.find(User_Info.class, chat_Reply.getU_ID());
		chat_Reply.setUser_Info(user_Info);
		em.persist(chat_Reply);
	}

	@Override
	public void deleteChatReply(int c_ID) {
		String sql = "DELETE FROM [dbo].[chat_Reply]\r\n"
				+ "WHERE [c_ID] = " + c_ID;
		em.createNativeQuery(sql).executeUpdate();
	}

}
