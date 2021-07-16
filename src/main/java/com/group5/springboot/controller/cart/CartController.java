package com.group5.springboot.controller.cart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group5.springboot.model.cart.CartItem;
import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.cart.CartItemService;
import com.group5.springboot.service.cart.OrderService;
import com.group5.springboot.service.product.ProductServiceImpl;
import com.group5.springboot.service.user.UserService;
import com.group5.springboot.utils.api.ecpay.payment.integration.AllInOne;
import com.group5.springboot.utils.api.ecpay.payment.integration.domain.AioCheckOutALL;

@RestController
public class CartController {
	@Autowired // SDI ✔
	private ProductServiceImpl productService;
	@Autowired // SDI ✔
	private UserService userService;
	@Autowired // SDI ✔
	private OrderService orderService;
	@Autowired // SDI ✔
	private CartItemService cartItemService;
	
//	/***************************************************************************** */
//	@Autowired 
//	private OrderServiceTest orderServiceTest;
//	@GetMapping(value="/test07")
//	public List test07() {
//		List list = orderServiceTest.selectAllOOPU();
//		return list; 
//	}
	
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
	@GetMapping(value = "/cart.controller/adminSelectTop100", produces = "application/json; charset=UTF-8")
	public Map<String, Object> adminCartSelectTop100(){
		return cartItemService.selectTop100();
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/adminSelectProduct")
	public ProductInfo adminCartSelectProduct(@RequestParam("p_id") String p_id) {
		return productService.findByProductID(Integer.parseInt(p_id));
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/adminSelectUser")
	public User_Info adminCartSelectUser(@RequestParam("u_id") String u_id) {
		return userService.getSingleUser(u_id);
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/adminSearchBar")
	public Map<String, Object> adminCartSearchBar(@RequestParam(name = "searchBy") String condition, @RequestParam(name = "searchBar") String value) {
		try {
			
			if ("u_id".equals(condition)) {
				// (1) 準確查詢
				return cartItemService.selectBy(condition, value);
			} else if ("p_name".equals(condition) || "u_firstname".equals(condition) || "u_lastname".equals(condition)) {
				// (2) 模糊查詢
				return cartItemService.selectLikeOperator(condition, value);
			} else if ("cart_date".equals(condition)) {
				// (3) 日期範圍查詢
				// 隨時可換
				String regex = ","; 
				String[] dates = value.split(regex);
				String startDateString = dates[0].split("T")[0] + " " + dates[0].split("T")[1];
				String endDateString = dates[1].split("T")[0] + " " + dates[1].split("T")[1];
				// ❗ ❓ 這邊寫得頗爛，感覺要用更通用的方法拆(轉)格式
				return cartItemService.selectWithTimeRange(startDateString, endDateString);
			} else if ("cart_id".equals(condition) || "p_id".equals(condition) || "p_price".equals(condition)) {
				// (4) 數值範圍查詢
				// 隨時可換
				String regex = ",";
				String[] numberStrings = value.split(regex);
				Integer minValue = 0;
				Integer maxValue = 0;
				minValue = Integer.parseInt(numberStrings[0]);
				maxValue = Integer.parseInt(numberStrings[1]);
				System.out.println("min = " + minValue + " ;max = " + maxValue);
				return cartItemService.selectWithNumberRange(condition, minValue, maxValue);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("errorMessage", "查詢出錯");
		map.put("list", new ArrayList<CartItem>());
		return map;
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/insertAdmin")
	public Map<String, Object> adminCartInsert(@RequestBody CartItem cartItem) {
		Map<String, Object> map = cartItemService.insert(cartItem.getP_id(), cartItem.getU_id());
		String msg = (map.get("errorMessage") == null)? "新增成功！" : "新增失敗 :^)";
		map.put("state", msg);
		
		return map;
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/deleteAdmin")
	public Map<String, String> adminCartDelete(@RequestParam Integer[] cart_ids) {
		cartItemService.delete(cart_ids);
		HashMap<String, String> map = new HashMap<>();
		map.put("state", "資料刪除完成。");
		return map;
	}
	
	/***************************************************************************** */
	@GetMapping("/a1")
	public String payViaEcpay() {
		AioCheckOutALL aioObj = new AioCheckOutALL(); 
		aioObj.setMerchantTradeNo("kiryuushiki8931");
		aioObj.setMerchantTradeDate("2017/01/01 08:05:23");
		aioObj.setTotalAmount("50");
		aioObj.setTradeDesc("test Description");
		aioObj.setItemName("TestItem");
		aioObj.setReturnURL("http://localhost:8080/studiehub/");
		aioObj.setNeedExtraPaidInfo("N");
		String form = new AllInOne("").aioCheckOut(aioObj, null);
		return form;
	}
	
	@GetMapping("/a2")
	public AioCheckOutALL genEcpayOrder(@RequestBody List<OrderInfo> orders) {
		// 【產生 MerchantTradeNo String(20)】 = studiehub + date(yyMMdd) + oid七位
		Integer latestOid = orderService.selectLatestOid().getO_id();
		String thisMoment = new SimpleDateFormat("yyMMdd").format(new Date());
		String myMerchantTradeNo = String.format("studiehub%s%07d", thisMoment, latestOid);
		// 【產生 MerchantTradeDate String(20)】
		String myMerchantTradeDate = new SimpleDateFormat("yyMMdd").format(new Date());
		// 【產生 TotalAmount Int】
		String myTotalAmount = String.valueOf(orders.size());	
		// 【產生 TradeDesc String(200)】
		String myTradeDesc = "Thank you for joining StudieHub!"; // ❗有更有意義的內容嗎？
		
		
		
		AioCheckOutALL aioObj = new AioCheckOutALL(); 
		aioObj.setMerchantTradeNo("kiryuushiki8931");
		aioObj.setMerchantTradeDate("2017/01/01 08:05:23");
		aioObj.setTotalAmount("1");
		aioObj.setTradeDesc("test Description");
		aioObj.setItemName("TestItem");
		aioObj.setReturnURL("http://localhost:8080/studiehub/");
		aioObj.setNeedExtraPaidInfo("N");
		return aioObj;
	}
	
	
}