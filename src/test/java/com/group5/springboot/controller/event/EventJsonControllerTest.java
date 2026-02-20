package com.group5.springboot.controller.event;

import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;
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

import static com.group5.springboot.controller.event.EventTestUtils.*;
import static com.group5.springboot.controller.user.UserTestUtils.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventJsonControllerTest {
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;


	private MockHttpSession mockHttpSession = new MockHttpSession();

	private User_Info yuz;
	private User_Info demoid2;
	private User_Info[] applicants;
	private EventInfo event1Approved;
	private EventInfo event2;
	private EventInfo event3;


	@Autowired
	EventJsonControllerTest(MockMvc mockMvc, GenericDao dao) {
		this.mockMvc = mockMvc;
		this.dao = dao;
		this.userTestUtils = new UserTestUtils(mockMvc);
	}


	@BeforeEach
	void setUp() {
		this.mockHttpSession = new MockHttpSession();

		// add fresh test data + admin approves
		this.yuz = dao.save(aUserYuz());
		this.demoid2 = dao.save(aUserDemoid2());
		this.applicants = new User_Info[4];
			this.applicants[0] = dao.save(aUserJoshua());
			this.applicants[1] = dao.save(aUserKen());
			this.applicants[2] = dao.save(aUserNick());
			this.applicants[3] = dao.save(aUserYen());
		this.event1Approved = dao.saveEventButNoStorage(anEventInfo(), yuz);
		this.event2 = dao.saveEventButNoStorage(anEventInfo2(), yuz);
		this.event3 = dao.saveEventButNoStorage(anEventInfo3(), demoid2);
		dao.adminApprovesEvent(event1Approved);
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		dao.deleteAll(Entryform.class);
		dao.deleteAll(EventInfo.class);
		dao.deleteAll(User_Info.class);
		this.applicants = null;
	}


	@Test
	@DisplayName("GET /EventfindAll - success")
	void eventfindAll_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/EventfindAll")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.size", is(3)))
				.andExpect(jsonPath("$.list[*].a_aid", hasItem(event1Approved.getA_aid().intValue())))
				.andExpect(jsonPath("$.list[*].a_aid", hasItem(event2.getA_aid().intValue())))
				.andExpect(jsonPath("$.list[*].a_aid", hasItem(event3.getA_aid().intValue())));
	}

	@Test
	@DisplayName("GET /EventfindAll - requires admin")
	void eventfindAll_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/EventfindAll"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("/guest/EventfindAll")
	void guestEventfindAll() throws Exception {
		mockMvc.perform(get("/guest/EventfindAll"))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.list[*].verification", everyItem(is("Y"))));
	}

	@Test
	@DisplayName("GET /admin/events - success")
	void adminFindEvents_success() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		String rname = event1Approved.getA_name().substring(1);
		mockMvc.perform(get("/admin/events")
						.session(mockHttpSession)
						.queryParam("rname", rname)
						.queryParam("approved", "false")
						.queryParam("includeEntryforms", "false"))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.list[*].a_name", everyItem(containsStringIgnoringCase(rname))))
				.andExpect(jsonPath("$.list[*].verification", everyItem(is("Y"))));
	}

	@Test
	@DisplayName("GET /admin/events - requires admin")
	void adminFindEvents_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/admin/events"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /queryEventByName - success")
	void queryByName_success() throws Exception {
		// 0, admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/queryEventByName")
						.param("rname", event1Approved.getA_name().substring(0, 3))
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.size", is(1)))
				.andExpect(jsonPath("$.list[0].a_aid", is(event1Approved.getA_aid().intValue())))
				.andExpect(jsonPath("$.list[0].a_aid", not(event2.getA_aid().intValue())))
				.andExpect(jsonPath("$.list[0].a_aid", not(event3.getA_aid().intValue())));
	}

	@Test
	@DisplayName("GET /queryEventByName - requires admin")
	void queryByName_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/queryEventByName"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /me/events - success")
	void userFindEvents_success() throws Exception {
		userTestUtils.loginAs(yuz, mockHttpSession);

		String rname = event2.getA_name().substring(1);
		mockMvc.perform(get("/me/events")
						.session(mockHttpSession)
						.queryParam("rname", rname))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.size", is(1)))
				.andExpect(jsonPath("$.list[*].a_name", everyItem(containsStringIgnoringCase(rname))))
				.andExpect(jsonPath("$.list[*].a_uid", everyItem(is(yuz.getU_id()))));
	}

	@Test
	@DisplayName("GET /me/events - requires user")
	void userFindEvents_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/me/events"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /eventcontentjson/{a_aid} - success")
	void eventcontentjson_success() throws Exception {
		mockMvc.perform(get("/eventcontentjson/{a_aid}", event1Approved.getA_aid()))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.a_aid", is(event1Approved.getA_aid().intValue())));
	}

	@Test
	@DisplayName("GET /eventcontentjson/{a_aid} - unreviewed event")
	void eventcontentjson_whenUnreviewedEvent_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/eventcontentjson/{a_aid}", event2.getA_aid()))

				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("GET /Eventfindbyuid - success")
	void eventfindbyuid_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(demoid2, mockHttpSession);


		// 1. main
		mockMvc.perform(get("/Eventfindbyuid")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.size", is(1)))
				.andExpect(jsonPath("$.list[*].a_aid", not(hasItem(event1Approved.getA_aid().intValue()))))
				.andExpect(jsonPath("$.list[*].a_aid", not(hasItem(event2.getA_aid().intValue()))))
				.andExpect(jsonPath("$.list[*].a_aid", hasItem(event3.getA_aid().intValue())));
	}

	@Test
	@DisplayName("GET /Eventfindbyuid - requires user")
	void eventfindbyuid_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/Eventfindbyuid"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /signupEventjson/{a_aid} - success")
	void signupEventjson_success() throws Exception {
		// 0. random users login to sign up the test event!
		for (User_Info applicant : applicants) {
			dao.persistEventRegistration(event1Approved, applicant);
		}

		// 0. holder logins
		User_Info holder = yuz;
		userTestUtils.loginAs(holder, mockHttpSession);


		// 1. main
		mockMvc.perform(get("/signupEventjson/{a_aid}", event1Approved.getA_aid())
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[*].e_id", hasItem(applicants[0].getU_id())))
				.andExpect(jsonPath("$[*].e_id", hasItem(applicants[1].getU_id())))
				.andExpect(jsonPath("$[*].e_id", hasItem(applicants[2].getU_id())))
				.andExpect(jsonPath("$[*].e_id", hasItem(applicants[3].getU_id())));
	}

	@Test
	@DisplayName("GET /signupEventjson/{a_aid} - requires user")
	void signupEventjson_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/signupEventjson/{a_aid}", event1Approved.getA_aid()))

				.andExpect(status().isUnauthorized());
	}
}