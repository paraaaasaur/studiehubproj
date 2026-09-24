package com.group5.springboot.dao.chat;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ChatDaoImpl implements ChatDao {
	final EntityManager em;


	@Autowired
	public ChatDaoImpl(EntityManager em) {
		this.em = em;
	}


	@Override
	public void insertChat(Chat_Info chat_Info) {
		em.persist(chat_Info);
	}

	@Override
	public void deleteChat(int c_ID) {
		em.remove(em.find(Chat_Info.class, c_ID));
	}

	@Override
	public List<Chat_Info> findAllChat() {
		String hql = "from Chat_Info";
		List<Chat_Info> chat_Info = em.createQuery(hql).getResultList();
		return chat_Info;
	}

	@Override
	public Chat_Info selectChatById(int c_ID) {
		return em.find(Chat_Info.class, c_ID);
	}
	
	@Override
	public Chat_Reply selectChatReplyById(int c_ID) {
		return em.find(Chat_Reply.class, c_ID);
	}

	@Override
	public List<Chat_Reply> findAllChatReply(int c_IDr) {
		String hql = "from Chat_Reply c left join User_Info u on c.u_ID = u.u_id where c.c_IDr = " + c_IDr;
		List<Chat_Reply> chat_Reply = em.createQuery(hql).getResultList();
		return chat_Reply;
	}
	
	@Override
	public void insertFirstChatReply(Chat_Info chat) {
		String sql = "INSERT INTO [dbo].[chat_Reply]\r\n"
				+ "           ([c_IDr]\r\n"
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
		em.persist(chat_Reply);
	}

	@Override
	public void deleteChatReply(int c_IDr) {
		String sql = "DELETE FROM [dbo].[chat_Reply]\r\n"
				+ "WHERE [c_IDr] = " + c_IDr;
		em.createNativeQuery(sql).executeUpdate();
	}

	@Override
	public void updateChatReply(Chat_Reply topPostRedundancy) {
		em.merge(topPostRedundancy);
	}
}