package com.group5.springboot.model.chat.scaffolding.dev;

import com.group5.springboot.model.chat.Chat_Reply;
import com.group5.springboot.model.user.User_Info;

/**
 * <li>A handy Java object to reflect the response structure currently
 * used to build thread content in the view, instead of intended use
 * of JPA framework.</li>
 * <li>Should have been a single {@link Chat_Reply} to access {@link User_Info}
 * via JPA association.</li>
 * @JSON-index-0 {@link Chat_Reply}
 * @JSON-index-1 {@link User_Info}
 **/
public final class PostWithPoster {
	private final Chat_Reply chat_Reply;
	private final User_Info user_Info;


	public PostWithPoster(Chat_Reply chatReply, User_Info userInfo) {
		this.chat_Reply = chatReply;
		this.user_Info = userInfo;
	}


	public Chat_Reply getChat_Reply() {
		return chat_Reply;
	}

	public User_Info getUser_Info() {
		return user_Info;
	}
}