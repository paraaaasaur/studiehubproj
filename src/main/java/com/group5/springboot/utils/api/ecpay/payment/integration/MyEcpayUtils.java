package com.group5.springboot.utils.api.ecpay.payment.integration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group5.springboot.model.product.ProductInfo;
import com.group5.springboot.service.cart.OrderService;
import com.group5.springboot.utils.api.ecpay.payment.integration.domain.AioCheckOutALL;
@Component
public class MyEcpayUtils {
	@Autowired
	private static OrderService orderService;
	
	public static AioCheckOutALL genEcpayOrder(List<ProductInfo> cart) {
		// 【產生 MerchantTradeNo String(20)】 = studiehub + date(yyMMdd) + oid七位
		Integer latestOid = orderService.selectLatestOid().getO_id() - 20;
		String thisMoment = new SimpleDateFormat("yyMMdd").format(new Date());
		String myMerchantTradeNo = String.format("studiehub%s%07d", thisMoment, latestOid);
		// 【產生 MerchantTradeDate String(20)】
		String myMerchantTradeDate = new SimpleDateFormat("yyMMdd").format(new Date());
		// 【產生 TotalAmount Int】
		String myTotalAmount = String.valueOf(cart.size());	
		// 【產生 TradeDesc String(200)】
		String myTradeDesc = "Thank you for joining StudieHub!"; // ❗有更有意義的內容嗎？
		// 【產生 ItemName String(400)】
		StringBuilder myItemNameBuilder = new StringBuilder("");
		cart.forEach(product -> myItemNameBuilder.append("#").append(product.getP_Name()));
		String myItemName = myItemNameBuilder.replace(0, 1, "").toString();
		// 【產生 ReturnURL String(200)】
		String myReturnURL = "http://localhost:8080/studiehub/cart.controller/receiveEcpayReturnInfo";
		String myClientBackURL = "http://localhost:8080/studiehub/cart.controller/clientResultPage";
		
		
		
		AioCheckOutALL aioObj = new AioCheckOutALL(); 
		aioObj.setMerchantTradeNo(myMerchantTradeNo);
		aioObj.setMerchantTradeDate(myMerchantTradeDate);
		aioObj.setTotalAmount(myTotalAmount);
		aioObj.setTradeDesc(myTradeDesc);
		aioObj.setItemName(myItemName);
		aioObj.setReturnURL(myReturnURL);
		aioObj.setNeedExtraPaidInfo("N"); // ❗ 實際上應該要有選擇性
		aioObj.setClientBackURL(myClientBackURL);
		return aioObj;
	}
}
