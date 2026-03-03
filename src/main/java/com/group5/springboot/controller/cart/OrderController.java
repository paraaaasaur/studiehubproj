package com.group5.springboot.controller.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.group5.springboot.annotation.auth.RequiresAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group5.springboot.model.cart.OrderInfo;
import com.group5.springboot.service.cart.OrderService;

@RestController
public class OrderController {
	@Autowired private OrderService orderService;


	@RequiresAdmin
	@GetMapping(value = "/order.controller/adminSelectTop100", produces = "application/json; charset=UTF-8")
	public Map<String, Object> adminOrderSelectTop100() {
		return orderService.selectTop100();
	}
	
	@RequiresAdmin
	@PostMapping(value = "/order.controller/adminSearchBar")
	public Map<String, Object> adminOrderSearchBar(
			@RequestParam(name = "searchBy") String condition,
			@RequestParam(name = "searchBar") String value
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
				String regex = ",";
				String[] dates = value.split(regex);
				String startDateString = dates[0].split("T")[0] + " " + dates[0].split("T")[1];
				String endDateString = dates[1].split("T")[0] + " " + dates[1].split("T")[1];

				return orderService.selectWithTimeRange(startDateString, endDateString);
			} else if ("o_id".equals(condition) || "p_id".equals(condition) || "o_amt".equals(condition) || "p_price".equals(condition)) {
				// (4) 數值範圍查詢
				String regex = ",";
				String[] numberStrings = value.split(regex);
				Integer minValue = 0;
				Integer maxValue = 0;
				minValue = Integer.parseInt(numberStrings[0]);
				maxValue = Integer.parseInt(numberStrings[1]);

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
}