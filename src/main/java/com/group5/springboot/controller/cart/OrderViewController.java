package com.group5.springboot.controller.cart;


import com.group5.springboot.annotation.auth.RequiresAdmin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderViewController {
	@RequiresAdmin
	@GetMapping(value = {"/order.controller/adminSelect"})
	public String toCartAdminSelect() {
		return "cart/orderAdminSelect";
	}
}