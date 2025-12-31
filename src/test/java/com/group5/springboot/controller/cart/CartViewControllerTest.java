package com.group5.springboot.controller.cart;

import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.dto.cart.AdminCreateCartItemRequest;
import com.group5.springboot.dto.cart.AdminUpdateCartItemRequest;
import com.group5.springboot.model.cart.CartItem;
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
import static com.group5.springboot.controller.user.UserTestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartViewControllerTest {
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;

	private MockHttpSession mockHttpSession;

	private User_Info tajenwww;
	private CartItem cartItem1;
	private ProductInfo shelfItem1;


	@Autowired
	CartViewControllerTest(MockMvc mockMvc, GenericDao dao){
		this.mockMvc = mockMvc;
		this.dao = dao;
		this.userTestUtils = new UserTestUtils(this.mockMvc);
	}


	@BeforeEach
	void setUp() {
		this.mockHttpSession = new MockHttpSession();

		// set up test data & dependencies
		this.tajenwww = dao.save(aUserTajenwww());
		var ken = dao.save(aUserKen());
		var product1 = dao.saveProductButSkipStorage(aRandomProduct(), ken);
		this.shelfItem1 = dao.saveProductButSkipStorage(aRandomProduct(), ken);
		this.cartItem1 = dao.persistCartItem(product1, tajenwww);
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		dao.deleteAll(CartItem.class);
		dao.deleteAll(ProductInfo.class);
		dao.deleteAll(User_Info.class);
	}


	@Test
	@DisplayName("GET /cart.controller/adminInsert - success")
	void toCartAdminInsert_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(get("/cart.controller/adminInsert")
				.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(model().attributeExists("emptyCartItem"))
				.andExpect(view().name("/cart/cartAdminInsert"));
	}

	@Test
	@DisplayName("POST /cart.controller/adminInsert - success")
	void cartAdminInsert_success() throws Exception {
		// 0. admin-login + decide a cart item to add & a user to add for
		userTestUtils.adminLoginAsAdming5(mockHttpSession);
		var newCartItem = new AdminCreateCartItemRequest(shelfItem1, tajenwww);


		// 1. main
		mockMvc.perform(post("/cart.controller/adminInsert")
						.session(mockHttpSession)
						.param("p_id", newCartItem.getP_id() + "")
						.param("p_name", newCartItem.getP_name())
						.param("p_price", newCartItem.getP_price() + "")
						.param("u_id", newCartItem.getU_id())
						.param("u_firstname", newCartItem.getU_firstname())
						.param("u_lastname", newCartItem.getU_lastname()))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/cart.controller/adminSelect"));
	}

	@Test
	@DisplayName("POST /cart.controller/adminInsert - empty field")
	void cartAdminInsert_whenEmptyField_thenRequestIsRejected() throws Exception {
		// 0. admin-login + decide a cart item to add & a user to add for
		userTestUtils.adminLoginAsAdming5(mockHttpSession);
		var newCartItem = new AdminCreateCartItemRequest(shelfItem1, tajenwww);


		// 1. main
		mockMvc.perform(post("/cart.controller/adminInsert")
						.session(mockHttpSession)
						.param("p_id", "") // mandatory field empty
						.param("p_name", newCartItem.getP_name())
						.param("p_price", newCartItem.getP_price() + "")
						.param("u_id", "") // mandatory field empty
						.param("u_firstname", newCartItem.getU_firstname())
						.param("u_lastname", newCartItem.getU_lastname()))

				.andExpect(view().name("/cart/cartAdminInsert"))
				.andExpect(model().errorCount(2));
	}

	@Test
	@DisplayName("GET /cart.controller/adminUpdate/{cartid} - success")
	void toCartAdminUpdate_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/cart.controller/adminUpdate/{cartid}", cartItem1.getCart_id())
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(model().attributeExists("cartItem"))
				.andExpect(view().name("/cart/cartAdminUpdate"));
	}

	@Test
	@DisplayName("POST /cart.controller/adminUpdate/{cartid} - success")
	void cartAdminUpdate_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);
		var update = AdminUpdateCartItemRequest.Builder
				.from(cartItem1)
				.newProduct(shelfItem1)
				.build();


		// 1. main
		mockMvc.perform(post("/cart.controller/adminUpdate/{cartid}", cartItem1.getCart_id())
				.session(mockHttpSession)
				.param("cart_id", update.getCart_id() + "")
				.param("p_id", update.getP_id() + "")
				.param("p_name", update.getP_name())
				.param("p_price", update.getP_price() + "")
				.param("u_id", update.getU_id())
				.param("u_firstname", update.getU_firstname())
				.param("u_lastname", update.getU_lastname()))

				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("successMessage"))
				.andExpect(redirectedUrl("/cart.controller/adminSelect"));
	}

	@Test
	@DisplayName("POST /cart.controller/adminUpdate/{cartid} - empty field")
	void cartAdminUpdate_whenEmptyField_thenRequestIsRejected() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);
		var update = AdminUpdateCartItemRequest.Builder
				.from(cartItem1)
				.newProduct(shelfItem1)
				.build();


		// 1. main
		mockMvc.perform(post("/cart.controller/adminUpdate/{cartid}", cartItem1.getCart_id())
						.session(mockHttpSession)
						.param("cart_id", update.getCart_id() + "")
						.param("p_id", "") // mandatory field empty
						.param("p_name", update.getP_name())
						.param("p_price", update.getP_price() + "")
						.param("u_id", "")  // mandatory field empty
						.param("u_firstname", update.getU_firstname())
						.param("u_lastname", update.getU_lastname()))

				.andExpect(view().name("/cart/cartAdminUpdate"))
				.andExpect(model().errorCount(2));
	}

	@Test
	@DisplayName("GET /cart.controller/cartIndex - success")
	void toCartIndex_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		mockMvc.perform(get("/cart.controller/cartIndex")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("cart/cartIndex"));
	}

	@Test
	@DisplayName("GET /cart.controller/adminSelect - success")
	void toCartAdminSelect_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/cart.controller/adminSelect")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("cart/cartAdminSelect"));
	}

	@Test
	@DisplayName("/cart.controller/clientResultPage - success")
	void toClientResultPage_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		mockMvc.perform(get("/cart.controller/clientResultPage")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(view().name("cart/cartClientResultPage"));
	}
}