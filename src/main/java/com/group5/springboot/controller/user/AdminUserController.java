package com.group5.springboot.controller.user;

import com.group5.springboot.annotation.auth.RejectsAdmin;
import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
@Controller
public class AdminUserController {
	final UserService userService;


	@Autowired
	public AdminUserController(UserService userService) {
		this.userService = userService;
	}


	@RequiresAdmin
	@GetMapping(path = "/gotoAdminIndex.controller")
	public String adminIndex() {
		return "common/admin/dashboard";
	}
	
	@RejectsAdmin
	@GetMapping(path = "/gotoAdminLogin.controller")
	public String gotoAdminLoginPage() {
		return "auth/admin/login";
	}
	
	@RequiresAdmin
	@GetMapping(path = "/gotoShowAllUser.controller")
	public String gotoShowAllUser() {
		return "users/admin/list";
	}

	@RejectsAdmin
	@PostMapping(path = "/AdminLogin.controller")
	public String adminLogin(
			@RequestParam(name = "id")String id,
			@RequestParam(name = "psw")String psw,
			RedirectAttributes ra,
			HttpSession session
	) {
		String returnPage = "";
		
		if(id.equals("adming5") && psw.equals("manager")) {
			session.setAttribute("adminId", id);
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
	public String adminLogout(HttpSession session) {
		session.invalidate();
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