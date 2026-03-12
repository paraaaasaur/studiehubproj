package com.group5.springboot.controller.event;

import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.event.Entryform;
import com.group5.springboot.model.event.EventInfo;
import com.group5.springboot.model.user.User_Info;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.group5.springboot.controller.event.EventTestUtils.*;
import static com.group5.springboot.controller.user.UserTestUtils.*;
import static com.group5.springboot.controller.user.UserTestUtils.aUserKen;
import static com.group5.springboot.controller.user.UserTestUtils.aUserNick;
import static com.group5.springboot.controller.user.UserTestUtils.aUserYen;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerTest {
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;

	private MockHttpSession mockHttpSession = new MockHttpSession();

	private User_Info yuz;
	private User_Info applicant1;
	private EventInfo event1Approved;
	private EventInfo event2;
	private EventInfo event3ApprovedButDue;



	@Autowired
	EventControllerTest(MockMvc mockMvc, GenericDao dao) {
		this.mockMvc = mockMvc;
		this.dao = dao;
		this.userTestUtils = new UserTestUtils(mockMvc);
	}


	@BeforeEach
	void setUp() {
		mockHttpSession = new MockHttpSession();

		// renew test data
		this.yuz = dao.save(aUserYuz());
		this.applicant1 = dao.save(aUserTajenwww());
		this.event1Approved = dao.saveEventButNoStorage(anEventInfo(), yuz);
		dao.adminApprovesEvent(event1Approved);
		this.event2 = dao.saveEventButNoStorage(anEventInfo2(), yuz);
		this.event3ApprovedButDue = dao.saveEventButNoStorage(aDueEventInfo1(), yuz);
		dao.adminApprovesEvent(event3ApprovedButDue);
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
	}


	@Test
	@DisplayName("GET /insertEvent - success")
	void insertEvent_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(yuz, mockHttpSession);


		mockMvc.perform(get("/insertEvent")
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("events/add"));
	}

	@Test
	@DisplayName("GET /insertEvent - requires user")
	void insertEvent_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/insertEvent"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("GET /userAllEvent - success")
	void userAllEvent_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(yuz, mockHttpSession);


		// 1. main
		mockMvc.perform(get("/userAllEvent")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("events/my-list"));
	}

	@Test
	@DisplayName("GET /userAllEvent - requires user")
	void userAllEvent_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/userAllEvent"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("GET /adminAllEvent - success")
	void queryRestaurant_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/adminAllEvent")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("events/admin/list"));
	}

	@Test
	@DisplayName("GET /adminAllEvent - requires admin")
	void queryRestaurant_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/adminAllEvent"))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /eventindex")
	void eventindex() throws Exception {
		mockMvc.perform(get("/eventindex"))

				.andExpect(status().isOk())
				.andExpect(view().name("events/list"));
	}

	@Test
	@DisplayName("GET /managerAllEvent - success")
	void managerAllEvent_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/managerAllEvent")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("events/admin/pending-list"));
	}

	@Test
	@DisplayName("GET /managerAllEvent - requires admin")
	void managerAllEvent_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/managerAllEvent"))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("POST /insertEvent - success")
	void insertSaveEvent_success() throws Exception {
		// 0. login + prepare test data
		userTestUtils.loginAs(yuz, mockHttpSession);
		var body = anEventInfo2();
		var holder = (User_Info) mockHttpSession.getAttribute("loginBean");


		// 1. main
		mockMvc.perform(multipart("/insertEvent")
						.file((MockMultipartFile) body.getEventImage())
						.param("a_name", body.getA_name())
						.param("a_type", body.getA_type())
						.param("registration_starttime", body.getRegistration_starttime())
						.param("registration_endrttime", body.getRegistration_endrttime())
						.param("Transienta_startTime", body.getTransienta_startTime())
						.param("Transienta_endTime", body.getTransienta_endTime())
						.param("a_address", body.getA_address())
						.param("transientcomment", body.getTransientcomment())
						.param("applicants", body.getApplicants() + "")
						.param("uidname", holder.getU_lastname() + holder.getU_firstname())
						.param("a_uid", holder.getU_id())
						.session(mockHttpSession)
						.contentType(MULTIPART_FORM_DATA))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/userAllEvent"));
	}

	@Test
	@DisplayName("POST /insertEvent - requires user")
	void insertSaveEvent_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(multipart("/insertEvent"))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("POST /insertEvent - empty field")
	void insertSaveEvent_whenEmptyField_thenRequestIsRejected() throws Exception {
		// 0. login + prepare test data
		userTestUtils.loginAs(yuz, mockHttpSession);
		var body = anEventInfo2();
		var holder = (User_Info) mockHttpSession.getAttribute("loginBean");


		// 1. main
		mockMvc.perform(multipart("/insertEvent")
						.file((MockMultipartFile) body.getEventImage())
						.param("a_name", "") // mandatory field empty
						.param("a_type", body.getA_type())
						.param("registration_starttime", body.getRegistration_starttime())
						.param("registration_endrttime", body.getRegistration_endrttime())
						.param("Transienta_startTime", body.getTransienta_startTime())
						.param("Transienta_endTime", body.getTransienta_endTime())
						.param("a_address", body.getA_address())
						.param("transientcomment", body.getTransientcomment())
						.param("applicants", body.getApplicants() + "")
						.param("uidname", holder.getU_lastname() + holder.getU_firstname())
						.param("a_uid", holder.getU_id())
						.session(mockHttpSession)
						.contentType(MULTIPART_FORM_DATA))

				.andExpect(view().name("events/add"))
				.andExpect(model().errorCount(1));
	}

	@Test
	@DisplayName("GET /updateEvent/{a_aid} - success")
	void sendEditPage_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(yuz, mockHttpSession);


		// 1. main
		final long a_aid = event1Approved.getA_aid();
		mockMvc.perform(get("/updateEvent/{a_aid}", a_aid)
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(model().attributeExists("EventInfo"))
				.andExpect(view().name("events/edit"));
	}

	@Test
	@DisplayName("GET /updateEvent/{a_aid} - requires user")
	void sendEditPage_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/updateEvent/{a_aid}", event1Approved.getA_aid()))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("POST /updateEvent/{a_aid} - success")
	void updateSaveEvent_success() throws Exception {
		// 0. login + prepare test data
		User_Info holder = yuz;
		userTestUtils.loginAs(holder, mockHttpSession);

		var body = anEventInfo3();


		// 1. main
		mockMvc.perform(multipart("/updateEvent/{a_aid}", event1Approved.getA_aid())
						.file((MockMultipartFile) body.getEventImage())
						.param("a_name", body.getA_name())
						.param("a_type", body.getA_type())
						.param("registration_starttime", body.getRegistration_starttime())
						.param("registration_endrttime", body.getRegistration_endrttime())
						.param("Transienta_startTime", body.getTransienta_startTime())
						.param("Transienta_endTime", body.getTransienta_endTime())
						.param("a_address", body.getA_address())
						.param("transientcomment", body.getTransientcomment())
						.param("applicants", body.getApplicants() + "")
						.param("uidname", holder.getU_lastname() + holder.getU_firstname())
						.param("a_uid", holder.getU_id())
						.session(mockHttpSession)
						.contentType(MULTIPART_FORM_DATA))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/userAllEvent"));
	}

	@Test
	@DisplayName("POST /updateEvent/{a_aid} - requires user")
	void updateSaveEvent_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(multipart("/updateEvent/{a_aid}", event1Approved.getA_aid()))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("GET /deleteEvent/{a_aid} - success")
	void deleteEditPage_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(yuz, mockHttpSession);


		// 1. main
		mockMvc.perform(get("/deleteEvent/{a_aid}", event1Approved.getA_aid())
				.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/userAllEvent"));
	}

	@Test
	@DisplayName("GET /deleteEvent/{a_aid} - requires user")
	void deleteEditPage_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/deleteEvent/{a_aid}", event1Approved.getA_aid()))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("GET /deleteadminEvent/{a_aid} - success")
	void deleteadminEvent_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/deleteadminEvent/{a_aid}", event1Approved.getA_aid())
				.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/adminAllEvent"));
	}

	@Test
	@DisplayName("GET /deleteadminEvent/{a_aid} - requires admin")
	void deleteadminEvent_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/deleteadminEvent/{a_aid}", event1Approved.getA_aid()))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /Selecteventcontent/{a_aid}")
	void Selecteventcontent() throws Exception {

		final Long a_aid = event1Approved.getA_aid();
		mockMvc.perform(get("/Selecteventcontent/{a_aid}", a_aid))

				.andExpect(status().isOk())
				.andExpect(model().attributeExists("eventcontent"))
				.andExpect(view().name("events/detail"));
	}

	@Test
	@DisplayName("GET /verification/{a_aid} - success")
	void verification_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		final Long a_aid = event1Approved.getA_aid();
		mockMvc.perform(get("/verification/{a_aid}", a_aid)
						.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/managerAllEvent"));
	}

	@Test
	@DisplayName("GET /verification/{a_aid} - requires admin")
	void verification_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/verification/{a_aid}", event1Approved.getA_aid()))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /deleteverification/{a_aid} - success")
	void deleteverification_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);

		// 1. main
		mockMvc.perform(get("/deleteverification/{a_aid}", event1Approved.getA_aid())
				.session(mockHttpSession))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/managerAllEvent"));
	}

	@Test
	@DisplayName("GET /deleteverification/{a_aid} - requires admin")
	void deleteverification_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/deleteverification/{a_aid}", event1Approved.getA_aid()))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}

	@Test
	@DisplayName("GET /signupclick/{a_aid} - success")
	void signupclick_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(applicant1, mockHttpSession);


		// 1. main
		final Long a_aid = event1Approved.getA_aid();
		mockMvc.perform(get("/signupclick/{a_aid}", a_aid)
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.succes", notNullValue()));
	}

	@Test
	@DisplayName("GET /signupclick/{a_aid} - requires user")
	void signupclick_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/signupclick/{a_aid}", event1Approved.getA_aid()))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /signupclick/{a_aid} - already registered")
	void signupclick_whenAlreadyRegistered_thenRequestIsRejected() throws Exception {
		// 0. login
		userTestUtils.loginAs(applicant1, mockHttpSession);


		// 1. main
		final Long a_aid = event1Approved.getA_aid();
		mockMvc.perform(get("/signupclick/{a_aid}", a_aid)
						.session(mockHttpSession));

		mockMvc.perform(get("/signupclick/{a_aid}", a_aid)
						.session(mockHttpSession))

				.andExpect(jsonPath("$.fail").exists());
	}

	@Test
	@DisplayName("GET /signupclick/{a_aid} - applicant limit exceeded")
	void signupclick_whenApplicantLimitExceeded_thenRequestIsRejected() throws Exception {
		// 0. login + occupy all applicant slots
		userTestUtils.loginAs(applicant1, mockHttpSession);
		User_Info[] applicants = fiveUsers();
		for (var applicant : applicants) {
			dao.persistEventRegistration(event1Approved, applicant);
		}


		// 1. main
		final Long a_aid = event1Approved.getA_aid();
		mockMvc.perform(get("/signupclick/{a_aid}", a_aid)
						.session(mockHttpSession))

				.andExpect(jsonPath("$.Exceed").exists());
	}

	@Test
	@DisplayName("GET /signupclick/{a_aid} - due")
	void signupclick_whenDue_thenRequestIsRejected() throws Exception {
		// 0. login
		userTestUtils.loginAs(applicant1, mockHttpSession);


		// 1. main
		final Long a_aid = event3ApprovedButDue.getA_aid();

		mockMvc.perform(get("/signupclick/{a_aid}", a_aid)
						.session(mockHttpSession))

				.andExpect(jsonPath("$.Time").exists());
	}

	@Test
	@DisplayName("GET /signupEvent/{a_aid} - success")
	void signupEvent_success() throws Exception {
		// 0. login to signup event
		userTestUtils.loginAs(yuz, mockHttpSession);
		dao.persistEventRegistration(event1Approved, applicant1);


		// 1. main
		final Long a_aid = event1Approved.getA_aid();
		mockMvc.perform(get("/signupEvent/{a_aid}", a_aid)
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(model().attributeExists("signupEvent"))
				.andExpect(view().name("events/registration/list"));
	}

	@Test
	@DisplayName("GET /signupEvent/{a_aid} - requires user")
	void signupEvent_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/signupEvent/{a_aid}", event1Approved.getA_aid()))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}

	@Test
	@DisplayName("GET /deletesignupEvent/{e_id}/{a_id} - success")
	void cancelEventRegistration_success() throws Exception {
		// 0. login to sign up event
		userTestUtils.loginAs(yuz, mockHttpSession);
		dao.persistEventRegistration(event1Approved, applicant1);


		// 1. main
		final Long a_id = event1Approved.getA_aid();
		final Long e_id = dao.findEventRegistration(a_id, applicant1.getU_id()).getId();
		mockMvc.perform(get("/deletesignupEvent/{e_id}/{a_id}", e_id, a_id)
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(model().attributeExists("signupEvent"))
				.andExpect(view().name("events/registration/list"));
	}

	@Test
	@DisplayName("GET /deletesignupEvent/{e_id}/{a_id} - requires user")
	void cancelEventRegistration_whenNoUserLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/deletesignupEvent/{e_id}/{a_id}", 0, 0))

				.andExpect(forwardedUrl("/gotologin.controller"));
	}


	// helper
	public User_Info[] fiveUsers() {
		User_Info[] applicants = new User_Info[5];
			applicants[0] = dao.save(aUserJoshua());
			applicants[1] = dao.save(aUserKen());
			applicants[2] = dao.save(aUserNick());
			applicants[3] = dao.save(aUserYen());
			applicants[4] = dao.save(aUserDemoid2());

		return applicants;
	}
}