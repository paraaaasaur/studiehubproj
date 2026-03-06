package com.group5.springboot.controller.cart;

import com.group5.springboot.dto.cart.ECPayPaymentResult;
import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.cart.CartItemService;
import com.group5.springboot.service.cart.OrderService;
import com.group5.springboot.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * A webhook class that has provides receivers required by ECPay API spec
 * to call and "push" API payment result to, and, notably, NOT as usual
 * controller endpoints for our internal uses requested by views.
 **/
@Controller
public class ECPayController {
	private final OrderService orderService;
	private final UserService userService;
	private final CartItemService cartItemService;


	@Autowired
	public ECPayController(OrderService orderService, UserService userService, CartItemService cartItemService) {
		this.orderService = orderService;
		this.userService = userService;
		this.cartItemService = cartItemService;
	}


	/**
	 * @client ECPay server (NOT frontend views of our app)
	 * @trigger Payment result notification
	 * @method: POST
	 * @response-body Ignored by client
	 * @workflow receives API payment result -> parses and saves into order
	 * in database -> put JSON into in-memory, public static map for the follow-up
	 * onload request from redirected result view {@code cart/cartClientResultPage}
	 */
	@PostMapping(value = "/payment/ecpay/callback", consumes = APPLICATION_FORM_URLENCODED_VALUE)
	// Can't use @RequestBody since ECPay sends application/x-www-form-urlencoded,
	// not application/json :(
	public void paymentResultWebhook(ECPayPaymentResult dto) {
		boolean success = "Credit_CreditCard".equals(dto.getPaymentType())
						  && "1".equals(dto.getRtnCode());
		// ❗ 信用卡以外的成功判定都還沒設計
		String u_id = dto.getCustomField1();
		if (success) {
			// (i) OrderInfo part
			// 取得自訂oid 【統一值】
			Integer o_id = orderService.getCurrentIdSeed();
			String o_status = "完成";
			Integer o_amt = Integer.parseInt(dto.getTradeAmt());
			String ecpay_o_id = dto.getMerchantTradeNo();
			String ecpay_trade_no = dto.getTradeNo();
			// (ii) User_Info part 【統一值】
			User_Info user = userService.getSingleUser(u_id);
			// (iii) ProductInfo part 【個別值】
			@SuppressWarnings("unchecked")
			List<ProductInfo> tempCart = (List<ProductInfo>) (CartViewController.cartInfoMap.get(u_id));

			// 把結帳完的購物車內容正式存進資料表order_info
			// ❗ identity 和 o_date 值多少都沒影響
			tempCart.forEach(product -> orderService.insert(new OrderInfo(0, o_id, "dateinfo", o_amt, ecpay_o_id, o_status, ecpay_trade_no, // order相關
					product.getP_ID(), product.getP_Name(), product.getP_Price(), // product相關
					user.getU_id(), user.getU_firstname(), user.getU_lastname(), user.getU_email()))); // user相關

			// 把結帳完的購物車內容從資料表cart_item移除
			cartItemService.deleteByUserId(u_id);

		}

		CartViewController.cartInfoMap.remove(u_id);
		CartViewController.cartInfoMap.put("ecpayResultAttr", dto);
	}
}