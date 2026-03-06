package com.group5.springboot.controller.cart;

import java.util.HashMap;
import java.util.List;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.service.cart.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.validate.CartValidator;

@Controller
public class CartViewController {
	private final CartItemService cartItemService;
	private final CartValidator cartValidator;
	// fixme: literally in-memory db...
	public static HashMap<String, Object> cartInfoMap = new HashMap<>();


	@Autowired
	public CartViewController(CartItemService cartItemService, CartValidator cartValidator) {
		this.cartItemService = cartItemService;
		this.cartValidator = cartValidator;
	}


	@RequiresAdmin
	@GetMapping(value = {"/cart.controller/adminInsert"})
	public String toCartAdminInsert(Model model) {
		model.addAttribute("emptyCartItem", new CartItem());
		return "cart/cartAdminInsert";
	}
	
	@RequiresAdmin
	@PostMapping(value = {"/cart.controller/adminInsert"})
	public String cartAdminInsert(
			@ModelAttribute("emptyCartInfo") CartItem cartItem,
			BindingResult result, 
			RedirectAttributes ra
	) {
		cartValidator.validate(cartItem, result);
		if (result.hasErrors()) {			
			result.getAllErrors().forEach(objectError -> System.out.println("有錯誤：" + objectError));
			return "cart/cartAdminInsert";
		}
		
		cartItemService.insert(cartItem.getP_id(), cartItem.getU_id());
		ra.addFlashAttribute("successMessage", "購物車項目編號 = " + cartItem.getCart_id() + "新增成功！");
		return "redirect:/cart.controller/adminSelect";
	}
	
	@RequiresAdmin
	@GetMapping(value = {"/cart.controller/adminUpdate/{cartid}"})
	public String toCartAdminUpdate(@PathVariable("cartid") Integer cartid, Model model) {
		model.addAttribute("cartItem", cartItemService.select(cartid).get("cartItem"));
		return "cart/cartAdminUpdate";
	}
	
	@RequiresAdmin
	@PostMapping(value = {"/cart.controller/adminUpdate/{cartid}"})
	public String cartAdminUpdate(
			@ModelAttribute(name = "cartItem") CartItem cartItem,
			BindingResult result, 
			RedirectAttributes ra
	) {
		cartValidator.validate(cartItem, result);
		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			list.forEach(objectError -> System.out.println("有錯誤：" + objectError));
			return "cart/cartAdminUpdate";
		}

		Integer updateStatus = cartItemService.update(cartItem.getU_id(), cartItem.getP_id(), cartItem.getCart_id());
		String successMessage = (updateStatus == 1)? "o_id = " + cartItem.getCart_id() + "修改成功" : "修改失敗";
		ra.addFlashAttribute("successMessage", successMessage);
		return "redirect:/cart.controller/adminSelect";
	}
	
	@RequiresUser
	@GetMapping(value = {"/cart.controller/cartIndex"})
	public String toCartIndex() {
		return "cart/cartIndex";
	}
	
	@RequiresAdmin
	@GetMapping(value = {"/cart.controller/adminSelect"})
	public String toCartAdminSelect() {
		return "cart/cartAdminSelect";
	}
	
	@RequiresUser
	@GetMapping(value = "/cart.controller/clientResultPage")
	public String toClientResultPage() {
		return "cart/cartClientResultPage";
	}
}