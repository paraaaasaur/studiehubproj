package com.group5.springboot.controller.user;

import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.utils.EmailSenderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.group5.springboot.controller.user.UserTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserfunctionControllerTest {
	// reusable
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;
	@MockBean // sending a real email here takes 3 holy seconds so ^_^
	private EmailSenderService mockEmailSenderService;

	// renew per test
	private MockHttpSession mockHttpSession = new MockHttpSession();
	private User_Info joshua;


	@Autowired
	UserfunctionControllerTest(MockMvc mockMvc, GenericDao dao) {
		this.mockMvc = mockMvc;
		this.dao = dao;
		this.userTestUtils = new UserTestUtils(mockMvc);
	}


	@BeforeEach
	void setUp() {
		this.mockHttpSession = new MockHttpSession();

		// set up fresh test data
		this.joshua = dao.save(aUserJoshua());
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		dao.deleteAll(User_Info.class);
	}

	@Test
	@DisplayName("GET /gotoForgetPassword.controller - success")
	void gotoForgetPassword_success() throws Exception {
		mockMvc.perform(get("/gotoForgetPassword.controller"))

				.andExpect(status().isOk())
				.andExpect(view().name("users/forgot-password"));
	}

	@Test
	@DisplayName("GET /gotoForgetPassword.controller - rejects user")
	void gotoForgetPassword_whenUserLoggedIn_ThenDeniesAccess() throws Exception {
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(get("/gotoForgetPassword.controller")
						.session(mockHttpSession))

				.andExpect(forwardedUrl("/"));
	}

	@Test
	@DisplayName("GET /logout.controller - success")
	void logout_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(joshua, mockHttpSession);


		// 1. main
		mockMvc.perform(get("/logout.controller")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.success").exists());

		assertTrue(mockHttpSession.isInvalid());
	}

	@Test
	@DisplayName("GET /logout.controller - requires user")
	void logout_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/logout.controller"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("POST /sendRandomPasswordToRegisteredEmail.controller - success")
	void resetPasswordAndSendEmail_success() throws Exception {
		// 0. prepare request
		String reqBody = String.format("{ \"u_email\" : \"%s\" }", joshua.getU_email());


		mockMvc.perform(post("/sendRandomPasswordToRegisteredEmail.controller")
						.contentType(APPLICATION_JSON)
						.content(reqBody))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.fail").doesNotExist())
				.andExpect(jsonPath("$.success").exists());

		verify(mockEmailSenderService, times(1)).sendSimpleEmail(any(), any(), any());
	}

	@Test
	@DisplayName("POST /sendRandomPasswordToRegisteredEmail.controller - rejects user")
	void resetPasswordAndSendEmail_whenUserLoggedIn_thenAccessIsDenied() throws Exception {
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(post("/sendRandomPasswordToRegisteredEmail.controller")
						.session(mockHttpSession))

				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("POST /sendRandomPasswordToRegisteredEmail.controller - email not found")
	void resetPasswordAndSendEmail_whenEmailNotFound_thenNotSendPswResetEmail() throws Exception {
		// 0. prepare request
		String reqBody = String.format("{ \"u_email\" : \"%s\" }", "email-not-found@protonmail.com");


		mockMvc.perform(post("/sendRandomPasswordToRegisteredEmail.controller")
						.contentType(APPLICATION_JSON)
						.content(reqBody))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.fail").exists());

		verify(mockEmailSenderService, never()).sendSimpleEmail(any(), any(), any());
	}
}