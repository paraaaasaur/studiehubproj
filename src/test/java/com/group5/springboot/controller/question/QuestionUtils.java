package com.group5.springboot.controller.question;

import com.group5.springboot.model.question.Question_Info;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

public final class QuestionUtils {
	public static Question_Info aQuestion() {
		Question_Info question_Info = new Question_Info();
		// simulates 一鍵 behavior in question/insertQuestion.jsp
		{
			// mandatory
			question_Info.setQ_id(null);
			question_Info.setQ_class("日語");
			question_Info.setQ_type("聽力題");
			question_Info.setQ_question("明日、二人で映画に行きたいです。何と言いますか。");
			question_Info.setQ_selectionA("明日、映画に誘いましょう。");
			question_Info.setQ_selectionB("明日、映画を見に行きませんか。");
			question_Info.setQ_selectionC("明日、映画に行きたいそうですよ。");
			question_Info.setQ_selectionD("明日、映画の撮影を行きましょうか。");
			question_Info.setAnswers(new String[]{"B"});

			// optional
			question_Info.setMultipartFilePic(new MockMultipartFile("multipartFilePic", "mock-image.jpg", MediaType.IMAGE_JPEG_VALUE, "mock-image-content".getBytes()));
			question_Info.setMultipartFileAudio(new MockMultipartFile("multipartFileAudio", "mock-audio.mp4", "audio/mp3", "mock-audio-content".getBytes()));
		}

		return question_Info;
	}

	/** in courtesy of da almighty ChatGPT kun */
	public static Question_Info aQuestion2() {
		Question_Info question_Info = new Question_Info();
		{
			// mandatory
			question_Info.setQ_id(null);
			question_Info.setQ_class("日語");
			question_Info.setQ_type("聽力題");
			question_Info.setQ_question("駅までどうやって行きますか。");
			question_Info.setQ_selectionA("歩いて五分ぐらいです。");
			question_Info.setQ_selectionB("駅で待ち合わせします。");
			question_Info.setQ_selectionC("電車を見に行きます。");
			question_Info.setQ_selectionD("切符を買いたいです。");
			question_Info.setAnswers(new String[]{"A"});

			// optional
			question_Info.setMultipartFilePic(
					new MockMultipartFile("multipartFilePic", "mock-image2.jpg",
							MediaType.IMAGE_JPEG_VALUE, "mock-image-content-2".getBytes()));
			question_Info.setMultipartFileAudio(
					new MockMultipartFile("multipartFileAudio", "mock-audio2.mp4",
							"audio/mp3", "mock-audio-content-2".getBytes()));
		}

		return question_Info;
	}

	/** in courtesy of da almighty ChatGPT kun */
	public static Question_Info aQuestion3() {
		Question_Info question_Info = new Question_Info();
		{
			// mandatory
			question_Info.setQ_id(null);
			question_Info.setQ_class("日語");
			question_Info.setQ_type("聽力題");
			question_Info.setQ_question("今週の土曜日は暇ですか。");
			question_Info.setQ_selectionA("土曜日は雨が降ります。");
			question_Info.setQ_selectionB("はい、特に予定はありません。");
			question_Info.setQ_selectionC("毎週土曜日に行きます。");
			question_Info.setQ_selectionD("土曜日が一番忙しいです。");
			question_Info.setAnswers(new String[]{"B"});

			// optional
			question_Info.setMultipartFilePic(
					new MockMultipartFile("multipartFilePic", "mock-image3.jpg",
							MediaType.IMAGE_JPEG_VALUE, "mock-image-content-3".getBytes()));
			question_Info.setMultipartFileAudio(
					new MockMultipartFile("multipartFileAudio", "mock-audio3.mp4",
							"audio/mp3", "mock-audio-content-3".getBytes()));
		}

		return question_Info;
	}

	/** in courtesy of da almighty ChatGPT kun */
	public static Question_Info aQuestion4() {
		Question_Info question_Info = new Question_Info();
		{
			// mandatory
			question_Info.setQ_id(null);
			question_Info.setQ_class("日語");
			question_Info.setQ_type("聽力題");
			question_Info.setQ_question("この文章の内容と合っているものはどれですか。");
			question_Info.setQ_selectionA("学生は毎日図書館へ行きます。");
			question_Info.setQ_selectionB("図書館は日曜日に休みます。");
			question_Info.setQ_selectionC("図書館は夜八時まで開いています。");
			question_Info.setQ_selectionD("学生は図書館を利用できません。");
			question_Info.setAnswers(new String[]{"C"});

			// optional
			question_Info.setMultipartFilePic(
					new MockMultipartFile("multipartFilePic", "mock-image4.jpg",
							MediaType.IMAGE_JPEG_VALUE, "mock-image-content-4".getBytes()));
			question_Info.setMultipartFileAudio(
					new MockMultipartFile("multipartFileAudio", "mock-audio4.mp4",
							"audio/mp3", "mock-audio-content-4".getBytes()));
		}

		return question_Info;
	}

	public static Question_Info aRandomQuestionOfType(String q_type) {
		Question_Info question_Info = aQuestion();
		String noise = UUID.randomUUID().toString();
		{
			question_Info.setQ_question(noise);
			question_Info.setQ_type(q_type);
			question_Info.setMultipartFilePic(new MockMultipartFile("multipartFilePic", "mock-image" + noise + ".jpg", MediaType.IMAGE_JPEG_VALUE, ("mock-image-content-" + noise).getBytes()));
			question_Info.setMultipartFileAudio(new MockMultipartFile("multipartFileAudio", "mock-audio" + noise + ".mp4", "audio/mp3", ("mock-audio-content-" + noise).getBytes()));
		}

		return question_Info;
	}
}
