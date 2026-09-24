package com.group5.springboot.controller.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.dto.chat.CreateReplyRequest;
import com.group5.springboot.model.chat.Chat_Info;
import com.group5.springboot.model.chat.Chat_Reply;
import com.group5.springboot.model.chat.scaffolding.dev.ChatInfoWithRedundancy;
import com.group5.springboot.model.chat.scaffolding.dev.PostWithPoster;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.chat.ChatService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.group5.springboot.controller.chat.ChatTestUtils.*;
import static com.group5.springboot.controller.user.UserTestUtils.aUserTajenwww;
import static com.group5.springboot.controller.user.UserTestUtils.aUserYen;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChatControllerTest {
	private final MockMvc mockMvc;
	private final UserTestUtils userTestUtils;
	private final ObjectMapper objectMapper;
	private final GenericDao dao;
	private final ChatService chatService;

	private MockHttpSession mockHttpSession = new MockHttpSession();

	private User_Info yen;
	private User_Info tajenwww;
	private Chat_Info chatInfo1;
	private Chat_Reply chatInfo1Redundancy;
	private Chat_Reply chatReply1;
	private Chat_Reply chatReply2;


	@Autowired
	ChatControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, GenericDao dao, ChatService chatService) {
		this.mockMvc = mockMvc;
		this.userTestUtils = new UserTestUtils(mockMvc);
		this.objectMapper = objectMapper;
		this.dao = dao;
		this.chatService = chatService;
	}


	@BeforeEach
	void setUp() {
		mockHttpSession = new MockHttpSession();

		// set up test data
		this.yen = dao.save(aUserYen());
		this.tajenwww = dao.save(aUserTajenwww());
		chatService.insertTopPostAndRedundancy(yen.getU_id(), aTopPost());
		ChatInfoWithRedundancy saved = dao.findTopPostAndRedundancy(aTopPost().getC_Title());
		this.chatInfo1 = saved.getChatInfo();
		this.chatInfo1Redundancy = saved.getRedundancy();
		this.chatReply1 = dao.saveReply(tajenwww.getU_id(), aRandomReplyTo(chatInfo1.getC_ID()));
		this.chatReply2 = dao.saveReply(tajenwww.getU_id(), aRandomReplyTo(chatInfo1.getC_ID()));
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		dao.deleteAll(Chat_Reply.class);
		dao.deleteAll(Chat_Info.class);
		dao.deleteAll(User_Info.class);
	}

	@Test
	@DisplayName("GET /goSelectAllChat")
	void goSelectAllChat() throws Exception {
		mockMvc.perform(get("/goSelectAllChat"))

				.andExpect(status().isOk())
				.andExpect(view().name("chat/threads/list"));
	}

	@Test
	@DisplayName("GET /goSelectAllChatAdmin - success")
	void goSelectAllChatAdmin_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/goSelectAllChatAdmin")
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("chat/threads/admin/list"));
	}

	@Test
	@DisplayName("GET /goSelectAllChatAdmin - requires admin")
	void goSelectAllChatAdmin_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/goSelectAllChatAdmin"))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /goSelectOneChat/{c_ID}")
	void goSelectOneChat() throws Exception {
		final int c_ID = chatInfo1.getC_ID();
		mockMvc.perform(get("/goSelectOneChat/{c_ID}", c_ID))

				.andExpect(status().isOk())
				.andExpect(model().attribute("c_ID", is(c_ID)))
				.andExpect(view().name("chat/threads/detail"));
	}

	@Test
	@DisplayName("GET /goInsertChat - success")
	void gotoInsertChat_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(yen, mockHttpSession);


		mockMvc.perform(get("/goInsertChat")
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("chat/threads/add"));
	}

	@Test
	@DisplayName("GET /goInsertChat - requires user")
	void gotoInsertChat_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/goInsertChat"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("GET /goDeleteChatAdmin/{c_ID} - success")
	void goDeleteChatAdmin_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		final int c_ID = chatInfo1.getC_ID();
		mockMvc.perform(get("/goDeleteChatAdmin/{c_ID}", c_ID)
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(model().attribute("c_ID", is(c_ID)))
				.andExpect(view().name("chat/threads/admin/delete"));
	}

	@Test
	@DisplayName("GET /goDeleteChatAdmin/{c_ID} - requires admin")
	void goDeleteChatAdmin_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/goDeleteChatAdmin/{c_ID}", chatInfo1.getC_ID()))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /goUpdateChat/{c_ID} - success")
	void updateChat_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(yen, mockHttpSession);


		// 1. main
		mockMvc.perform(get("/goUpdateChat/{c_ID}", chatInfo1Redundancy.getC_ID())
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(model().attribute("updatePostForm", notNullValue(Chat_Reply.class)))
				.andExpect(view().name("chat/threads/edit-post"));
	}

	@Test
	@DisplayName("GET /goUpdateChat/{c_ID} - requires user")
	void updateChat_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/goUpdateChat/{c_ID}", chatInfo1Redundancy.getC_ID()))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("GET /selectSingleChat/{c_ID}")
	void selectChatById() throws Exception {
		final int c_ID = chatInfo1.getC_ID();
		mockMvc.perform(get("/selectSingleChat/{c_ID}", c_ID))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.c_ID", is(c_ID)))
				.andExpect(jsonPath("$.c_Conts", is(chatInfo1.getC_Conts())));
	}

	@Test
	@DisplayName("GET /selectAllChat")
	void findAllChat() throws Exception {
		mockMvc.perform(get("/selectAllChat"))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$..c_ID", hasItem(chatInfo1.getC_ID())))
				.andExpect(jsonPath("$..c_Conts", hasItem(chatInfo1.getC_Conts())));
	}

	@Test
	@DisplayName("GET /selectAllChatAdmin - success")
	void findAllChatAdmin_success() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/selectAllChatAdmin")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$..c_ID", hasItem(chatInfo1.getC_ID())))
				.andExpect(jsonPath("$..c_Conts", hasItem(chatInfo1.getC_Conts())));
	}

	@Test
	@DisplayName("GET /selectAllChatAdmin - requires admin")
	void findAllChatAdmin_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/selectAllChatAdmin"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /selectOneChat/{c_ID}")
	void findOneChat() throws Exception {
		final int c_ID = chatInfo1.getC_ID();
		PostWithPoster[] arr = dao.findPostsWithPosters(c_ID);
		mockMvc.perform(get("/selectOneChat/{c_ID}", c_ID))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$[0][0].c_ID", is(arr[0].getChat_Reply().getC_ID())))
				.andExpect(jsonPath("$[0][0].c_IDr", is(arr[0].getChat_Reply().getC_IDr())))
				.andExpect(jsonPath("$[0][1].u_id", is(arr[0].getUser_Info().getU_id())))
				.andExpect(jsonPath("$[1][0].c_ID", is(arr[1].getChat_Reply().getC_ID())))
				.andExpect(jsonPath("$[1][0].c_IDr", is(arr[1].getChat_Reply().getC_IDr())))
				.andExpect(jsonPath("$[1][1].u_id", is(arr[1].getUser_Info().getU_id())));
	}

	@Test
	@DisplayName("POST /insertChat - success")
	void InsertChat_success() throws Exception {
		// 0. login + prepare insert data
		userTestUtils.loginAs(yen, mockHttpSession);
		var newTopPost = aTopPost2();
		var reqObj = Map.of(
				"c_Date", newTopPost.getC_Date(),
				"c_Class", newTopPost.getC_Class(),
				"c_Title", newTopPost.getC_Title(),
				"c_Conts", newTopPost.getC_Conts(),
				"u_ID", yen.getU_id() // redundant; to be removed in 2.0.0
		);
		String reqBody = objectMapper.writeValueAsString(reqObj);


		mockMvc.perform(post("/insertChat")
						.contentType(APPLICATION_JSON)
						.content(reqBody)
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.success", notNullValue(String.class)));
	}

	@Test
	@DisplayName("POST /insertChat - requires user")
	void InsertChat_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(post("/insertChat"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("POST /insertChatReply - success")
	void InsertChatReply_success() throws Exception {
		// 0. login + prepare reply data
		userTestUtils.loginAs(yen, mockHttpSession);

		CreateReplyRequest newReply = aRandomReplyTo(chatInfo1.getC_ID());
		var reqObj = Map.of(
				"c_IDr", newReply.getC_IDr(),
				"c_Date", newReply.getC_Date(),
				"c_Conts", newReply.getC_Conts(),
				"u_ID", yen.getU_id() // redundant; to be removed in 2.0.0
		);


		// 1. main
		String reqBody = objectMapper.writeValueAsString(reqObj);
		mockMvc.perform(post("/insertChatReply")
				.session(mockHttpSession)
				.contentType(APPLICATION_JSON)
				.content(reqBody))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.success", notNullValue(String.class)));
	}

	@Test
	@DisplayName("POST /insertChatReply - requires user")
	void InsertChatReply_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(post("/insertChatReply"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("DELETE /deleteChatAdmin/{c_ID} - success")
	void deleteChatAdmin_success() throws Exception {
		// 0. admin-login + prepare thread to delete
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		int c_ID = chatInfo1.getC_ID();
		mockMvc.perform(delete("/deleteChatAdmin/{c_ID}", c_ID)
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.success", notNullValue(String.class)));
	}

	@Test
	@DisplayName("DELETE /deleteChatAdmin/{c_ID} - requires admin")
	void deleteChatAdmin_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(delete("/deleteChatAdmin/{c_ID}", chatInfo1.getC_ID()))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("POST /goUpdateChat/{c_ID} - success")
	void updateChatReply_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(yen, mockHttpSession);


		// 1. main
		String newContent = "請問聖誕老人到底是哪國傳來的啊";
		mockMvc.perform(post("/goUpdateChat/{c_ID}", chatInfo1Redundancy.getC_ID())
						.session(mockHttpSession)
						.param("c_IDr", chatInfo1Redundancy.getC_IDr() + "")
						.param("c_Date", chatInfo1Redundancy.getC_Date())
						.param("U_ID", chatInfo1Redundancy.getU_ID()) // redundant; to be removed in 2.0.0
						.param("c_Conts", newContent))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/goSelectOneChat/" + chatInfo1Redundancy.getC_IDr()));
	}

	@Test
	@DisplayName("POST /goUpdateChat/{c_ID} - requires user")
	void updateChatReply_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(post("/goUpdateChat/{c_ID}", chatInfo1Redundancy.getC_ID()))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("POST /goUpdateChat/{c_ID} - empty field")
	void updateChatReply_whenEmptyField_thenRequestIsRejected() throws Exception {
		// 0. login
		userTestUtils.loginAs(yen, mockHttpSession);


		// 1. main
		mockMvc.perform(post("/goUpdateChat/{c_ID}", chatInfo1Redundancy.getC_ID())
						.session(mockHttpSession)
						.param("c_IDr", chatInfo1Redundancy.getC_IDr() + "")
						.param("c_Date", chatInfo1Redundancy.getC_Date())
						.param("U_ID", chatInfo1Redundancy.getU_ID()) // redundant; to be removed in 2.0.0
						.param("c_Conts", "")) // empty required field

				.andExpect(model().attribute("updatePostForm", notNullValue()))
				.andExpect(model().attributeHasFieldErrors("updatePostForm", "c_Conts"))
				.andExpect(model().errorCount(1))
				.andExpect(view().name("chat/threads/edit-post"));
	}
}