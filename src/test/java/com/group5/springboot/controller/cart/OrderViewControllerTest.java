package com.group5.springboot.controller.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
				.andExpect(view().name("cart/orderAdminSelect"));
	}
}