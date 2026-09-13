package com.group5.springboot.controller.event;

import com.group5.springboot.dto.event.CreateEventForm;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public final class EventTestUtils {
	/**
	 * <li>A demo request DTO with hardcoded values. </li>
	 * <li>name: GoogleAnalytics數據分析</li>
	 * <li>registration period: 2025-06-07T15:00 - 2045-10-01T17:00</li>
	 * <li>event period: 2065-10-02T08:50 - 2085-10-12T17:10</li>
	 **/
	public static CreateEventForm anEventInfo() {
		return new CreateEventForm(
				"GoogleAnalytics數據分析",
				"線下課程",
				"2025-06-07T15:00",
				"2045-10-01T17:00",
				"2065-10-02T08:50",
				"2085-10-12T17:10",
				"桃園市桃園區埔新路12號",
				"在使用Google Analytics你是否曾出現過以下疑問 這些數據，真的能監控網站與分析轉換率，改變訂單成交數量嗎？我要怎麼靠數據了解顧客？哪些數據可以讓我瞭解客戶的行為與需求？數據裡的名詞代表意義是什麼,"
				+ "這些問題讓創新未來學校來幫你,創新未來學校設計的課程將Google Analytics分析的應用know-how，歸納整理一套完整的操作流程與即學即用的實務方法，協助解決提高網站流量與購買轉換率的核心難題！"
				+ "七大學習重點,Google廣告類型,1.關鍵字廣告運用的範圍,2.廣告競價機制模式,3.競業關鍵字與自有品牌關鍵字操作,4.廣告操作介面說明,5.廣告活動架構與廣告建立,6.廣告報表分析及廣告優化。",
				5,
				new MockMultipartFile(
						"eventImage",
						"mock-event-image.png",
						MediaType.IMAGE_PNG_VALUE,
						"mock-event-image-content".getBytes()
				)
		);
	}

	/**
	 * <li>A demo#2 request DTO with hardcoded values. </li>
	 * <li>name: "人生重來槍：小叮噹的贈禮</li>
	 * <li>registration period: 2015-06-07T15:00 - 2045-07-18T17:00</li>
	 * <li>event period: 2055-07-19T08:50 - 2085-07-21T17:10</li>
	 **/
	public static CreateEventForm anEventInfo2() {
		return new CreateEventForm(
				"人生重來槍：小叮噹的贈禮",
				"線下課程",
				"2015-06-07T15:00",
				"2045-07-18T17:00",
				"2055-07-19T08:50",
				"2085-07-21T17:10",
				"木星市彗星坑區蟲洞路77號",
				"現場示範來自22世紀最偉大的發明",
				1,
				new MockMultipartFile(
						"eventImage",
						"mock-event-image2.jpg",
						MediaType.IMAGE_JPEG_VALUE,
						"mock-event2-image-content".getBytes()
				)
		);
	}

	/**
	 * <li>A demo#3 request DTO with hardcoded values. </li>
	 * <li>name: 指尖的平衡感：寫程式的任督二脈</li>
	 * <li>registration period: 2005-06-07T15:00 - 2045-07-18T17:00</li>
	 * <li>event period: 2055-07-19T08:50 - 2095-07-21T17:10</li>
	 **/
	public static CreateEventForm anEventInfo3() {
		 return new CreateEventForm(
				"指尖的平衡感：寫程式的任督二脈",
				"線下課程",
				"2005-06-07T15:00",
				"2045-07-18T17:00",
				"2055-07-19T08:50",
				"2095-07-21T17:10",
				"水星市烈陽坑區銀河路1號",
				"透與太陽對話升級作為程式設計師的內功",
				3,
				new MockMultipartFile(
						"eventImage",
						"mock-event-image3.jpg",
						MediaType.IMAGE_GIF_VALUE,
						"mock-event3-image-content".getBytes()
				)
		 );
	}

	/**
	 * <li>A demo#4 request DTO with hardcoded values. </li>
	 * <li>name: 你所不知道的檸檬汁的50種妙用</li>
	 * <li>registration period: 2000-06-07T15:00 - 2040-07-18T17:00</li>
	 * <li>event period: 2050-07-19T08:50 - 2080-07-21T17:10</li>
	 **/
	public static CreateEventForm anEventInfo4() {
		return new CreateEventForm(
			"你所不知道的檸檬汁的50種妙用",
			"線下課程",
			"2000-06-07T15:00",
			"2040-07-18T17:00",
			"2050-07-19T08:50",
			"2080-07-21T17:10",
			"西紅市避難區麋路25號",
			"未定",
			3,
			new MockMultipartFile(
					"eventImage",
					"mock-event-image4.jpg",
					MediaType.IMAGE_PNG_VALUE,
					"mock-event4-image-content".getBytes()
			)
		);
	}

	public static CreateEventForm aDueEventInfo1() {
		return new CreateEventForm(
			"你所不知道的檸檬汁的50種妙用",
			"線下課程",
			"2000-06-07T15:00",
			"2001-07-18T17:00",
			"2050-07-19T08:50",
			"2080-07-21T17:10",
			"西紅市避難區麋路25號",
			"未定",
			3,
			new MockMultipartFile(
					"eventImage",
					"mock-event-image4.jpg",
					MediaType.IMAGE_PNG_VALUE,
					"mock-event4-image-content".getBytes()
			)
		);
	}
}