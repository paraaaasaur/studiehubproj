package com.group5.springboot.controller.cart;

import java.net.URL;

public class Tttt {

	public static void main(String[] args) {
		String confPath = "/studiehubproj/src/main/java/com/group5/springboot/utils/api/ecpay/payment/integration/config/EcpayPayment.xml";
		String paymentConfPath = "./src/main/resources/static/api/ecpay/payment_conf.xml";
		URL fileURL = new Tttt().getClass().getResource("/com/group5/springboot/utils/api/ecpay/payment/integration/config/EcpayPayment.xml");
		System.out.println(fileURL);
		System.out.println(new Tttt().getClass().getResource("./"));

	}

}
