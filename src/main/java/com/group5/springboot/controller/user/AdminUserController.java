package com.group5.springboot.controller.user;

import java.util.List;

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
import com.group5.springboot.service.user.IUserService;
@Controller
@SessionAttributes(names = {"adminId"})
public class AdminUserController {

	@Autowired
	IUserService iUserService;
	
	
	//去管理員頁面
	@GetMapping(path = "/gotoAdminIndex.controller")
	public String adminIndex(Model model) {
		String returnPage = "";
		boolean loginResult = checkIfAdminLoggedIn(model);
		if(loginResult) {
			returnPage = "adminIndex";
		}else {
			returnPage = "user/adminLogin";
		}
		return returnPage;
	}
	
	//去管理員登入頁面
	@GetMapping(path = "/gotoAdminLogin.controller")
	public String gotoAdminLoginPage() {
		return "user/adminLogin";
	}
	
	//到查看全部會員資料頁面
	@GetMapping(path = "/gotoShowAllUser.controller")
	public String gotoShowAllUser(Model model) {
		String returnPage = "";
		boolean loginResult = checkIfAdminLoggedIn(model);
		if(loginResult) {
			returnPage = "user/showAllUser";
		}else {
			returnPage = "user/adminLogin";
		}
		return returnPage;
	}
	
	
	//管理員登入
	@PostMapping(path = "/AdminLogin.controller")
	public String adminLogin(@RequestParam(name = "id")String id,
			@RequestParam(name = "psw")String psw,
			RedirectAttributes ra,
			Model model) {
		String returnPage = "";
		
		if(id.equals("adming5") && psw.equals("manager")) {
			model.addAttribute("adminId", id);
			System.out.println("session adminId: " + model.getAttribute("adminId"));
			returnPage = "redirect:/gotoAdminIndex.controller";
			ra.addFlashAttribute("success", "管理員登入成功");
//			ra.addFlashAttribute("success", "管理員登入成功, 為您導去管理者頁面...");
		}else {
			returnPage = "redirect:/gotoAdminLogin.controller";
			ra.addFlashAttribute("fail", "帳號或密碼錯誤");
//			ra.addFlashAttribute("fail", "帳號或密碼錯誤, 請再試一次!");
		}
		
		return returnPage;
	}
	
	//管理員登出
	@GetMapping(path = "/adminLogout.controller")
	public String adminLogout(Model model, SessionStatus ss){
		ss.setComplete();
		return "redirect:/";	//登出後回使用者首頁
	}
	
	//查看全部會員資料
	@GetMapping(path = "/showAllUser.controller", produces = {"application/json"})
	@ResponseBody
	public List<User_Info> gotoFindAllUserPage() {
		List<User_Info> users = iUserService.showAllUsers();
		return users;
	}
	
	//檢查管理員是否已登入
	private boolean checkIfAdminLoggedIn(Model model) {
		boolean result = false;
		try {
			String id = (String)model.getAttribute("adminId");
			if(id!=null && id.equals("adming5")) {
				result = true;
			}else {
				result = false;
			}
		} catch (Exception e) {
			result = false;
			System.out.println("有問題，在\'檢查管理者是否登入\'這邊......");
		}
		return result;
	}
}
