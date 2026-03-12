package com.group5.springboot.controller.cart;

import com.group5.springboot.controller.user.UserTestUtils;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderViewControllerTest {
	private final MockMvc mockMvc;
	private final UserTestUtils userTestUtils;

	private MockHttpSession mockHttpSession;


	@Autowired
	OrderViewControllerTest(MockMvc mockMvc){
		this.mockMvc = mockMvc;
		this.userTestUtils = new UserTestUtils(this.mockMvc);
	}


	@BeforeEach
	void setUp() {
		this.mockHttpSession = new MockHttpSession();
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}
	}


	@Test
	@DisplayName("GET /order.controller/adminSelect - success")
	// wrong method name
	void toCartAdminSelect_success() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/order.controller/adminSelect")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("orders/admin/list"));
	}

	@Test
	@DisplayName("GET /order.controller/adminSelect - requires admin")
	// wrong method name
	void toCartAdminSelect_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/order.controller/adminSelect"))

				.andExpect(forwardedUrl("/gotoAdminLogin.controller"));
	}
}