package com.group5.springboot.controller.user;

import java.util.List;

import com.group5.springboot.annotation.auth.RejectsAdmin;
import com.group5.springboot.annotation.auth.RequiresAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.user.UserService;
@Controller
@SessionAttributes(names = {"adminId"})
public class AdminUserController {
	final UserService userService;


	@Autowired
	public AdminUserController(UserService userService) {
		this.userService = userService;
	}


	@RequiresAdmin
	@GetMapping(path = "/gotoAdminIndex.controller")
	public String adminIndex() {
		return "adminIndex";
	}
	
	@RejectsAdmin
	@GetMapping(path = "/gotoAdminLogin.controller")
	public String gotoAdminLoginPage() {
		return "user/adminLogin";
	}
	
	@RequiresAdmin
	@GetMapping(path = "/gotoShowAllUser.controller")
	public String gotoShowAllUser() {
		return "user/showAllUser";
	}

	@RejectsAdmin
	@PostMapping(path = "/AdminLogin.controller")
	public String adminLogin(
			@RequestParam(name = "id")String id,
			@RequestParam(name = "psw")String psw,
			RedirectAttributes ra,
			Model model
	) {
		String returnPage = "";
		
		if(id.equals("adming5") && psw.equals("manager")) {
			model.addAttribute("adminId", id);
			returnPage = "redirect:/gotoAdminIndex.controller";
			ra.addFlashAttribute("success", "管理員登入成功");
		}else {
			returnPage = "redirect:/gotoAdminLogin.controller";
			ra.addFlashAttribute("fail", "帳號或密碼錯誤");
		}
		
		return returnPage;
	}
	
	@RequiresAdmin
	@GetMapping(path = "/adminLogout.controller")
	public String adminLogout(SessionStatus ss){
		ss.setComplete();
		return "redirect:/";
	}
	
	@RequiresAdmin
	@GetMapping(path = "/showAllUser.controller", produces = {"application/json"})
	@ResponseBody
	public List<User_Info> gotoFindAllUserPage() {
		List<User_Info> users = userService.showAllUsers();
		return users;
	}
}