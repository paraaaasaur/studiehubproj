package com.group5.springboot.controller.cart;

import java.util.ArrayList;
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
import com.group5.springboot.service.cart.CartItemService;
import com.group5.springboot.validate.CartValidator;

@Controller
public class CartViewController {
	@Autowired
	private CartItemService cartItemService;
	@Autowired
	private CartValidator cartValidator;
	
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
			List<ObjectError> list = result.getAllErrors();
			list.forEach(objectError -> System.out.println("有錯誤：" + objectError));
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
	@PostMapping(value = {"/cart.controller/cartCheckout"})
	public String toCartCheckout() {
		return "cart/cartCheckout";
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

//		Boolean success = ("Credit_CreditCard".equals(paymentType) && "1".equals(rtnCode))? true : false;
		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("ecpayResult", ecpayResult);
//		map.put("success", success);
		// ❗ 要移除這個
//		cartInfo.put("ecpayResultAttr", map);
		cartInfoMap.put("ecpayResultAttr", map);
		
		return;
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = "/cart.controller/clientResultPage")
	public String toClientResultPage(
			@RequestParam Map<String, String> map
//			, Model model
			) {
		System.out.println("*********************** 回傳結果如下 **************************");
		map.forEach((paramName, paramValue) -> System.out.println(paramName + " : " + paramValue));
		System.out.println("*********************** 回傳結果如上 **************************");
		
		
		return "cart/cartClientResultPage";
	}
	
	

}