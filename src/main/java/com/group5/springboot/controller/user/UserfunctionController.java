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

import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.user.IUserService;

@Controller
@SessionAttributes(names = {"loginBean"})
public class UserfunctionController {
	
	@Autowired
	IUserService iUserService;
	

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
		maps.put("result", userInfo.getU_email());
		System.out.println(userInfo.getU_email());
		System.out.println(maps.get("result"));
		return maps;
	}
	
//	//查看全部會員資料
//	@GetMapping(path = "/showAllUser.controller", produces = {"application/json"})
//	@ResponseBody
//	public List<User_Info> gotoFindAllUserPage() {
//		List<User_Info> users = iUserService.showAllUsers();
//		return users;
//	}
	
}
