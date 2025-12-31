package com.group5.springboot.controller.cart;

import com.group5.springboot.dto.cart.AdminCreateCartItemRequest;
import com.group5.springboot.dto.cart.ECPayPaymentResult;
import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class CartAndOrderTestUtils {
	private final MockMvc mockMvc;


	public CartAndOrderTestUtils(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	public void adminAddsCartItem(ProductInfo dbProduct, User_Info dbUser, MockHttpSession mockHttpSession) throws Exception {
		// 0. validation
		assertNotNull(mockHttpSession);
		assertNotNull(mockHttpSession.getAttribute("adminId"), "requires admin session");
	}

	/**
	 * A method to construct a {@link CartItem} holding exactly enough
	 * information for cart service to save to the database.
	 * @since 1.0.0
	 **/
	public static AdminCreateCartItemRequest aCartItemDto(ProductInfo product, User_Info customer) {
		return new AdminCreateCartItemRequest(product, customer);
	}

	/**
	 * <li>A mock ECPay payment result DTO for credit card payment.</li>
	 * <li>Mock values may contain potential gibberish which is not compatible with ECPay server side</li>
	 **/
	public static ECPayPaymentResult aSuccessfulECPayResult(User_Info customer) {
		// convenience constants
		var dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		var PaymentDate = LocalDateTime.now();
		var TradeDate = PaymentDate.minusMinutes(1);

		var builder = ECPayPaymentResult.Builder.newBuilder();

		return builder
				.CustomField1(customer.getU_id())
				.CustomField2(customer.getU_lastname() + customer.getU_firstname())
				.MerchantID("mock-merchant-id-9999999") // gibberish
				.MerchantTradeNo("studiehub-demo-no-" + UUID.randomUUID())
				.PaymentDate(PaymentDate.format(dateFormat))
				.PaymentType("Credit_CreditCard")
				.PaymentTypeChargeFee("5")
				.RtnCode("1") // 1 == successful
				.RtnMsg("交易成功")
				.SimulatePaid("0")
				.TradeAmt("200")
				.TradeDate(TradeDate.format(dateFormat))
				.TradeNo("mock-trade-no-9999999") // gibberish
				.CheckMacValue("mock-check-mac-value-9999999") // gibberish
				.build();

		// sample println output:
		/*
		 * CustomField1 : ken
		 * CustomField2 : 黃粉紅
		 * CustomField3 :
		 * CustomField4 :
		 * MerchantID : 2000132
		 * MerchantTradeNo : studiehub25122852336
		 * PaymentDate : 2025/12/28 20:27:59
		 * PaymentType : Credit_CreditCard
		 * PaymentTypeChargeFee : 5
		 * RtnCode : 1
		 * RtnMsg : 交易成功
		 * SimulatePaid : 0
		 * StoreID :
		 * TradeAmt : 200
		 * TradeDate : 2025/12/28 20:27:22
		 * TradeNo : 2512282027223916
		 * CheckMacValue : 1BD5AD220F037028251BAB84B73A0A04F76E5B7CFD269F7A714E86189F527794
		 */
	}
}