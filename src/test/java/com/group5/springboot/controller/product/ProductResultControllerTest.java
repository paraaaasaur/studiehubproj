package com.group5.springboot.controller.product;

import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.group5.springboot.controller.product.ProductTestUtils.aRandomProduct;
import static com.group5.springboot.controller.user.UserTestUtils.aUserKen;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductResultControllerTest {
	private final MockMvc mockMvc;
	private final UserTestUtils userTestUtils;
	private final GenericDao dao;

	private User_Info ken;
	private ProductInfo product1Approved;
	private ProductInfo product2;
	private ProductInfo product3;
	private MockHttpSession mockHttpSession = new MockHttpSession();


	@Autowired
	ProductResultControllerTest(MockMvc mockMvc, GenericDao dao) {
		this.mockMvc = mockMvc;
		this.userTestUtils = new UserTestUtils(mockMvc);
		this.dao = dao;
	}


	@BeforeEach
	void setUp() {
		mockHttpSession = new MockHttpSession();

		// prepare fresh test data
		// new user ken + ken creates 3 new courses w/o rating & have 1 verified
		this.ken = dao.save(aUserKen());
		this.product1Approved = dao.saveProductButSkipStorage(aRandomProduct(), ken);
		this.product2 = dao.saveProductButSkipStorage(aRandomProduct(), ken);
		this.product3 = dao.saveProductButSkipStorage(aRandomProduct(), ken);
		dao.adminApprovesProduct(product1Approved);
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		dao.deleteAll(ProductInfo.class);
		dao.deleteAll(User_Info.class);
	}


	@Test
	@DisplayName("GET /findAllProduct")
	void findAll() throws Exception {
		mockMvc.perform(get("/findAllProduct"))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", aMapWithSize(3)))
				.andExpect(jsonPath("$.ratedIndex", everyItem(nullValue())))
				.andExpect(jsonPath("$.size", is(1)))
				.andExpect(jsonPath("$.list[*].p_Name", hasItem(product1Approved.getP_Name())));
	}

	@Test
	@DisplayName("GET /findAllProductPendingAccess - success")
	void findAllProductPendingAccess_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/findAllProductPendingAccess")
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", aMapWithSize(2)))
				.andExpect(jsonPath("$.size", is(2)))
				.andExpect(jsonPath("$.list[*].p_Name", hasItem(product2.getP_Name())))
				.andExpect(jsonPath("$.list[*].p_Name", hasItem(product3.getP_Name())));
	}

	@Test
	@DisplayName("GET /findAllProductPendingAccess - requires admin")
	void findAllProductPendingAccess_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/findAllProductPendingAccess"))

				.andExpect(status().isUnauthorized());
	}

	@Disabled("reactivate after fixes on resource leakage(1.0.1) and wrong query(1.0.2)")
	@Test
	@DisplayName("GET /queryByProductName")
	void queryByName() throws Exception {
		String pname = product1Approved.getP_Name().substring(1);
		String producttypename = product1Approved.getP_Class();
		mockMvc.perform(get("/queryByProductName")
						.param("pname", pname)
						.param("producttypename", producttypename))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", aMapWithSize(3)))
				.andExpect(jsonPath("$.ratedIndex", everyItem(nullValue())))
				.andExpect(jsonPath("$.size", is(1)))
				.andExpect(jsonPath("$.list[*].p_Name", hasItem(product1Approved.getP_Name())));
	}
}