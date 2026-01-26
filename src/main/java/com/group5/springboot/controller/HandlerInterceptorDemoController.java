package com.group5.springboot.controller;

import com.group5.springboot.annotation.auth.RejectsAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HandlerInterceptorDemoController {
	@RejectsAdmin
	@GetMapping("/chikuwa")
	public String chikuwa() {
		System.err.println("chikuwa");
		return "<h1 style='color:cyan'>chikuwa</h1>";
	}
}