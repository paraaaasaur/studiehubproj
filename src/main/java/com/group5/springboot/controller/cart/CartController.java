package com.group5.springboot.controller.cart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.cart.CartItemService;
import com.group5.springboot.service.cart.OrderService;
import com.group5.springboot.service.product.ProductServiceImpl;
import com.group5.springboot.service.user.UserService;

@RestController
public class CartController {
	@Autowired // SDI ✔
	private OrderService orderService;
	@Autowired // SDI ✔
	private ProductServiceImpl productService;
	@Autowired // SDI ✔
	private UserService userService;
	@Autowired // SDI ✔
	private CartItemService cartItemService;


	/***************************************************************************** */
	@GetMapping(value="/test00")
	public List<OrderInfo> test00() {
		List<OrderInfo> list = orderService.test();
		list.forEach(System.out::println);
		return list;
	}
	
	/***************************************************************************** */
	@PostMapping(value="/cart.controller/clientShowCart")
	public List<Map<String, Object>> clientShowCart(@RequestParam String u_id) {
		List<Map<String, Object>> cart = cartItemService.getCart(u_id);
		cart.forEach(System.out::println);
		return cart;
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/clientRemoveProductFromCart", produces = "application/json; charset=UTF-8")
	public List<Map<String, Object>> clientRemoveProductFromCart(@RequestParam Integer[] p_ids, @RequestParam String u_id) {
		Arrays.asList(p_ids).forEach(p_id -> cartItemService.deleteASingleProduct(u_id, p_id));
		return cartItemService.getCart(u_id);
	}

	/***************************************************************************** */
	@GetMapping(value = "/order.controller/adminSelectTop100", produces = "application/json; charset=UTF-8")
	public Map<String, Object> adminOrderSelectTop100(){
		return orderService.selectTop100();
	}
	
	/***************************************************************************** */
	@GetMapping(value = "/order.controller/adminSelectAll", produces = "application/json; charset=UTF-8")
	public Map<String, Object> adminOrderSelectAll(){
		return orderService.selectAll();
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/order.controller/adminSelectProduct")
	public ProductInfo adminOrderSelectProduct(@RequestParam("p_id") String p_id) {
		return productService.findByProductID(Integer.parseInt(p_id));
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/order.controller/adminSelectUser")
	public User_Info adminOrderSelectUser(@RequestParam("u_id") String u_id) {
		return userService.getSingleUser(u_id);
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/order.controller/adminSearchBar")
	public Map<String, Object> adminOrderSearchBar(@RequestParam String searchBy, @RequestParam String searchBar) {
		
		if ("o_id".equals(searchBy) || "p_id".equals(searchBy) || "o_status".equals(searchBy) || "u_id".equals(searchBy)) {
			return orderService.selectBy(searchBy, searchBar);
		} else if ("p_name".equals(searchBy) || "u_firstname".equals(searchBy) || "u_lastname".equals(searchBy)) {
			return orderService.selectLikeOperator(searchBy, searchBar);
		} else if ("o_date".equals(searchBy)) {
			System.out.println("o_date查詢還沒寫好");
			return null;
		} else if ("o_amt".equals(searchBy)) {
			System.out.println("o_amt查詢還沒寫好");
			return null;
		}
		
		return null;
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/order.controller/insertAdmin")
	public Map<String, Object> adminOrderInsert(@RequestBody OrderInfo order) {
		Map<String, Object> map = orderService.insert(order);
		boolean insertStatus = (map != null)? true : false;
		String msg = (insertStatus)? "新增成功！" : "新增失敗 :^)";
		map.put("state", msg);
		
		return map;
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/order.controller/deleteAdmin")
	public Map<String, String> adminOrderDelete(@RequestParam String o_id) {
		
		OrderInfo order = new OrderInfo();
		order.setO_id(Integer.parseInt(o_id));
		Boolean deleteStatus = (orderService.delete(order))? true : false;
		String msg = "oid = " + o_id + ((deleteStatus)?  "：刪除成功✔" : "：刪除失敗❌");
		HashMap<String, String> resultMap = new HashMap<>();
		resultMap.put("state", msg);
		resultMap.put("status", deleteStatus.toString());
		
		return resultMap;
	}
	
	/***************************************************************************** */
	
	
}