package com.group5.springboot.controller.cart;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.service.cart.OrderService;

@SessionAttributes(names = "cart")
@RestController
@RequestMapping(path = "")
public class CartController {
	@Autowired // SDI ✔
	private OrderService orderService;
	public List<ProductInfo> cart = new ArrayList<ProductInfo>();

	/***************************************************************************** */
	@SuppressWarnings("unchecked")
	@GetMapping(value="/test00")
	public List<ProductInfo> test00() {
		return (List<ProductInfo>) orderService.test().get("list");
	}
	
	/***************************************************************************** */
	@SuppressWarnings("unchecked")
	@GetMapping(value="/cart.controller/showCart", produces = "application/json; charset=UTF-8")
	public List<ProductInfo> showCart(HttpSession session) {
		session.setAttribute("cart", cart);
		cart = (ArrayList<ProductInfo>) session.getAttribute("cart");
		System.out.println("*** 現在正在showCart()方法內 ***");
		System.out.println("cart = " + cart);
		System.out.println("*** showCart()方法結束 ***");
		return cart;
	}
	
	/***************************************************************************** */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/cart.controller/remove", produces = "application/json; charset=UTF-8")
	public List<ProductInfo> removeProductFromCart(@RequestParam String[] ckboxValues, HttpSession session) {
		cart = (ArrayList<ProductInfo>) session.getAttribute("cart");
		System.out.println(ckboxValues);
		for (int i = 0; i < ckboxValues.length; i++) {
			int ckIndex = Integer.parseInt(ckboxValues[i]);
			cart.remove(ckIndex - i);
			System.out.println(ckboxValues[i]);
		}
		session.setAttribute("cart", cart);
		return cart;
	}

	/***************************************************************************** */
	@GetMapping(value = "/cart.controller/adminSelectTop20", produces = "application/json; charset=UTF-8")
	public Map<String, Object> adminSelectTop20(){
		return orderService.selectTop20();
	}
	
	/***************************************************************************** */
	@GetMapping(value = "/cart.controller/adminSelectTop100", produces = "application/json; charset=UTF-8")
	public Map<String, Object> adminSelectTop100(){
		return orderService.selectTop100();
	}
	
	/***************************************************************************** */
	@GetMapping(value = "/cart.controller/adminSelectAll", produces = "application/json; charset=UTF-8")
	public Map<String, Object> adminSelectAll(){
		return orderService.selectAll();
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/adminSearchBar")
	public Map<String, Object> adminSearchBar(@RequestParam String searchBy, @RequestParam String searchBar) {
		return orderService.selectLikeOperator(searchBy, searchBar);
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/insertAdmin")
	public Map<String, Object> adminInsert(@RequestBody OrderInfo order) {
		Map<String, Object> map = orderService.insert(order);
		boolean insertStatus = (map != null)? true : false;
		String msg = (insertStatus)? "新增成功！" : "新增失敗 :^)";
		map.put("state", msg);
		
		return map;
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/updateAdmin")
	public Map<String, Object> adminUpdate(@RequestBody OrderInfo order) {
		// ❗ 之後加上一圈先檢查 FK 的機制
		HashMap<String, Object> map = new HashMap<>();
		boolean updateStatus = (orderService.update(order))? true : false;
		String msg = "oid = " + order.getO_id() + ((updateStatus)?  "：修改成功✔" : "：修改失敗❌");
		map.put("state", msg);
		
		return map;
	}

	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/deleteAdmin")
	public Map<String, String> adminDelete(@RequestParam String o_id) {
		
		OrderInfo order = new OrderInfo();
		order.setO_id(Integer.parseInt(o_id));
		Boolean deleteStatus = (orderService.delete(order))? true : false;
		String msg = "oid = " + o_id + ((deleteStatus)?  "：刪除成功✔" : "：刪除失敗❌");
		HashMap<String, String> resultMap = new HashMap<>();
		resultMap.put("state", msg);
		resultMap.put("status", deleteStatus.toString());
		
		return resultMap;
	}
	
}