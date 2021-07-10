package com.group5.springboot.controller.cart;

import java.util.Arrays;
import java.util.HashMap;
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
	public Map<String, Object> adminOrderSearchBar(@RequestParam(name = "searchBy") String condition, @RequestParam(name = "searchBar") String value) {
		if ("o_status".equals(condition) || "u_id".equals(condition)) {
			// 準確查詢
			return orderService.selectBy(condition, value);
		} else if ("p_name".equals(condition) || "u_firstname".equals(condition) || "u_lastname".equals(condition)) {
			// 模糊查詢
			return orderService.selectLikeOperator(condition, value);
		} else if ("o_date".equals(condition)) {
			// 日期範圍查詢
			// 隨時可換
			String regex = ","; 
			String[] dates = value.split(regex);
			String startDateString = dates[0].split("T")[0] + " " + dates[0].split("T")[1];
			String endDateString = dates[1].split("T")[0] + " " + dates[1].split("T")[1];
			// ❗ ❓ 這邊寫得頗爛，感覺要用更通用的方法拆(轉)格式
			return orderService.selectWithTimeRange(startDateString, endDateString);
		} else if ("o_id".equals(condition) || "p_id".equals(condition) || "o_amt".equals(condition)) {
			// 數值範圍查詢
			// 隨時可換
			String regex = ",";
			String[] numberStrings = value.split(regex);
			Integer minValue = Integer.parseInt(numberStrings[0]);
			Integer maxValue = Integer.parseInt(numberStrings[1]);
			System.out.println("min = " + minValue + " ;max = " + maxValue);
			return orderService.selectWithNumberRange(condition, minValue, maxValue);
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