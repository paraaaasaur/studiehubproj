package com.group5.springboot.controller.cart;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tttt {

	public static void main(String[] args) {
		// StringBuilder的replace()方法測試
		StringBuilder myItemNameBuilder = new StringBuilder("");
		ArrayList<String> list = new ArrayList<String>();
		list.add("you");
		list.add("are");
		list.add("amazing");
		list.forEach(s -> myItemNameBuilder.append("#").append(s));
		String myItemName = myItemNameBuilder.replace(0, 1, "").toString();
		System.out.println(myItemName);
		
		// String format測試
//		Integer latestOid = 15;
//		String thisMoment = new SimpleDateFormat("yyMMdd").format(new Date());
//		String s = String.format("studiehub%s%07d", thisMoment, latestOid);
//		System.out.println(s);
		
		// 路徑測試
//		String confPath = "/studiehubproj/src/main/java/com/group5/springboot/utils/api/ecpay/payment/integration/config/EcpayPayment.xml";
//		String paymentConfPath = "./src/main/resources/static/api/ecpay/payment_conf.xml";
//		URL fileURL = new Tttt().getClass().getResource("/com/group5/springboot/utils/api/ecpay/payment/integration/config/EcpayPayment.xml");
//		System.out.println(fileURL);
//		System.out.println(new Tttt().getClass().getResource("./"));

	}

}
