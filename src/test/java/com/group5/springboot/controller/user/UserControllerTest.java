package com.group5.springboot.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.utils.EmailSenderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Field;
import java.util.UUID;

import static com.group5.springboot.controller.user.UserTestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
	// reusable
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;
	@MockBean // sending a real email here takes 3 holy seconds so ^_^
	private EmailSenderService mockEmailSenderService;

	// renew per test
	private MockHttpSession mockHttpSession = new MockHttpSession();
	private User_Info joshua;


	@Autowired
	UserControllerTest(MockMvc mockMvc, GenericDao dao) {
		this.mockMvc = mockMvc;
		this.dao = dao;
		this.userTestUtils = new UserTestUtils(mockMvc);
	}


	@BeforeEach
	void setUp() {
		this.mockHttpSession = new MockHttpSession();
		// insert test user
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
	@DisplayName("GET /gotologin.controller - success")
	void gotoLoginPage_success() throws Exception {
		mockMvc.perform(get("/gotologin.controller"))

				.andExpect(view().name("user/login"));
	}

	@Test
	@DisplayName("GET /gotologin.controller - rejects user")
	void gotoLoginPage_whenUserLoggedIn_thenAccessIsDenied() throws Exception {
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(get("/gotologin.controller")
						.session(mockHttpSession))

				.andExpect(forwardedUrl("/"));
	}

	@Test
	@DisplayName("GET /gotosignup.controller - success")
	void gotoSignupPage_success() throws Exception {
		mockMvc.perform(get("/gotosignup.controller"))

				.andExpect(status().isOk())
				.andExpect(view().name("user/signup"));
	}

	@Test
	@DisplayName("GET /gotosignup.controller - rejects user")
	void gotoSignupPage_whenUserLoggedIn_thenAccessIsDenied() throws Exception {
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(get("/gotosignup.controller")
						.session(mockHttpSession))

				.andExpect(forwardedUrl("/"));
	}

	@Test
	@DisplayName("GET /gotoUpdateUserinfo.controller - success")
	void gotoUpdateUserinfo_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(get("/gotoUpdateUserinfo.controller")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("user/updateUser"));
	}

	@Test
	@DisplayName("GET /gotoUpdateUserinfo.controller - requires user")
	void gotoUpdateUserinfo_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/gotoUpdateUserinfo.controller"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("GET /gotoChangePassword.controller - success")
	void gotoChangePassword_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(get("/gotoChangePassword.controller")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("user/changePassword"));
	}

	@Test
	@DisplayName("GET /gotoChangePassword.controller - requires user")
	void gotoChangePassword_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/gotoChangePassword.controller"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("POST /login.controller - success")
	void login_success() throws Exception {
		// 0. prepare login creds as JSON
		Field rememberMe = User_Info.class.getDeclaredField("rememberMe");
		rememberMe.setAccessible(true);
		rememberMe.set(joshua, null);

		String reqJson = OBJECT_MAPPER.writeValueAsString(joshua);


		MvcResult mvcResult = mockMvc.perform(post("/login.controller")
						.contentType(APPLICATION_JSON)
						.content(reqJson))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.success").exists())
				.andExpect(jsonPath("$.fail").doesNotExist())
				.andExpect(jsonPath("$.loginBean.u_id").value("joshua"))
				.andReturn();

		assertNotNull(mvcResult.getRequest().getSession().getAttribute("loginBean"), "session attribute 'loginBean' should exist");
	}

	@Test
	@DisplayName("POST /login.controller - rejects user")
	void login_whenUserLoggedIn_thenAccessIsDenied() throws Exception {
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(post("/login.controller")
						.session(mockHttpSession))

				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("POST /login.controller - invalid credential")
	void login_whenInvalidCredential_thenRequestIsRejected() throws Exception {
		// 0. prepare login credentials as JSON
		Field rememberMe = User_Info.class.getDeclaredField("rememberMe");
		rememberMe.setAccessible(true);
		rememberMe.set(joshua, null);
		joshua.setU_psw(UUID.randomUUID().toString()); // invalid credential

		String reqJson = OBJECT_MAPPER.writeValueAsString(joshua);


		mockMvc.perform(post("/login.controller")
						.contentType(APPLICATION_JSON)
						.content(reqJson))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.fail").exists());
	}

	@Test
	@DisplayName("POST /checkUserId - success (available)")
	void checkUserId_success_available() throws Exception {
		mockMvc.perform(post("/checkUserId")
						.param("u_id", "bless-you") // available
						.contentType(APPLICATION_FORM_URLENCODED))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.u_id", is("")))
				.andExpect(jsonPath("$.u_id", not("帳號已存在")));
	}

	@Test
	@DisplayName("POST /checkUserId - success (id already exists)")
	void checkUserId_success_idAlreadyExists() throws Exception {
		mockMvc.perform(post("/checkUserId")
						.param("u_id", joshua.getU_id()) // id already exists
						.contentType(APPLICATION_FORM_URLENCODED))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.u_id", is("帳號已存在")))
				.andExpect(jsonPath("$.u_id", not("")));
	}

	@Test
	@DisplayName("POST /userSignup - success")
	void signup_success() throws Exception {
		User_Info newRandomUser = aRandomUser();
		String reqBody = OBJECT_MAPPER.writeValueAsString(newRandomUser);


		mockMvc.perform(post("/userSignup")
						.contentType(APPLICATION_JSON)
						.content(reqBody))

				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.success").exists());

		verify(mockEmailSenderService, times(1)).sendSimpleEmail(any(), any(), any());
	}

	@Test
	@DisplayName("POST /userSignup - rejects user")
	void signup_whenUserLoggedIn_thenAccessIsDenied() throws Exception {
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(post("/userSignup")
						.session(mockHttpSession))

				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("POST /userSignup - id already exists")
	void signup_whenIdAlreadyExists_thenRequestIsRejected() throws Exception {
		User_Info newRandomUser = aRandomUser();
		newRandomUser.setU_id(joshua.getU_id());
		String reqBody = OBJECT_MAPPER.writeValueAsString(newRandomUser);


		mockMvc.perform(post("/userSignup")
						.contentType(APPLICATION_JSON)
						.content(reqBody))

				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.fail").exists());

		verify(mockEmailSenderService, times(0)).sendSimpleEmail(any(), any(), any());
	}

	@Test
	@DisplayName("POST /changePassword.controller - success")
	void changePassword_success() throws Exception {
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(post("/changePassword.controller")
					.session(mockHttpSession)
					.contentType(APPLICATION_FORM_URLENCODED)
					.param("u_psw", "tasukaru")
					.param("cfm_psw", "tasukaru"))

				.andExpect(request().sessionAttribute("loginBean", mockHttpSession.getAttribute("loginBean")))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeCount(1))
				.andExpect(flash().attributeExists("successMessageOfChangingPassword"))
				.andExpect(redirectedUrl("/"));
	}

	@Test
	@DisplayName("POST /changePassword.controller - requires user")
	void changePassword_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(post("/changePassword.controller"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("POST /changePassword.controller - confirm-psw mismatch")
	void changePassword_whenConfirmPswMismatch_ThenRequestIsRejected() throws Exception {
		userTestUtils.loginAs(joshua, mockHttpSession);


		mockMvc.perform(post("/changePassword.controller")
						.session(mockHttpSession)
						.contentType(APPLICATION_FORM_URLENCODED)
						.param("u_psw", "honeybee")
						.param("cfm_psw", "wasp"))

				.andExpect(request().sessionAttribute("loginBean", mockHttpSession.getAttribute("loginBean")))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeCount(1))
				.andExpect(flash().attributeExists("errorMessageOfChangingPassword"))
				.andExpect(redirectedUrl("/gotoChangePassword.controller"));
	}

	@Test
	@DisplayName("POST /updateUserinfo.controller - success")
	void updateUser_success() throws Exception {
		// 0. prepare input and session
		userTestUtils.loginAs(joshua, mockHttpSession);

		User_Info loginBean = (User_Info) mockHttpSession.getAttribute("loginBean");
		String updateEmail = "i-love-jazz@gmail.com";
		String updateAddress = "Mars";
		MockMultipartFile updateProfilePic = new MockMultipartFile("uploadImage", "mock-user-profile-picture.jpg", IMAGE_JPEG_VALUE, "Glamorous meme content".getBytes());


		// 1. POST /updateUserinfo.controller
		MvcResult mvcResult = mockMvc.perform(multipart("/updateUserinfo.controller")
						.file(updateProfilePic)
						.contentType(APPLICATION_FORM_URLENCODED)
						.session(mockHttpSession)
						.param("u_lastname", loginBean.getU_lastname())
						.param("u_firstname", loginBean.getU_firstname())
						.param("u_email", updateEmail)
						.param("u_address", updateAddress)
				)

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("successMessage", "修改成功"))
				.andExpect(redirectedUrl("/gotoUpdateUserinfo.controller"))
				.andReturn();

		User_Info newInfo2 = (User_Info)mvcResult.getRequest().getSession(false).getAttribute("loginBean");
		assertNotNull(newInfo2, "session attribute 'loginBean' should exist");

		assertEquals("joshua", newInfo2.getU_id());
		assertEquals(updateEmail, newInfo2.getU_email());
		assertEquals(updateAddress, newInfo2.getU_address());
		assertEquals("Jo-An", newInfo2.getU_firstname());
		assertEquals("Sun", newInfo2.getU_lastname());


		// 2. follow redirect using the current session
		String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
		MockHttpSession session = (MockHttpSession) mvcResult.getRequest().getSession(false);

		assertNotNull(redirectedUrl);
		assertNotNull(session);

		mockMvc.perform(get(redirectedUrl).session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("user/updateUser"));
	}

	@Test
	@DisplayName("POST /updateUserinfo.controller - requires user")
	void updateUser_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(multipart("/updateUserinfo.controller"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("POST /updateUserinfo.controller - empty field")
	void updateUser_whenEmptyField_thenRequestIsRejected() throws Exception {
		// 0. prepare input and session
		userTestUtils.loginAs(joshua, mockHttpSession);


		// 1. POST /updateUserinfo.controller
		mockMvc.perform(multipart("/updateUserinfo.controller")
						.contentType(APPLICATION_FORM_URLENCODED)
						.session(mockHttpSession)
						.param("u_lastname", "")) // mandatory field empty

				.andExpect(view().name("user/updateUser"))
				.andExpect(model().errorCount(1));
	}
}