package com.group5.springboot.controller.cart;

import java.util.ArrayList;
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
import com.group5.springboot.service.cart.OrderService;
import com.group5.springboot.service.product.ProductServiceImpl;
import com.group5.springboot.service.user.UserService;

@RestController
public class OrderController {
	@Autowired // SDI ✔
	private OrderService orderService;
	@Autowired // SDI ✔
	private ProductServiceImpl productService;
	@Autowired // SDI ✔
	private UserService userService;
	
	@GetMapping("/order.controller/test/selectOrderInfoByOPUJoin")
	public List<OrderInfo> testSelectOrderInfoByOPUJoin() {
		return orderService.selectOrderInfoByOPUJoin();
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
		ProductInfo pBeanInDb = productService.findByProductID(Integer.parseInt(p_id));
		System.out.println(pBeanInDb);
		return pBeanInDb; 
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/order.controller/adminSelectUser")
	public User_Info adminOrderSelectUser(@RequestParam("u_id") String u_id) {
		return userService.getSingleUser(u_id);
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/order.controller/adminSearchBar")
	public Map<String, Object> adminOrderSearchBar(
			@RequestParam(name = "searchBy") String condition
			, @RequestParam(name = "searchBar") String value
			) {
		try {
			
			if ("o_status".equals(condition) || "u_id".equals(condition) || "u_email".equals(condition) || "ecpay_o_id".equals(condition)) {
				// (1) 準確查詢
				return orderService.selectBy(condition, value);
			} else if ("p_name".equals(condition) || "u_firstname".equals(condition) || "u_lastname".equals(condition)) {
				// (2) 模糊查詢
				return orderService.selectLikeOperator(condition, value);
			} else if ("o_date".equals(condition)) {
				// (3) 日期範圍查詢
				// 隨時可換
				String regex = ","; 
				String[] dates = value.split(regex);
				String startDateString = dates[0].split("T")[0] + " " + dates[0].split("T")[1];
				String endDateString = dates[1].split("T")[0] + " " + dates[1].split("T")[1];
				// ❗ ❓ 這邊寫得頗爛，感覺要用更通用的方法拆(轉)格式
				return orderService.selectWithTimeRange(startDateString, endDateString);
			} else if ("o_id".equals(condition) || "p_id".equals(condition) || "o_amt".equals(condition) || "p_price".equals(condition)) {
				// (4) 數值範圍查詢
				// 隨時可換
				String regex = ",";
				String[] numberStrings = value.split(regex);
				Integer minValue = 0;
				Integer maxValue = 0;
				minValue = Integer.parseInt(numberStrings[0]);
				maxValue = Integer.parseInt(numberStrings[1]);
				System.out.println("min = " + minValue + " ;max = " + maxValue);
				return orderService.selectWithNumberRange(condition, minValue, maxValue);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("errorMessage", "查詢出錯");
		map.put("list", new ArrayList<OrderInfo>());
		return map;
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/order.controller/insertAdmin")
	public Map<String, Object> adminOrderInsert(@RequestBody OrderInfo order) {
		Map<String, Object> map = orderService.insert(order);
		String msg = (map.get("errorMessage") == null)? "新增成功！" : "新增失敗 :^)";
		map.put("state", msg);
		
		return map;
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/order.controller/deleteAdmin")
	public Map<String, String> adminOrderDelete(@RequestParam Integer[] identitySeeds) {
		orderService.delete(identitySeeds);
		HashMap<String, String> map = new HashMap<>();
		map.put("state", "資料刪除完成。");
		return map;
	}
	
	/***************************************************************************** */
	
	
}