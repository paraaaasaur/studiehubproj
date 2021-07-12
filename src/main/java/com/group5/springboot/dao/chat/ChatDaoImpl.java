package com.group5.springboot.dao.chat;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.user.User_Info;

@Repository
public class ChatDaoImpl implements ChatDao{
	
	@Autowired
	EntityManager em;
	
	@Autowired
	Chat_Info chat_Info;

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

	@Override
	public void createChatTable(String table_Name) {
		String sql = "CREATE TABLE [dbo].[" + table_Name + "](\r\n"
				+ "	[c_ID] [int] NOT NULL,\r\n"
				+ "	[c_Conts] [varchar](max) NULL,\r\n"
				+ "	[c_Date] [varchar](255) NULL,\r\n"
				+ "	[u_ID] [varchar](255) NULL,\r\n"
				+ " CONSTRAINT [PK_Table_1] PRIMARY KEY CLUSTERED \r\n"
				+ "(\r\n"
				+ "	[c_ID] ASC\r\n"
				+ ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]\r\n"
				+ ") ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]";
		em.createNativeQuery(sql).executeUpdate();
	}

	@Override
	public void deleteChatTable(String table_Name) {
		String sql = "DROP TABLE [dbo].[" + table_Name + "]";
		em.createNativeQuery(sql).executeUpdate();
	}

}
