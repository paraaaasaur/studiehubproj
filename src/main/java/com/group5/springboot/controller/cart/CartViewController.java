package com.group5.springboot.controller.cart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.cart.OrderService;
import com.group5.springboot.utils.SystemUtils;

@SessionAttributes(names = "cart")
@Controller
@RequestMapping(path = "")
public class CartViewController {
	@Autowired // SDI ✔
	private OrderService orderService;
//	@Autowired // SDI ✔
	public List<ProductInfo> cart = new ArrayList<ProductInfo>();

	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = {"/cart.controller/cartAdminInsert"})
	public String toCartAdminInsert(Model model) {
		model.addAttribute("emptyOrderInfo", new OrderInfo());
		return "/cart/cartAdminInsert";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = {"/cart.controller/cartAdminUpdate/{oid}"})
	public String toCartAdminUpdate(@PathVariable("oid") Integer oid, Model model) {
		model.addAttribute("orderInfo", orderService.select(new OrderInfo(oid)));
		return "/cart/cartAdminUpdate";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@PostMapping(value = {"/cart.controller/cartAdminUpdate/{oid}"})
	public String cartAdminUpdate(@ModelAttribute(name = "orderInfo") OrderInfo orderInfo,
			BindingResult result, 
			RedirectAttributes ra) {
		
//		placeValidator.validate(place, result);
//		if (result.hasErrors()) {
//          下列敘述可以理解Spring MVC如何處理錯誤			
//			List<ObjectError> list = result.getAllErrors();
//			for (ObjectError error : list) {
//				System.out.println("有錯誤：" + error);
//			}
//		ra.addFlashAttribute("successMessage", place.getName() + "修改成功");
		
		// 新增或修改成功，要用response.sendRedirect(newURL) 通知瀏覽器對newURL發出請求
		orderService.update(orderInfo);
		return "/cart/cartAdminSelect";
		
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = {"/cart.controller/cartIndex"})
	public String toCartIndex() {
		refill();
		System.out.print("現在你的購物車 = ");
		cart.forEach(System.out::print);
		return "cart/cartIndex";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@PostMapping(value = {"/cart.controller/cartCheckout"})
	public String toCartCheckout() {
		refill();
		return "cart/cartCheckout";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = {"/cart.controller/cartAdminSelect"})
	public String toCartAdminSelect() {
		return "cart/cartAdminSelect";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@GetMapping(value = "/cart.controller/index")
	public String backToMainPage() {
		return "/index";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	@PostMapping("/cart.controller/pay")
	public String pay() {

		User_Info fakeUserInfo00 = new User_Info();
		fakeUserInfo00.setU_id("fbk001");
		
		// 取得O_Amt
		 Integer O_Amt = 0;
		 for (int i = 0; i < this.cart.size(); i++) {
			O_Amt += this.cart.get(i).getP_Price();
		}
		 
		// 把OrderBean的資料寫進去Database
		for(int i = 0; i < cart.size(); i++) {
			OrderInfo orderBean = new OrderInfo();
			orderBean.setO_id(1000); // PK // 會受IDENTITY(1, 1)的影響被複寫掉，值是多少都無所謂
			orderBean.setP_id(cart.get(i).getP_ID()); // FK
//			orderBean.setP_name(cart.get(i).getP_Name()); 
//			orderBean.setP_price(cart.get(i).getP_Price()); 
			orderBean.setU_id(fakeUserInfo00.getU_id()); // FK // fake
//			orderBean.setU_firstname(fakeUserInfos.get(0).getU_firstname()); 
//			orderBean.setU_lastname(fakeUserInfos.get(0).getU_lastname()); 
//			orderBean.setU_email(fakeUserInfos.get(0).getU_email());
//			orderBean.setO_status("DONE"); // non-insertable 
//			orderBean.setO_date(now); // non-insertable
			orderBean.setO_amt(O_Amt);
			orderService.insert(orderBean);
		}

		this.cart = new ArrayList<ProductInfo>();
		System.out.println("購買成功，感謝您！");
		return "/cart/cartThanks";
	}
	
	/**OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */
	/**
	 * 純粹測試用；最後用不到
	 * 每次經過這個Controller都會跑這個block
	 * 測試用。cart如果是空的，會自動補3件下列商品作為測試
	 **/
	private void refill() {
		System.out.println("正在檢查你的cart是不是空的...");
		
		if(cart.size() == 0 || cart == null) {
//			byte[] aa = {1, 2};
			ProductInfo fakeProductBean1 = new ProductInfo();
			fakeProductBean1.setP_ID(1);
			fakeProductBean1.setP_Name("EN_Speaking");
			fakeProductBean1.setP_Class("EN");
			fakeProductBean1.setP_Price(500);
			fakeProductBean1.setP_DESC(SystemUtils.stringToClob("Cool & Fun"));
			fakeProductBean1.setU_ID("fbk001");
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1999-11-22");
				fakeProductBean1.setP_createDate(date);
//				fakeProductBean1.setP_createDate(java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("1999-11-22").getTime())); // in case of java.sql.Date
			} catch (ParseException e) {
				e.printStackTrace();
			}
//			fakeProductBean1.setP_Img(aa);
//			fakeProductBean1.setP_Video(aa);
			
			ProductInfo fakeProductBean2 = new ProductInfo();
			fakeProductBean2.setP_ID(2);
			fakeProductBean2.setP_Name("RU_Reading");
			fakeProductBean2.setP_Class("RU");
			fakeProductBean2.setP_Price(750);
			fakeProductBean2.setP_DESC(SystemUtils.stringToClob("хороший"));
			fakeProductBean2.setU_ID("Stalin");
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1878-12-06");
				fakeProductBean2.setP_createDate(date);
//				fakeProductBean2.setP_createDate(java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("1999-11-22").getTime())); // in case of java.sql.Date
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(cart == null) {
				cart = new ArrayList<ProductInfo>();
			}
			cart.add(fakeProductBean1);
			cart.add(fakeProductBean2);
		}
	}
	
}