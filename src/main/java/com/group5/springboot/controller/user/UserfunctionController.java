package com.group5.springboot.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.group5.springboot.dao.user.UserDao;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.user.IUserService;
import com.group5.springboot.utils.EmailSenderService;
import com.group5.springboot.utils.GenerateRandomPassword;

@Controller
@SessionAttributes(names = {"loginBean"})
public class UserfunctionController {
	
	@Autowired
	IUserService iUserService;
	@Autowired
	EmailSenderService emailService;
	
//	@Autowired
//	GenerateRandomPassword generateRandomPassword;
	

	//到忘記密碼頁面
	@GetMapping(path = "/gotoForgetPassword.controller")
	public String gotoForgetPassword() {
		return "user/forgetPassword";
	}

	//登出
	@GetMapping(path = "/logout.controller", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> logout(Model model, SessionStatus ss){
		Map<String, String> map = new HashMap<>();
		try {
			User_Info bean = (User_Info)model.getAttribute("loginBean");
			if(bean != null && !(bean.getU_id().length() == 0)) {
				ss.setComplete();
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
	
	//送隨機密碼至信箱(忘記密碼)
	@PostMapping(path = "/sendRandomPasswordToRegisteredEmail.controller", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> resetPasswordAndSendEmail(@RequestBody User_Info userInfo) {
		Map<String, String> maps = new HashMap<>();
		String u_email = userInfo.getU_email();
		//maps.put("result", u_email);	//測試
		User_Info searchResult = iUserService.getUserInfoForForgetPassword(u_email);
		
		if(searchResult == null) {
			maps.put("fail", "此信箱尚未註冊!");
			return maps;
		}else {
		String rdmPassword = GenerateRandomPassword.generatePasswordProcess();	//產生密碼
		iUserService.setNewPasswordForForgetPsw(u_email, rdmPassword);	//更新結果
		//System.out.println(updateResult);
		//寄成功註冊的信件
		String body = "用戶: " + searchResult.getU_id() + " 您好，新的密碼為:" + rdmPassword + "，請使用這組密碼登入並盡快更改密碼!";
		emailService.sendSimpleEmail(u_email,
									 body,
									 "Studie Hub 忘記密碼通知函");
		maps.put("success", "新密碼信件已寄送至您的信箱，請盡快更新!");
		}
		
		return maps;
	}
	
	
}
