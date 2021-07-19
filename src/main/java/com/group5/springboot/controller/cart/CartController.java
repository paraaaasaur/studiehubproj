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
	private CartItemService cartItemService;
	@Autowired // SDI ✔
	private OrderService orderService;
	
	/***************************************************************************** */
	@PostMapping(value="/cart.controller/clientShowCart")
	public List<Map<String, Object>> clientShowCart(@RequestParam String u_id) {
		List<Map<String, Object>> cart = cartItemService.getCart(u_id);
		cart.forEach(System.out::println);
		return cart;
	}
	
	/***************************************************************************** */
	@SuppressWarnings("deprecation")
	@PostMapping(value = "/cart.controller/clientRemoveProductFromCart", produces = "application/json; charset=UTF-8") @Deprecated
	public List<Map<String, Object>> clientRemoveProductFromCart(@RequestParam Integer[] p_ids, @RequestParam String u_id) {
		Arrays.asList(p_ids).forEach(p_id -> cartItemService.deleteASingleProduct(u_id, p_id));
		return cartItemService.getCart(u_id);
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/clientRemoveProductFromCartByCartId", produces = "application/json; charset=UTF-8")
	public List<Map<String, Object>> clientRemoveProductFromCartByCartId(@RequestParam Integer[] cart_ids, @RequestParam String u_id) {
		Arrays.asList(cart_ids).forEach(cartItemService::deleteASingleProduct);
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
	@PostMapping("/cart.controller/checkout")
	public String payViaEcpay(
			@RequestParam("u_id") String u_id,
			@RequestParam("p_ids") Integer[] p_ids
			) {
		List<ProductInfo> cart = new ArrayList<ProductInfo>();
		for(Integer p_id : p_ids) {
			ProductInfo product = productService.findByProductID(p_id);
			cart.add(product);
		}
		User_Info uBean = userService.getSingleUser(u_id);
		
		AioCheckOutALL aioObj = genEcpayOrder(cart, uBean, cart); 
		System.out.println(aioObj);
		
		// 參數 1 = 充滿EcpayOrder參數的aioObj，參數 2 = 是否要發票(invoice)
		String htmlForm = new AllInOne("").aioCheckOut(aioObj, null);
		return htmlForm;
	}

	@PostMapping("/cart.controller/getEcpayResultAttr")
	public Map<String, String> getEcpayResultAttr() {
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) CartViewController.cartInfoMap.get("ecpayResultAttr");
		CartViewController.cartInfoMap.remove("ecpayResultAttr");
		return map;
	}

	
	
	private AioCheckOutALL genEcpayOrder(List<ProductInfo> cart, User_Info uBean, List<ProductInfo> tempCart) {
		// 【產生 MerchantTradeNo String(20)】 = studiehub + date(yyMMdd) + oid五位
		// ❗ 交易失敗的時候這會變得不能用第二次
		Integer latestOid = orderService.getCurrentIdSeed() + 10000 + (int)Math.ceil(Math.random()*60000); 
		String thisMoment = new SimpleDateFormat("yyMMdd").format(new Date());
		String myMerchantTradeNo = String.format("studiehub%s%05d", thisMoment, latestOid);
		// 【產生 MerchantTradeDate String(20)】
		String myMerchantTradeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		// 【產生 TotalAmount Int】
		Integer myTotalAmountInt = 0;
		for(ProductInfo product : cart) {
			myTotalAmountInt += product.getP_Price();
		}
		String myTotalAmount = String.valueOf(myTotalAmountInt);
		// 【產生 TradeDesc String(200)】
		String myTradeDesc = "Thank you for joining StudieHub!"; // ❗有更有意義的內容嗎？
		// 【產生 ItemName String(400)】
		StringBuilder myItemNameBuilder = new StringBuilder("");
		cart.forEach(product -> myItemNameBuilder.append("#").append(product.getP_Name()));
		String myItemName = myItemNameBuilder.replace(0, 1, "").toString();
		// 【產生 ReturnURL String(200)】
//		String ngrokhttps = "";
		String ngrokhttp = "http://0025811d2eed.ngrok.io"; // 演示時需要重開ngrok輸入ngrok http 8080取得

		String myReturnURL = new StringBuilder(ngrokhttp).append("/studiehub").append("/cart.controller/receiveEcpayReturnInfo").toString();
		String myClientBackURL = "http://localhost:8080/studiehub/cart.controller/clientResultPage";
		
		
		AioCheckOutALL aioObj = new AioCheckOutALL(); 
		aioObj.setMerchantTradeNo(myMerchantTradeNo);
		aioObj.setMerchantTradeDate(myMerchantTradeDate);
		aioObj.setTotalAmount(myTotalAmount);
		aioObj.setTradeDesc(myTradeDesc);
		aioObj.setItemName(myItemName);
		aioObj.setReturnURL(myReturnURL);
		aioObj.setNeedExtraPaidInfo("N"); // ❗ 實際上應該要有選擇性
		aioObj.setCustomField1(uBean.getU_id()); // u_id
		aioObj.setCustomField2(uBean.getU_lastname() + uBean.getU_firstname()); // user's full name
		
		
		CartViewController.cartInfoMap.put(uBean.getU_id(), tempCart);
		

//		aioObj.setCustomField3("ガンキマリ");
//		aioObj.setCustomField4("僕を応援しろよ僕を");
		aioObj.setClientBackURL(myClientBackURL);
		return aioObj;
	}
	
	
	
}