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
	@SuppressWarnings("unchecked")
	@GetMapping(value="/test00")
	public List<ProductInfo> test00() {
		return (List<ProductInfo>) orderService.test().get("list");
	}
	
	/***************************************************************************** */
	@PostMapping(value="/cart.controller/clientShowCart")
	public List<Map<String, Object>> clientShowCart(@RequestParam String u_id) {
		List<Map<String, Object>> cart = cartItemService.getCart(u_id);
		cart.forEach(System.out::println);
		return cart;
	}
	

	
	/***************************************************************************** */
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = "/cart.controller/remove", produces = "application/json; charset=UTF-8")
//	public List<ProductInfo> removeProductFromCart(@RequestParam String[] ckboxValues, HttpSession session) { // ❗
//		cart = (ArrayList<ProductInfo>) session.getAttribute("cart");
//		System.out.println(ckboxValues);
//		for (int i = 0; i < ckboxValues.length; i++) {
//			int ckIndex = Integer.parseInt(ckboxValues[i]);
//			cart.remove(ckIndex - i);
//			System.out.println(ckboxValues[i]);
//		}
//		session.setAttribute("cart", cart);
//		return cart;
//	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/clientRemoveProductFromCart", produces = "application/json; charset=UTF-8")
	public List<Map<String, Object>> clientRemoveProductFromCart(@RequestParam Integer[] p_ids, @RequestParam String u_id) {
		Arrays.asList(p_ids).forEach(p_id -> cartItemService.deleteASingleProduct(u_id, p_id));
		return cartItemService.getCart(u_id);
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
	@PostMapping(value = "/cart.controller/adminSelectProduct")
	public ProductInfo adminSelectProduct(@RequestParam("p_id") String p_id) {
		return productService.findByProductID(Integer.parseInt(p_id));
	}
	
	/***************************************************************************** */
	@PostMapping(value = "/cart.controller/adminSelectUser")
	public User_Info adminSelectUser(@RequestParam("u_id") String u_id) {
		return userService.getSingleUser(u_id);
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
/**
 * @沒用了
	@PostMapping(value = "/cart.controller/updateAdmin")
	public Map<String, Object> adminUpdate(@RequestBody OrderInfo order) {
		// ❗ 之後加上一圈先檢查 FK 的機制
		HashMap<String, Object> map = new HashMap<>();
		boolean updateStatus = (orderService.update(order))? true : false;
		String msg = "oid = " + order.getO_id() + ((updateStatus)?  "：修改成功✔" : "：修改失敗❌");
		map.put("state", msg);
		
		return map;
	}
*/
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

	
	/***************************************************************************** */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/***************************************************************************** */
	/***************************************************************************** */
	/**
	 * 純粹測試用；最後用不到
	 * 每次經過這個Controller都會跑這個block
	 * 測試用。cart如果是空的，會自動補3件下列商品作為測試
	 **/
	
//	private void refill() {
//		System.out.println("正在檢查你的cart是不是空的...");
//		
//		if(cart.size() == 0 || cart == null) {
////			byte[] aa = {1, 2};
//			ProductInfo fakeProductBean1 = new ProductInfo();
//			fakeProductBean1.setP_ID(1);
//			fakeProductBean1.setP_Name("EN_Speaking");
//			fakeProductBean1.setP_Class("EN");
//			fakeProductBean1.setP_Price(500);
//			fakeProductBean1.setP_DESC(SystemUtils.stringToClob("Cool & Fun"));
//			fakeProductBean1.setU_ID("fbk001");
//			try {
//				Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1999-11-22");
//				fakeProductBean1.setP_createDate(date);
////				fakeProductBean1.setP_createDate(java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("1999-11-22").getTime())); // in case of java.sql.Date
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
////			fakeProductBean1.setP_Img(aa);
////			fakeProductBean1.setP_Video(aa);
//			
//			ProductInfo fakeProductBean2 = new ProductInfo();
//			fakeProductBean2.setP_ID(2);
//			fakeProductBean2.setP_Name("RU_Reading");
//			fakeProductBean2.setP_Class("RU");
//			fakeProductBean2.setP_Price(750);
//			fakeProductBean2.setP_DESC(SystemUtils.stringToClob("хороший"));
//			fakeProductBean2.setU_ID("Stalin");
//			try {
//				Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1878-12-06");
//				fakeProductBean2.setP_createDate(date);
////				fakeProductBean2.setP_createDate(java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("1999-11-22").getTime())); // in case of java.sql.Date
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			
//			ProductInfo fakeProductBean3 = new ProductInfo();
//			fakeProductBean3.setP_ID(2);
//			fakeProductBean3.setP_Name("AR_Reading");
//			fakeProductBean3.setP_Class("AR");
//			fakeProductBean3.setP_Price(345);
//			fakeProductBean3.setP_DESC(SystemUtils.stringToClob("Excellent"));
//			fakeProductBean3.setU_ID("Oil");
//			try {
//				Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1878-12-06");
//				fakeProductBean3.setP_createDate(date);
////				fakeProductBean3.setP_createDate(java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("1999-11-22").getTime())); // in case of java.sql.Date
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			
//			if(cart == null) {
//				cart = new ArrayList<ProductInfo>();
//			}
//			cart.add(fakeProductBean1);
//			cart.add(fakeProductBean2);
//			cart.add(fakeProductBean3);
//		}
//	}
	
}