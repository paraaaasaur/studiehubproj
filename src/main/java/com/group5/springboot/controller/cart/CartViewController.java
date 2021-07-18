package com.group5.springboot.controller.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.cart.CartItemService;
import com.group5.springboot.service.cart.OrderService;
import com.group5.springboot.service.user.UserService;
import com.group5.springboot.validate.CartValidator;

@Controller
public class CartViewController {
	@Autowired
	private CartItemService cartItemService;
	@Autowired
	private CartValidator cartValidator;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	// ❗ 比起static attribute感覺應該要放在更好的地方...sessionAttribute之類，或直接讀DB / 真實檔案？
	public static HashMap<String, Object> cartInfoMap = new HashMap<>();
	
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = {"/cart.controller/adminInsert"})
	public String toCartAdminInsert(Model model) {
		model.addAttribute("emptyCartItem", new CartItem());
		return "/cart/cartAdminInsert";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@PostMapping(value = {"/cart.controller/adminInsert"})
	public String cartAdminInsert(@ModelAttribute("emptyCartInfo") CartItem cartItem,
			BindingResult result, 
			RedirectAttributes ra) {
		
		cartValidator.validate(cartItem, result);
		if (result.hasErrors()) {			
			result.getAllErrors().forEach(objectError -> System.out.println("有錯誤：" + objectError));
			return "/cart/cartAdminInsert";
//			return "redirect:/cart.controller/adminInsert"; // ❓
		}
		
		cartItemService.insert(cartItem.getP_id(), cartItem.getU_id());
		ra.addFlashAttribute("successMessage", "購物車項目編號 = " + cartItem.getCart_id() + "新增成功！");
		return "redirect:/cart.controller/adminSelect";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = {"/cart.controller/adminUpdate/{cartid}"})
	public String toCartAdminUpdate(@PathVariable("cartid") Integer cartid, Model model) {
		model.addAttribute("cartItem", cartItemService.select(cartid).get("cartItem"));
		return "/cart/cartAdminUpdate";
//		return "redirect:/cart.controller/adminUpdate"; // ❓
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@PostMapping(value = {"/cart.controller/adminUpdate/{cartid}"})
	public String cartAdminUpdate(@ModelAttribute(name = "cartItem") CartItem cartItem,
			BindingResult result, 
			RedirectAttributes ra) {
		
		cartValidator.validate(cartItem, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			list.forEach(objectError -> System.out.println("有錯誤：" + objectError));
			return "/cart/cartAdminUpdate";
		}
		System.out.println(cartItem);
		Integer updateStatus = cartItemService.update(cartItem.getU_id(), cartItem.getP_id(), cartItem.getCart_id());
		String successMessage = (updateStatus == 1)? "o_id = " + cartItem.getCart_id() + "修改成功" : "修改失敗";
		ra.addFlashAttribute("successMessage", successMessage);
		return "redirect:/cart.controller/adminSelect";
		
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = {"/cart.controller/cartIndex"})
	public String toCartIndex() {
		return "cart/cartIndex";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = {"/cart.controller/adminSelect"})
	public String toCartAdminSelect() {
		return "cart/cartAdminSelect";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@PostMapping(value = "/cart.controller/receiveEcpayReturnInfo")
	public void toReceiveEcpayReturnInfo(
			@RequestParam Map<String, String> map
//			, Model model
			) {
		System.out.println("*********************** 回傳結果如下(2) **************************");
		map.forEach((paramName, paramValue) -> System.out.println(paramName + " : " + paramValue));
		System.out.println("*********************** 回傳結果如上(2) **************************");
		String rtnCode = map.get("RtnCode");
		String paymentType = map.get("PaymentType");

		Boolean success = ("Credit_CreditCard".equals(paymentType) && "1".equals(rtnCode))? true : false;
		// ❗ 信用卡以外的成功判定都還沒設計
		String u_id = (String) map.get("CustomField1");
		if (success) {
			// (i) OrderInfo part
			// 取得自訂oid 【統一值】
			Integer o_id = orderService.getCurrentIdSeed();
			String o_status = "完成";
			Integer o_amt = Integer.parseInt(map.get("TradeAmt"));
			String ecpay_o_id = map.get("MerchantTradeNo");
			String ecpay_trade_no = map.get("TradeNo");
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
		
		cartInfoMap.remove(map.get(u_id));
		cartInfoMap.put("ecpayResultAttr", map);
		
		return;
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = "/cart.controller/clientResultPage")
	public String toClientResultPage() {
		return "cart/cartClientResultPage";
	}
	
	

}