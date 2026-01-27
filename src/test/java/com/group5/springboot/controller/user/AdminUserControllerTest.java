package com.group5.springboot.controller.user;

import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.user.User_Info;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.group5.springboot.controller.user.UserTestUtils.aUserJoshua;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminUserControllerTest {
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;

	private MockHttpSession mockHttpSession;
	private User_Info joshua;


	@Autowired
	AdminUserControllerTest(MockMvc mockMvc, GenericDao dao) {
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
	@DisplayName("GET /gotoAdminIndex.controller - success")
	void adminIndex_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/gotoAdminIndex.controller")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("adminIndex"));
	}

	@Test
	@DisplayName("GET /gotoAdminIndex.controller - requires admin")
	void adminIndex_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/gotoAdminIndex.controller"))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /gotoAdminLogin.controller - success")
	void gotoAdminLoginPage_success() throws Exception {
		mockMvc.perform(get("/gotoAdminLogin.controller"))

				.andExpect(status().isOk())
				.andExpect(view().name("user/adminLogin"));
	}

	@Test
	@DisplayName("GET /gotoAdminLogin.controller - rejects admin")
	void gotoAdminLoginPage_whenAdminLoggedIn_thenAccessIsDenied() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/gotoAdminLogin.controller")
						.session(mockHttpSession))

				.andExpect(forwardedUrl("/gotoAdminIndex.controller"));
	}

	@Test
	@DisplayName("GET /gotoShowAllUser.controller - success")
	void gotoShowAllUser_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/gotoShowAllUser.controller")
					.session(mockHttpSession))
				.andExpect(status().isOk())
				.andExpect(view().name("user/showAllUser"));
	}

	@Test
	@DisplayName("GET /gotoShowAllUser.controller - requires admin")
	void gotoShowAllUser_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/gotoShowAllUser.controller"))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("POST /AdminLogin.controller - success")
	void adminLogin_success() throws Exception {
		String[] adminCreds = {"adming5", "manager"};

		MvcResult mvcResult = mockMvc.perform(post("/AdminLogin.controller")
						.contentType(APPLICATION_FORM_URLENCODED)
						.param("id", adminCreds[0])
						.param("psw", adminCreds[1]))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attribute("success", is("管理員登入成功")))
				.andExpect(request().sessionAttribute("adminId", is("adming5")))
				.andExpect(redirectedUrl("/gotoAdminIndex.controller"))
				.andReturn();

		String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
		MockHttpSession session = (MockHttpSession) mvcResult.getRequest().getSession(false);

		assertNotNull(redirectedUrl);
		assertNotNull(session);

		// follow redirect using the current session
		mockMvc.perform(get(redirectedUrl).session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("adminIndex"));
	}

	@Test
	@DisplayName("POST /AdminLogin.controller - rejects admin")
	void adminLogin_whenAdminLoggedIn_thenAccessIsDenied() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(post("/AdminLogin.controller")
						.session(mockHttpSession))

				.andExpect(forwardedUrl("/gotoAdminIndex.controller"));
	}

	@Disabled("todo@1.0.1: fix inline session logic before enabling this test")
	@Test
	@DisplayName("GET /adminLogout.controller - success")
	void adminLogout_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/adminLogout.controller")
						.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		assertTrue(mockHttpSession.isInvalid());
	}

	@Disabled("todo@1.0.1: fix inline session logic before enabling this test")
	@Test
	@DisplayName("GET /adminLogout.controller - requires admin")
	void adminLogout_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/adminLogout.controller"))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /showAllUser.controller - success")
	void gotoFindAllUserPage_success() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/showAllUser.controller")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$[*].u_id", hasItem("joshua")));
	}

	@Test
	@DisplayName("GET /showAllUser.controller - requires admin")
	void gotoFindAllUserPage_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/showAllUser.controller"))

				.andExpect(status().isUnauthorized());
	}
}