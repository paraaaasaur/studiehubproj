package com.group5.springboot.model.chat.scaffolding.dev;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;

/**
 * <li>A mere glue class to represent the full internal composition of a top-post,
 * which is saved in {@link Chat_Info} as parent entity, while also is saved in
 * {@link Chat_Reply} as child entity to leave a redundancy record.</li>
 * <li>Exists to address the issue of schema/logic antipattern in chat domain
 * until redesign.</li>
 * <li>Reference domain vocabulary in {@code docs/feature-flows/chat/99-extra-notes}
 * if the description above makes no sense.</li>
 **/
public final class ChatInfoWithRedundancy {
	private final Chat_Info chatInfo;
	private final Chat_Reply redundancy;


	public ChatInfoWithRedundancy(Chat_Info chatInfo, Chat_Reply redundancy) {
		this.chatInfo = chatInfo;
		this.redundancy = redundancy;
	}


	public Chat_Info getChatInfo() {
		return chatInfo;
	}

	public Chat_Reply getRedundancy() {
		return redundancy;
	}
}