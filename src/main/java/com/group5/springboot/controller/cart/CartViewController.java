package com.group5.springboot.controller.cart;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.dto.cart.*;
import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.service.cart.CartItemService;
import com.group5.springboot.service.product.ProductService;
import com.group5.springboot.service.user.UserService;
import com.group5.springboot.validate.CartValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@Controller
public class CartViewController {
	private final CartItemService cartItemService;
	private final CartValidator cartValidator;
	private final ProductService productService;
	private final UserService userService;
	// fixme: literally in-memory tables... 
	//  just use a real cart table and a 3rd-party result table plz 
	/**
	 * What does this do?<br>
	 * (1) store cart temporarily until payment process completes
	 * (2) store 3rd-party payment result temporarily until it's shown to users 
	 **/
	public static HashMap<String, Object> cartInfoMap = new HashMap<>();


	@Autowired
	public CartViewController(CartItemService cartItemService, CartValidator cartValidator, ProductService productService, UserService userService) {
		this.cartItemService = cartItemService;
		this.cartValidator = cartValidator;
		this.productService = productService;
		this.userService = userService;
	}


	@RequiresAdmin
	@GetMapping(value = {"/cart.controller/adminInsert"})
	public String toCartAdminInsert(Model model) {
		addModelAttributes(model);
		return "cart-items/admin/add";
	}

	@RequiresAdmin
	@PostMapping(value = {"/cart.controller/adminInsert"})
	public String cartAdminInsert(
			@ModelAttribute("createCartItemView") CreateCartItemRequest req,
			RedirectAttributes ra, Model model
	) {
		var result = cartValidator.validate(req);
		if (result.hasErrors()) {
			result.getAllErrors().forEach(System.err::println);
			readdModelAttributes(model, req, result);
			return "cart-items/admin/add";
		}

		Integer cartId = cartItemService.insert2(req.getP_id(), req.getU_id())
				.getCart_id();
		ra.addFlashAttribute("successMessage", "購物車項目編號 = " + cartId + "新增成功！");
		return "redirect:/cart.controller/adminSelect";
	}

	@RequiresAdmin
	@GetMapping(value = {"/cart.controller/adminUpdate/{cartid}"})
	public String toCartAdminUpdate(@PathVariable("cartid") Integer cartItemId, Model model) {
		addModelAttributes(model, cartItemId);
		return "cart-items/admin/edit";
	}

	@RequiresAdmin
	@PostMapping(value = {"/cart.controller/adminUpdate/{cartid}"})
	public String cartAdminUpdate(
			@ModelAttribute("updateCartItemView") UpdateCartItemRequest req,
			RedirectAttributes ra, Model model
	) {
		var result = cartValidator.validate(req);
		if (result.hasErrors()) {
			result.getAllErrors().forEach(System.err::println);
			readdModelAttributes(model, req, result);
			return "cart-items/admin/edit";
		}
		
		try {
			cartItemService.update2(req);
			ra.addFlashAttribute("successMessage", "cart_id = " + req.getCart_id() + "修改成功");
		} catch (Exception e) {
			ra.addFlashAttribute("successMessage", "cart_id = " + req.getCart_id() + "修改失敗");
		}
		
		return "redirect:/cart.controller/adminSelect";
	}

	@RequiresUser
	@GetMapping(value = {"/cart.controller/cartIndex"})
	public String toCartIndex() {
		return "cart-items/my-list";
	}
	
	@RequiresAdmin
	@GetMapping(value = {"/cart.controller/adminSelect"})
	public String toCartAdminSelect() {
		return "cart-items/admin/list";
	}
	
	@RequiresUser
	@GetMapping(value = "/cart.controller/clientResultPage")
	public String toClientResultPage() {
		return "cart-items/payment-result";
	}


	// convenience methods
	private void addModelAttributes(Model model) {
		model.addAttribute("createCartItemView", CreateCartItemView.newInstance());
	}

	private void addModelAttributes(Model model, Integer cartItemId) {
		model.addAttribute("updateCartItemView", getView(cartItemId));
	}

	private void readdModelAttributes(Model model, CreateCartItemRequest req, BindingResult result) {
		model.addAttribute("createCartItemView", assembleView(req));
		model.addAttribute(MODEL_KEY_PREFIX + "createCartItemView", result);
	}

	private void readdModelAttributes(Model model, UpdateCartItemRequest req, BindingResult result) {
		model.addAttribute("updateCartItemView", assembleView(req));
		model.addAttribute(MODEL_KEY_PREFIX + "updateCartItemView", result);
	}

	// facades
	private UpdateCartItemView getView(Integer cartItemId) {
		var cartItem = cartItemService.find(cartItemId);
		return adaptToView(cartItem);
	}
	
	// todo: what are the exception flows here
	// adapters?
	private CreateCartItemView assembleView(CreateCartItemRequest req) {
		final String u_id = req.getU_id();
		final Integer p_id = req.getP_id();
		
		var view = CreateCartItemView.newInstance();
		// if user/product ids were valid user inputs (not null/blank),
		// fetch related fields.
		// if successfully fetched, put data back to the view for representation.
		if (p_id != null) {
			var dbProduct = productService.findByProductID(p_id);
			
			if (dbProduct != null) {
				view = view.withP_id(p_id)
						.withP_name(dbProduct.getP_Name())
						.withP_price(dbProduct.getP_Price());
			}
		}
		if (u_id != null && !u_id.isBlank()) {
			var dbUser = userService.getSingleUser(u_id);
			
			if (dbUser != null) {
				view = view.withU_id(u_id)
						.withU_firstname(dbUser.getU_firstname())
						.withU_lastname(dbUser.getU_lastname());
			}
		}
		
		return view;
	}

	private UpdateCartItemView adaptToView(CartItem entity) {
		var view = UpdateCartItemView.newInstance(entity.getCart_id());
		
		return view
				.withP_id(entity.getP_id())
				.withP_name(entity.getP_name())
				.withP_price(entity.getP_price())
				.withU_id(entity.getU_id())
				.withU_firstname(entity.getU_firstname())
				.withU_lastname(entity.getU_lastname())
				.withCart_date(entity.getCart_date());
	}

	private UpdateCartItemView assembleView(UpdateCartItemRequest req) {
		final Integer p_id = req.getP_id();
		final String u_id = req.getU_id();
		final Integer cartItemId = req.getCart_id();


		var view = UpdateCartItemView.newInstance(cartItemId);
		// if user/product ids were valid user inputs (not null/blank),
		// fetch related fields.
		// if successfully fetched, put data back to the view for representation.
		if (p_id != null) {
			var dbProduct = productService.findByProductID(p_id);
			
			if (dbProduct != null) {
				view = view.withP_id(p_id)
						.withP_name(dbProduct.getP_Name())
						.withP_price(dbProduct.getP_Price());
			}
		}
		if (u_id != null && !u_id.isBlank()) {
			var dbUser = userService.getSingleUser(u_id);
			
			if (dbUser != null) {
				view = view.withU_id(u_id)
						.withU_firstname(dbUser.getU_firstname())
						.withU_lastname(dbUser.getU_lastname());
			}
		}
		var cartItem = cartItemService.find(cartItemId);
		view = view.withCart_date(cartItem.getCart_date());
		
		return view;
	}
}