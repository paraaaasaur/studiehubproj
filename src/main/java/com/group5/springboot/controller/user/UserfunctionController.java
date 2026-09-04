package com.group5.springboot.controller.user;

import com.group5.springboot.annotation.auth.RejectsUser;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.user.UserService;
import com.group5.springboot.utils.EmailSenderService;
import com.group5.springboot.utils.GenerateRandomPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserfunctionController {
	final UserService userService;
	final EmailSenderService emailService;


	@Autowired
	public UserfunctionController(UserService userService, EmailSenderService emailService) {
		this.userService = userService;
		this.emailService = emailService;
	}


	@RejectsUser
	@GetMapping(path = "/gotoForgetPassword.controller")
	public String gotoForgetPassword() {
		return "users/forgot-password";
	}

	@RequiresUser
	@GetMapping(path = "/logout.controller", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> logout(
			@SessionAttribute User_Info loginBean,
			HttpSession session
	) {
		Map<String, String> map = new HashMap<>();
		try {
			if(loginBean != null && !(loginBean.getU_id().length() == 0)) {
				session.invalidate();
				map.put("success", "已成功登出!");
			}else {
				map.put("fail", "尚未登入，請先登入後再操作...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("fail", "發生問題，請重新操作...");
		}
		return map;
	}
	
	@RejectsUser
	@PostMapping(path = "/sendRandomPasswordToRegisteredEmail.controller", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> resetPasswordAndSendEmail(@RequestBody Map<String, String> u_emailMap) {
		Map<String, String> maps = new HashMap<>();
		String u_email = u_emailMap.get("u_email");
		User_Info searchResult = userService.getUserInfoForForgetPassword(u_email);
		
		if(searchResult == null) {
			maps.put("fail", "此信箱尚未註冊!");
			return maps;
		}else {
		String rdmPassword = GenerateRandomPassword.generatePasswordProcess();
		userService.setNewPasswordForForgetPsw(u_email, rdmPassword);
		String body = "用戶: " + searchResult.getU_id() + " 您好，新的密碼為:" + rdmPassword + "，請使用這組密碼登入並盡快更改密碼!";
		emailService.sendSimpleEmail(u_email,
									 body,
									 "Studie Hub 忘記密碼通知函");
		maps.put("success", "新密碼信件已寄送至您的信箱，請盡快更新!");
		}
		
		return maps;
	}
}