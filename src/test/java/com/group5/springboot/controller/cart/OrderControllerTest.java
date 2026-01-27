package com.group5.springboot.controller.cart;

import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
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

import java.time.LocalDateTime;

import static com.group5.springboot.controller.product.ProductTestUtils.aRandomProduct;
import static com.group5.springboot.controller.user.UserTestUtils.*;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest {
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;

	private MockHttpSession mockHttpSession;

	private User_Info tajenwww;
	private OrderInfo purchased1;
	private OrderInfo purchased2;
	private OrderInfo purchased3;


	@Autowired
	OrderControllerTest(MockMvc mockMvc, GenericDao dao){
		this.mockMvc = mockMvc;
		this.dao = dao;
		this.userTestUtils = new UserTestUtils(this.mockMvc);
	}


	@BeforeEach
	void setUp() {
		this.mockHttpSession = new MockHttpSession();

		// set up test data & dependencies
		this.tajenwww = dao.save(aUserTajenwww());
		var joshua = dao.save(aUserJoshua());
		var product1 = dao.saveProductButSkipStorage(aRandomProduct(), joshua);
		var product2 = dao.saveProductButSkipStorage(aRandomProduct(), joshua);
		var product3 = dao.saveProductButSkipStorage(aRandomProduct(), joshua);
		this.purchased1 = dao.persistOrder(product1, tajenwww);
		this.purchased2 = dao.persistOrder(product2, tajenwww);
		this.purchased3 = dao.persistOrder(product3, tajenwww);
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		dao.deleteAll(OrderInfo.class);
		dao.deleteAll(ProductInfo.class);
		dao.deleteAll(User_Info.class);
	}


	@Test
	@DisplayName("GET /order.controller/adminSelectTop100 - success")
	void adminOrderSelectTop100_success() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/order.controller/adminSelectTop100")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list", hasSize(3)))
				.andExpect(jsonPath("$.list[*].o_id", contains(
						purchased3.getO_id(),
						purchased2.getO_id(),
						purchased1.getO_id()
				)));
	}

	@Test
	@DisplayName("GET /order.controller/adminSelectTop100 - requires admin")
	void adminOrderSelectTop100_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(get("/order.controller/adminSelectTop100"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("POST /order.controller/adminSearchBar - success (u_id full match)")
	void adminOrderSearchBar_success_uidFullMatch() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(post("/order.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "u_id")
						.param("searchBar", tajenwww.getU_id()))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list", hasSize(3)))
				.andExpect(jsonPath("$.list[*].o_id", containsInAnyOrder(
						purchased1.getO_id(),
						purchased3.getO_id(),
						purchased2.getO_id()
				)));
	}

	@Test
	@DisplayName("POST /order.controller/adminSearchBar - success (p_name partial match)")
	void adminOrderSearchBar_success_pnamePartialMatch() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		String p_name = purchased1.getProductInfo().getP_Name();
		mockMvc.perform(post("/order.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "p_name")
						.param("searchBar", p_name.substring(1)))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list", hasSize(1)))
				.andExpect(jsonPath("$.list[*].o_id", contains(purchased1.getO_id())));
	}

	@Test
	@DisplayName("POST /order.controller/adminSearchBar - success (o_date time range match)")
	void adminOrderSearchBar_success_odateTimeRangeMatch() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		var now = LocalDateTime.now();
//		var from = now.minusHours(7).minusMinutes(59).format(ISO_LOCAL_DATE_TIME);
//		var from = now.minusHours(8).format(ISO_LOCAL_DATE_TIME);
		var from = now.minusDays(1).format(ISO_LOCAL_DATE_TIME);
		var to = now.plusMinutes(10).format(ISO_LOCAL_DATE_TIME);
		System.out.println("db order#1 date = " + dao.find(OrderInfo.class, purchased1.getIdentity_seed()).getO_date());
		mockMvc.perform(post("/order.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "o_date")
						.param("searchBar", from + "," + to))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list", hasSize(3)))
				.andExpect(jsonPath("$.list[*].o_id", containsInAnyOrder(
						purchased1.getO_id(),
						purchased3.getO_id(),
						purchased2.getO_id()
				)));
	}

	@Test
	@DisplayName("POST /order.controller/adminSearchBar - success (o_amt number range match)")
	void adminOrderSearchBar_success_oamtNumberRangelMatch() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		String p_name = purchased1.getProductInfo().getP_Name();
		mockMvc.perform(post("/order.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "o_amt")
						.param("searchBar", "0,10000"))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list", hasSize(3)))
				.andExpect(jsonPath("$.list[*].o_id", containsInAnyOrder(
						purchased1.getO_id(),
						purchased3.getO_id(),
						purchased2.getO_id()
				)));
	}

	@Test
	@DisplayName("POST /order.controller/adminSearchBar - requires admin")
	void adminOrderSearchBar_whenNoAdminLoggedIn_thenAccessIsDenied() throws Exception {
		mockMvc.perform(post("/order.controller/adminSearchBar"))

				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("POST /order.controller/adminSearchBar - invalid search condition")
	void adminOrderSearchBar_whenInvalidSearchCondition_thenRequestIsRejected() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		String p_name = purchased1.getProductInfo().getP_Name();
		mockMvc.perform(post("/order.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "intellectual-laziness")
						.param("searchBar", "taida-desune"))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errorMessage").exists())
				.andExpect(jsonPath("$.list").isEmpty());
	}
}