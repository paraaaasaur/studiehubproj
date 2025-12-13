package com.group5.springboot.controller.chat;

import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;

import java.util.*;

public final class ChatTestUtils {
	public static Chat_Info aChatInfo() {
		Chat_Info chatInfo = new Chat_Info();
		{
			chatInfo.setC_Title("我總算知道println可以做什麼了");
			chatInfo.setC_Class("Java");
			chatInfo.setC_Conts(
					"你們知道嗎\r\n" +
					"println可以物件變成字串，還可以和int相加\r\n" +
					"真的很神奇，從名字裡你感覺不出他是這種功能\r\n" +
					"對了，有人知道到底要怎麼在console上印下一行嗎\r\n" +
					"找了好久都找不到這功能，怎麼會這樣"
			);
		}

		return chatInfo;
	}

	public static Chat_Info aChatInfo2() {
		Chat_Info chatInfo = new Chat_Info();
		{
			chatInfo.setC_Title("麻婆豆腐鮭魚義大利麵");
			chatInfo.setC_Class("法式料理");
			chatInfo.setC_Conts(
					"這是我最近新開發的料理\r\n" +
					"製作跨時代和文化的廚藝結晶非常不容易\r\n" +
					"但是我想推廣料理的不同面向\r\n" +
					"所以歡迎各位嘗試，記得幫說食譜是源自這裡就好\r\n" +
					"不用客氣，只是記得要加很多花椒，不然很難體現法式風味的精髓"
			);
		}

		return chatInfo;
	}

	public static Chat_Reply aRandomChatReply() {
		Chat_Reply chatReply = new Chat_Reply();
		{
			chatReply.setC_Conts(getRandomReply());
		}

		return chatReply;
	}


	// powered by the humor of ChatGPT
	public static final List<String> replies = new ArrayList<>(List.of(
			"你試過把 println 的 ln 拔掉嗎？那就真的印不出下一行了。",
			"Java笑你",
			"console：我可以印下一行，但你得先跟我講清楚你想要怎樣。",
			"你要不要試看看printlnlnln？也許可以印三行",
			"print不能印下一行？那你試過按enter嗎？",
			"println：我明明就叫你跳行，是你不信任我。",
			"如果名字取成printAndGoNextLine你會信嗎",
			"別找了，印下一行的功能被 println 偷走了。",
			"Java：命名靠靈感，理解靠緣分。",
			"古人換行都需要 \n、\r\n 或者燒香拜JVM",
			"其實print也能換行，只是它懶，要你自己附上一個 \\n。",
			"你找不到功能是對的，因為他就長得不像能換行的樣子",
			"println不跳行？你告訴我講今天看到飛碟我可能比較相信",
			"其實還有 print() + \"\\n\" 這招",
			"我猜println本來想叫printPleaseButAlsoNewLine",
			"友善提示：找到功能之前，先找到您心中的愛與耐心",
			"那你要不要試試我的蘆薈蓮藕蒙布朗",
			"法式料理？我記得那個應該算是英式料理..."
	));

	private static String getRandomReply() {
		Collections.shuffle(replies);
		return replies.get(0) + UUID.randomUUID();
	}
}