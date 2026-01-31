package com.group5.springboot.controller.question;

import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.question.Question_Info;
import com.group5.springboot.model.user.User_Info;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static com.group5.springboot.controller.question.QuestionUtils.*;
import static com.group5.springboot.controller.user.UserTestUtils.aUserNick;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class QuestionControllerTest {
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;

	private MockHttpSession mockHttpSession = new MockHttpSession();
	private User_Info nick;
	private Question_Info question1Approved;
	private Question_Info question1CreateRequest;
	private Question_Info question2;
	private Question_Info question3;


	@Autowired
	QuestionControllerTest(MockMvc mockMvc, GenericDao dao) {
		this.mockMvc = mockMvc;
		this.dao = dao;
		this.userTestUtils = new UserTestUtils(mockMvc);
	}


	@BeforeEach
	void setUp() throws IOException {
		this.mockHttpSession = new MockHttpSession();

		// prepare fresh test data
		this.nick = dao.save(aUserNick());
		this.question1CreateRequest = aQuestion();
		this.question1Approved = dao.saveQuestionButSkipExtStorage(question1CreateRequest);
		dao.adminApprovesQuestion(question1Approved);
		this.question2 = dao.saveQuestionButSkipExtStorage(aQuestion2());
		this.question3 = dao.saveQuestionButSkipExtStorage(aQuestion3());
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		this.question1CreateRequest = null;
		dao.deleteAll(Question_Info.class);
		dao.deleteAll(User_Info.class);
	}


	@Test
	@DisplayName("GET /question.controller/turnQuestionIndex")
	void turnQuestionIndex() throws Exception {
		mockMvc.perform(get("/question.controller/turnQuestionIndex"))

				.andExpect(status().isOk())
				.andExpect(view().name("question/intro_QuestionIndex"));
	}

	@Test
	@DisplayName("GET /question.controller/insertQuestion - success")
	void sendInsertQuestion_success() throws Exception {
		userTestUtils.loginAs(nick, mockHttpSession);


		mockMvc.perform(get("/question.controller/insertQuestion")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("question/insertQuestion"));
	}

	@Test
	@DisplayName("POST /question.controller/insertQuestion - success")
	void saveQuestion_success() throws Exception {
		userTestUtils.loginAs(nick, mockHttpSession);


		Question_Info newQuestion = aQuestion4();
		mockMvc.perform(multipart("/question.controller/insertQuestion")
						.file((MockMultipartFile) newQuestion.getMultipartFilePic())
						.file((MockMultipartFile) newQuestion.getMultipartFileAudio())
						.contentType(MULTIPART_FORM_DATA)
						.session(mockHttpSession)
						.param("q_class", newQuestion.getQ_class())
						.param("q_type", newQuestion.getQ_type())
						.param("q_question", newQuestion.getQ_question())
						.param("q_selectionA", newQuestion.getQ_selectionA())
						.param("q_selectionB", newQuestion.getQ_selectionB())
						.param("q_selectionC", newQuestion.getQ_selectionC())
						.param("q_selectionD", newQuestion.getQ_selectionD())
						.param("q_answer", newQuestion.getQ_answer()))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("successMessage", notNullValue()))
				.andExpect(redirectedUrl("/question.controller/guestQueryQuestion"));
	}

	@Test
	@DisplayName("POST /question.controller/insertQuestion - empty field")
	void saveQuestion_whenEmptyField_thenRequestIsRejected() throws Exception {
		userTestUtils.loginAs(nick, mockHttpSession);


		Question_Info newQuestion = aQuestion4();
		mockMvc.perform(multipart("/question.controller/insertQuestion")
						.file((MockMultipartFile) newQuestion.getMultipartFilePic())
						.file((MockMultipartFile) newQuestion.getMultipartFileAudio())
						.contentType(MULTIPART_FORM_DATA)
						.session(mockHttpSession)
						.param("q_class", newQuestion.getQ_class())
						.param("q_type", newQuestion.getQ_type())
						.param("q_question", "") // empty mandatory field
						.param("q_selectionA", newQuestion.getQ_selectionA())
						.param("q_selectionB", newQuestion.getQ_selectionB())
						.param("q_selectionC", newQuestion.getQ_selectionC())
						.param("q_selectionD", newQuestion.getQ_selectionD())
						.param("q_answer", newQuestion.getQ_answer()))

				.andExpect(view().name("question/insertQuestion"))
				.andExpect(model().errorCount(1));
	}

	@Test
	@DisplayName("GET /question.controller/guestQueryQuestion")
	void sendGuestQueryQuestion() throws Exception {
		mockMvc.perform(get("/question.controller/guestQueryQuestion"))

				.andExpect(status().isOk())
				.andExpect(view().name("question/guestQueryQuestion"));
	}

	@Test
	@DisplayName("GET /question.controller/guestOneQuestion/{q_id}")
	void guestOneQuestion() throws Exception {
		long q_id = question1Approved.getQ_id();
		mockMvc.perform(get("/question.controller/guestOneQuestion/{q_id}", q_id))

				.andExpect(status().isOk())
				.andExpect(model().attribute("Q1", isA(Question_Info.class)))
				.andExpect(view().name("question/guestOneQuestion"));
	}

	@Test
	@DisplayName("GET /question.controller/queryQuestion - success")
	void sendQueryQuestion_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/question.controller/queryQuestion")
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("question/queryQuestion"));
	}

	@Test
	@DisplayName("GET /question.controller/findAllQuestions")
	void findAllQuestions() throws Exception {
		mockMvc.perform(get("/question.controller/findAllQuestions"))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", aMapWithSize(2)))
				.andExpect(jsonPath("$.size", is(1)))
				.andExpect(jsonPath("$.list[*].q_id", hasItem(question1Approved.getQ_id().intValue())))
				.andExpect(jsonPath("$.list[*].q_id", not(hasItem(question2.getQ_id().intValue()))))
				.andExpect(jsonPath("$.list[*].q_id", not(hasItem(question3.getQ_id().intValue()))));
	}

	@Test
	@DisplayName("GET /question.controller/queryByName")
	void queryByName() throws Exception {
		final String partialQname = question1Approved.getQ_question().substring(1);
		mockMvc.perform(get("/question.controller/queryByName")
						.queryParam("qname", partialQname))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", aMapWithSize(2)))
				.andExpect(jsonPath("$.size", is(1)))
				.andExpect(jsonPath("$.list[*].q_question", hasItem(question1Approved.getQ_question())));
	}

	@Test
	@DisplayName("GET /question.controller/modifyQuestion/{q_id} - success")
	void sendEditPage_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		long q_id = question1Approved.getQ_id();
		mockMvc.perform(get("/question.controller/modifyQuestion/{q_id}", q_id)
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(model().attributeExists("Q1"))
				.andExpect(view().name("question/editQuestion"));
	}

	@Test
	@DisplayName("POST /question.controller/modifyQuestion/{q_id} - success")
	void updateQuestion_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		long q_id = question1Approved.getQ_id();
		mockMvc.perform(multipart("/question.controller/modifyQuestion/{q_id}", q_id)
						.file((MockMultipartFile) question1CreateRequest.getMultipartFilePic())
						.file((MockMultipartFile) question1CreateRequest.getMultipartFileAudio())
						.param("q_class", question1CreateRequest.getQ_class())
						.param("q_type", question1CreateRequest.getQ_type())
						.param("q_question", "UPDATED QUESTION( ^)o(^ )")
						.param("q_selectionA", question1CreateRequest.getQ_selectionA())
						.param("q_selectionB", question1CreateRequest.getQ_selectionB())
						.param("q_selectionC", question1CreateRequest.getQ_selectionC())
						.param("q_selectionD", question1CreateRequest.getQ_selectionD())
						.param("q_answer", question1CreateRequest.getQ_answer())
						.contentType(MULTIPART_FORM_DATA)
						.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/question.controller/queryQuestion"));
	}

	@Test
	@DisplayName("POST /question.controller/modifyQuestion/{q_id} - empty field")
	void updateQuestion_whenEmptyField_thenRequestIsRejected() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		long q_id = question1Approved.getQ_id();
		mockMvc.perform(multipart("/question.controller/modifyQuestion/{q_id}", q_id)
						.file((MockMultipartFile) question1CreateRequest.getMultipartFilePic())
						.file((MockMultipartFile) question1CreateRequest.getMultipartFileAudio())
						.param("q_class", question1CreateRequest.getQ_class())
						.param("q_type", question1CreateRequest.getQ_type())
						.param("q_question", "") // missing mandatory empty
						.param("q_selectionA", question1CreateRequest.getQ_selectionA())
						.param("q_selectionB", question1CreateRequest.getQ_selectionB())
						.param("q_selectionC", question1CreateRequest.getQ_selectionC())
						.param("q_selectionD", question1CreateRequest.getQ_selectionD())
						.param("q_answer", question1CreateRequest.getQ_answer())
						.contentType(MULTIPART_FORM_DATA)
						.session(mockHttpSession))

				.andExpect(view().name("question/editQuestion"))
				.andExpect(model().errorCount(1));
	}

	@Test
	@DisplayName("GET /question.controller/deleteQuestion/{q_id} - success")
	void deleteEditPage_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		long q_id = question1Approved.getQ_id();
		mockMvc.perform(get("/question.controller/deleteQuestion/{q_id}", q_id)
						.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("successMessage", notNullValue()))
				.andExpect(redirectedUrl("/question.controller/queryQuestion"));
	}

	@Test
	@DisplayName("GET /question.controller/sendRandomMixExam")
	void sendRandomMixExam() throws Exception {
		// 0. prepare test data
		for (int i = 0; i < 10; i++) {
			dao.saveQuestionButSkipExtStorage(aRandomQuestionOfType("聽力題"));
			dao.saveQuestionButSkipExtStorage(aRandomQuestionOfType("多選題"));
			dao.saveQuestionButSkipExtStorage(aRandomQuestionOfType("單選題"));
		}

		mockMvc.perform(get("/question.controller/sendRandomMixExam"))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$", aMapWithSize(2)))
				.andExpect(jsonPath("$.size", is(10)))
				.andExpect(jsonPath("$.list[?(@.q_type == '聽力題')]", hasSize(4)))
				.andExpect(jsonPath("$.list[?(@.q_type == '多選題')]", hasSize(3)))
				.andExpect(jsonPath("$.list[?(@.q_type == '單選題')]", hasSize(3)));
	}

	@Test
	@DisplayName("GET /question.controller/startRandomMixExam")
	void startRandomMixExam() throws Exception {
		mockMvc.perform(get("/question.controller/startRandomMixExam"))

				.andExpect(status().isOk())
				.andExpect(view().name("question/examMixQuestion"));
	}

	@Test
	@DisplayName("GET /question.controller/intoVerifyQuestion - success")
	void intoVerifyQuestion_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/question.controller/intoVerifyQuestion")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("question/verifyQuestion"));
	}

	@Test // returns JSON of questions where verification = 'N'
	@DisplayName("GET /question.controller/sendVerifyQuestion - success")
	void sendVerifyQuestion_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/question.controller/sendVerifyQuestion")
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", aMapWithSize(2)))
				.andExpect(jsonPath("$.size", is(2)))
				.andExpect(jsonPath("$.list[*].q_id", not(hasItem(question1Approved.getQ_id().intValue()))))
				.andExpect(jsonPath("$.list[*].q_id", hasItem(question2.getQ_id().intValue())))
				.andExpect(jsonPath("$.list[*].q_id", hasItem(question3.getQ_id().intValue())));
	}

	@Test
	@DisplayName("GET /question.controller/verifyPassQuestion/{q_id} - success")
	void verifyPassQuestion_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		long q_id = question2.getQ_id();
		mockMvc.perform(get("/question.controller/verifyPassQuestion/{q_id}", q_id)
				.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/question.controller/intoVerifyQuestion"));
	}

	@Test
	@DisplayName("GET /question.controller/verifyDeleteQuestion/{q_id} - success")
	void verifydeleteEditPage_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		long q_id = question2.getQ_id();
		mockMvc.perform(get("/question.controller/verifyDeleteQuestion/{q_id}", q_id)
				.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/question.controller/intoVerifyQuestion"));
	}

	@Test
	@DisplayName("GET /question.controller/verifyOneQuestion/{q_id} - success")
	void verifyOneQuestion_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		long q_id = question2.getQ_id();
		MvcResult mvcResult = mockMvc.perform(get("/question.controller/verifyOneQuestion/{q_id}", q_id)
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(model().attributeExists("Q1"))
				.andExpect(view().name("question/verifyOneQuestion"))
				.andReturn();

		Question_Info q1 = (Question_Info) mvcResult.getModelAndView().getModel().get("Q1");
		assertEquals(q_id, q1.getQ_id());
	}
}