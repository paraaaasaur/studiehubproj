package com.group5.springboot.controller.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group5.springboot.controller.user.UserTestUtils;
import com.group5.springboot.dao.test.GenericDao;
import com.group5.springboot.dto.cart.ECPayPaymentResult;
import com.group5.springboot.model.cart.CartItem;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.group5.springboot.controller.cart.CartAndOrderTestUtils.aSuccessfulECPayResult;
import static com.group5.springboot.controller.product.ProductTestUtils.aRandomProduct;
import static com.group5.springboot.controller.user.UserTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ECPayControllerTest {
	private final MockMvc mockMvc;
	private final GenericDao dao;
	private final UserTestUtils userTestUtils;

	private MockHttpSession mockHttpSession;

	private User_Info tajenwww;
	private ECPayPaymentResult paymentResult1;


	@Autowired
	ECPayControllerTest(MockMvc mockMvc, GenericDao dao) {
		this.mockMvc = mockMvc;
		this.dao = dao;
		this.userTestUtils = new UserTestUtils(this.mockMvc);
		this.tajenwww = dao.find(User_Info.class, "tajenwww");
	}


	@BeforeEach
	void setUp() {
		this.mockHttpSession = new MockHttpSession();

		// set up test data & dependencies
		this.tajenwww = dao.save(aUserTajenwww());
		this.paymentResult1 = aSuccessfulECPayResult(tajenwww);
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
		this.paymentResult1 = null;
	}

	@Test
	@DisplayName("POST /payment/ecpay/callback - success")
	void paymentResultWebhook_success() throws Exception {
		userTestUtils.loginAs(tajenwww, mockHttpSession);


		mockMvc.perform(post("/payment/ecpay/callback")
						.contentType(APPLICATION_FORM_URLENCODED)
						.session(mockHttpSession)
						.param("CustomField1", paymentResult1.getCustomField1())
						.param("CustomField2", paymentResult1.getCustomField2())
						.param("CustomField3", paymentResult1.getCustomField3())
						.param("CustomField4", paymentResult1.getCustomField4())
						.param("MerchantID", paymentResult1.getMerchantID())
						.param("MerchantTradeNo", paymentResult1.getMerchantTradeNo())
						.param("PaymentDate", paymentResult1.getPaymentDate())
						.param("PaymentType", paymentResult1.getPaymentType())
						.param("PaymentTypeChargeFee", paymentResult1.getPaymentTypeChargeFee())
						.param("RtnCode", paymentResult1.getRtnCode())
						.param("RtnMsg", paymentResult1.getRtnMsg())
						.param("SimulatePaid", paymentResult1.getSimulatePaid())
						.param("StoreID", paymentResult1.getStoreID())
						.param("TradeAmt", paymentResult1.getTradeAmt())
						.param("TradeDate", paymentResult1.getTradeDate())
						.param("TradeNo", paymentResult1.getTradeNo())
						.param("CheckMacValue", paymentResult1.getCheckMacValue()))

				.andExpect(status().isOk());


		// assert side effects
		// 1. order is persisted
		// 2. cart is deleted
		// 3. payment result is saved (in-memory atm)
	}
}