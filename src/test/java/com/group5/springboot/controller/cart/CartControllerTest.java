package com.group5.springboot.controller.cart;

import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.group5.springboot.controller.product.ProductTestUtils.aRandomProduct;
import static com.group5.springboot.controller.user.UserTestUtils.*;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartControllerTest {
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;

	private MockHttpSession mockHttpSession;

	private User_Info tajenwww;
	private ProductInfo shelfItem1;
	private CartItem cartItem1;
	private CartItem cartItem2;
	private CartItem cartItem3;
	private OrderInfo purchasedItem1;


	@Autowired
	CartControllerTest(MockMvc mockMvc, GenericDao dao){
		this.mockMvc = mockMvc;
		this.dao = dao;
		this.userTestUtils = new UserTestUtils(this.mockMvc);
	}


	@BeforeEach
	void setUp() {
		this.mockHttpSession = new MockHttpSession();

		// set up test data & dependencies
		this.tajenwww = dao.save(aUserTajenwww());
		var nick = dao.save(aUserNick());
		var product1 = dao.saveProductButSkipStorage(aRandomProduct(), nick);
		var product2 = dao.saveProductButSkipStorage(aRandomProduct(), nick);
		var product3 = dao.saveProductButSkipStorage(aRandomProduct(), nick);
		var product4 = dao.saveProductButSkipStorage(aRandomProduct(), nick);
		this.shelfItem1 = dao.saveProductButSkipStorage(aRandomProduct(), nick);
		this.cartItem1 = dao.persistCartItem(product1, tajenwww);
		this.cartItem2 = dao.persistCartItem(product2, tajenwww);
		this.cartItem3 = dao.persistCartItem(product3, tajenwww);
		this.purchasedItem1 = dao.persistOrder(product4, tajenwww);
	}

	@AfterEach
	void tearDown() {
		if (!mockHttpSession.isInvalid()) {
			mockHttpSession.invalidate();
		}

		// tear down test data
		dao.deleteAll(CartItem.class);
		dao.deleteAll(OrderInfo.class);
		dao.deleteAll(ProductInfo.class);
		dao.deleteAll(User_Info.class);
	}

	@Test
	@DisplayName("POST /cart.controller/clientShowCart - success")
	void clientShowCart_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		mockMvc.perform(post("/cart.controller/clientShowCart")
						.session(mockHttpSession)
						.param("u_id", tajenwww.getU_id()))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[*].cart_id", containsInAnyOrder(
						cartItem1.getCart_id(),
						cartItem2.getCart_id(),
						cartItem3.getCart_id()
				)));
	}

	@Test
	@DisplayName("POST /cart.controller/clientRemoveProductFromCartByCartId - success")
	void clientRemoveProductFromCartByCartId_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		String[] removes = {cartItem2.getCart_id() + "", cartItem3.getCart_id() + ""};
		mockMvc.perform(post("/cart.controller/clientRemoveProductFromCartByCartId")
						.session(mockHttpSession)
						.param("cart_ids", removes)
						.param("u_id", tajenwww.getU_id()))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[*].cart_id", contains(cartItem1.getCart_id())));
	}

	@Test
	@DisplayName("POST /cart.controller/clientAddProductToCart (toDo=buy) - success")
	void clientAddProductToCart_toDoEqualsBuy_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		mockMvc.perform(post("/cart.controller/clientAddProductToCart")
						.contentType(APPLICATION_FORM_URLENCODED)
						.session(mockHttpSession)
						.param("u_ID", tajenwww.getU_id())
						.param("p_ID", shelfItem1.getP_ID() + "")
						.param("toDo", "buy"))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(true)));
	}

	@Test
	@DisplayName("POST /cart.controller/clientAddProductToCart (toDo=remove) - success")
	void clientAddProductToCart_toDoEqualsRemove_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		mockMvc.perform(post("/cart.controller/clientAddProductToCart")
						.contentType(APPLICATION_FORM_URLENCODED)
						.session(mockHttpSession)
						.param("u_ID", tajenwww.getU_id())
						.param("p_ID", cartItem2.getProductInfo().getP_ID() + "")
						.param("toDo", "remove"))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(true)));
	}

	@Test
	@DisplayName("POST /cart.controller/clientAddProductToCart (toDo=query) - success")
	void clientAddProductToCart_toDoEqualsQuery_success() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		mockMvc.perform(post("/cart.controller/clientAddProductToCart")
						.contentType(APPLICATION_FORM_URLENCODED)
						.session(mockHttpSession)
						.param("u_ID", tajenwww.getU_id())
						.param("p_ID", purchasedItem1.getProductInfo().getP_ID() + "")
						.param("toDo", "query"))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(false)));
	}

	@Test
	@DisplayName("POST /cart.controller/clientInitializeProductBtnFunc - alreadyBought")
	void clientInitializeProductBtnFunc_alreadyBought() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		mockMvc.perform(post("/cart.controller/clientInitializeProductBtnFunc")
						.session(mockHttpSession)
						.param("p_ID", purchasedItem1.getProductInfo().getP_ID() + "")
						.param("u_ID", tajenwww.getU_id()))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(1)));
	}

	@Test
	@DisplayName("POST /cart.controller/clientInitializeProductBtnFunc - alreadyInCart")
	void clientInitializeProductBtnFunc_alreadyInCart() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		mockMvc.perform(post("/cart.controller/clientInitializeProductBtnFunc")
						.session(mockHttpSession)
						.param("p_ID", cartItem1.getProductInfo().getP_ID() + "")
						.param("u_ID", tajenwww.getU_id()))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(2)));
	}

	@Test
	@DisplayName("POST /cart.controller/clientInitializeProductBtnFunc (onShelf)")
	void clientInitializeProductBtnFunc_onShelf() throws Exception {
		// 0. login
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		// 1. main
		mockMvc.perform(post("/cart.controller/clientInitializeProductBtnFunc")
						.session(mockHttpSession)
						.param("p_ID", shelfItem1.getP_ID() + "")
						.param("u_ID", tajenwww.getU_id()))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(3)));
	}

	@Test
	@DisplayName("GET /cart.controller/adminSelectTop100 - success")
	void adminCartSelectTop100_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(get("/cart.controller/adminSelectTop100")
						.session(mockHttpSession))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.list", hasSize(3)))
				.andExpect(jsonPath("$.list[*].cart_id", contains(
						cartItem3.getCart_id(),
						cartItem2.getCart_id(),
						cartItem1.getCart_id()
				)));
	}

	@Test
	@DisplayName("POST /cart.controller/adminSelectProduct - success")
	void adminCartSelectProduct_success() throws Exception {
		// 0. admin-login
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		// 1. main
		mockMvc.perform(post("/cart.controller/adminSelectProduct")
						.session(mockHttpSession)
						.param("p_id", shelfItem1.getP_ID() + ""))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.p_ID", is(shelfItem1.getP_ID())));
	}

	@Test
	@DisplayName("POST /cart.controller/adminSelectUser - success")
	void adminCartSelectUser_success() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(post("/cart.controller/adminSelectUser")
						.session(mockHttpSession)
						.param("u_id", tajenwww.getU_id()))

				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.u_id", is(tajenwww.getU_id())));
	}

	@Test
	@DisplayName("POST /cart.controller/adminSearchBar - success (u_id full match)")
	void adminCartSearchBar_success_uidFullMatch() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(post("/cart.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "u_id")
						.param("searchBar", tajenwww.getU_id()))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list", hasSize(3)))
				.andExpect(jsonPath("$.list[*].cart_id", containsInAnyOrder(
						cartItem1.getCart_id(),
						cartItem2.getCart_id(),
						cartItem3.getCart_id()
				)));
	}

	@Test
	@DisplayName("POST /cart.controller/adminSearchBar - success (p_name partial match)")
	void adminCartSearchBar_success_pnamePartialMatch() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		String value = cartItem1.getP_name().substring(1);
		mockMvc.perform(post("/cart.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "p_name")
						.param("searchBar", value))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list", hasSize(1)))
				.andExpect(jsonPath("$.list[*].cart_id", contains(cartItem1.getCart_id())));
	}

	@Test
	@DisplayName("POST /cart.controller/adminSearchBar - success (cart_date time range match)")
	void adminCartSearchBar_success_cartdateTimeRangeMatch() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		var now = LocalDateTime.now();
//		var from = now.minusHours(7).minusMinutes(59).format(ISO_LOCAL_DATE_TIME);
//		var from = now.minusHours(8).format(ISO_LOCAL_DATE_TIME);
		var from = now.minusDays(1).format(ISO_LOCAL_DATE_TIME);
		var to = now.plusMinutes(10).format(ISO_LOCAL_DATE_TIME);
		System.out.println("db cartItem#1 date = " + dao.find(CartItem.class, cartItem1.getCart_id()).getCart_date());
		mockMvc.perform(post("/cart.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "cart_date")
						.param("searchBar", from + "," + to))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list", hasSize(3)))
				.andExpect(jsonPath("$.list[*].cart_id", containsInAnyOrder(
						cartItem1.getCart_id(),
						cartItem2.getCart_id(),
						cartItem3.getCart_id()
				)));
	}

	@Test
	@DisplayName("POST /cart.controller/adminSearchBar - success (p_price range match)")
	void adminCartSearchBar_success_ppriceRangeMatch() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(post("/cart.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "p_price")
						.param("searchBar", "0,10000"))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.list", hasSize(3)))
				.andExpect(jsonPath("$.list[*].cart_id", containsInAnyOrder(
						cartItem1.getCart_id(),
						cartItem2.getCart_id(),
						cartItem3.getCart_id()
				)));
	}

	@Test
	@DisplayName("POST /cart.controller/adminSearchBar - invalid search condition")
	void adminCartSearchBar_whenInvalidSearchCondition_thenRequestIsRejected() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		mockMvc.perform(post("/cart.controller/adminSearchBar")
						.session(mockHttpSession)
						.param("searchBy", "what-the-fish")
						.param("searchBar", "200"))

				.andExpect(jsonPath("$.errorMessage").exists())
				.andExpect(jsonPath("$.list").isEmpty());
	}

	@Test
	@DisplayName("POST /cart.controller/deleteAdmin - success")
	void adminCartDelete_success() throws Exception {
		userTestUtils.adminLoginAsAdming5(mockHttpSession);


		String[] removes = {cartItem2.getCart_id() + "", cartItem3.getCart_id() + ""};
		mockMvc.perform(post("/cart.controller/deleteAdmin")
						.session(mockHttpSession)
						.param("cart_ids", removes))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state", notNullValue(String.class)));
	}

	@Test
	@DisplayName("POST /cart.controller/checkout (To ECPay page) - success")
	void payViaEcpay_success() throws Exception {
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		var p_ids = Stream.of(cartItem1, cartItem2, cartItem3).map(ci -> ci.getProductInfo().getP_ID() + "").toArray(String[]::new);
		mockMvc.perform(post("/cart.controller/checkout")
						.session(mockHttpSession)
						.param("u_id", tajenwww.getU_id())
						.param("p_ids", p_ids))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$", containsString("action=\"https://payment-stage.ecPay.com.tw/Cashier/AioCheckOut/V5\"")))
				;
	}

	@Test
	@DisplayName("POST /cart.controller/getEcpayResultAttr - success")
	// test purpose: only to prove the endpoint exists.
	// asserts nothing: poor design using in-memory json storage instead of
	// db storage, but since it still functions atm, it needs to exist at least.
	void getEcpayResultAttr_success() throws Exception {
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		mockMvc.perform(post("/cart.controller/getEcpayResultAttr")
				.session(mockHttpSession))

				.andExpect(status().isOk());
	}
}