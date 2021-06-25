package com.group5.springboot.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.user.IUserService;

@Controller
@SessionAttributes(names = {"loginBean"})
public class UserController {
	@Autowired
	IUserService iUserService;
	@Autowired
	User_Info user_info;

	// 到會員的index
	@GetMapping(path = "/gotoUserIndex.controller")
	public String gotoUserIndex() {
		return "user/userIndex";
	}
	
	//到登入頁面
	@GetMapping(path = "/gotologin.controller")
	public String gotoLoginPage(Model model) {
		String returnPage = "";
		boolean loginResult = checkIfLogin(model);
		if (loginResult) {
//			returnPage = "user/userIndex";
			returnPage = "index";
		}else {
			returnPage = "user/login";
		}
		return returnPage;
	}
	
	//到註冊頁面
	@GetMapping(path = "/gotosignup.controller")
	public String gotoSignupPage() {
		return "user/signup";
	}
	
	//到查看全部會員資料頁面
	@GetMapping(path = "/gotoShowAllUser.controller")
	public String gotoShowAllUser() {
		return "user/showAllUser";
	}
	
	//到刪除會員的頁面
	@GetMapping(path = "/gotoDeleteUser.controller/{u_id}")
	public String gotoDeleteUser(@PathVariable String u_id, Model model) {
		model.addAttribute("u_id", u_id);
		return "user/deleteUser";
	}
	
	//到修改會員資料頁面
	@GetMapping(path = "/gotoUpdateUserinfo.controller")
	public String gotoUpdateUserinfo(Model model) {
		String returnPage = "";
		boolean loginResult = checkIfLogin(model);
		if (loginResult) {
			returnPage = "user/updateUser";
		}else {
			returnPage = "user/login";
		}
		return returnPage;
	}
	
//	//登出
//	@GetMapping(path = "/logout.controller", produces = {"application/json"})
//	@ResponseBody
//	public Map<String, String> logout(Model model, SessionStatus ss){
//		Map<String, String> map = new HashMap<>();
//		try {
//			User_Info bean = (User_Info)model.getAttribute("loginBean");
//			if(bean != null && !(bean.getU_id().length() == 0)) {
//				ss.setComplete();
//				map.put("success", "已成功登出!");
//			}else {
//				map.put("fail", "尚未登入，請先登入後再操作...");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			map.put("fail", "發生問題，請重新操作...");
//		}
//		return map;
//	}
	
	
	

	
	//讀取單筆會員資料(全部會員資料到刪除單筆資料)
	@GetMapping("/showSingleUser.controller/{u_id}")
	public @ResponseBody User_Info showSingleUser(@PathVariable String u_id) {
		User_Info user = iUserService.getSingleUser(u_id);
		return user;
	}
	
	
	
	//登入
	@PostMapping(path = "/login.controller", produces = {"application/json"})
	@ResponseBody
	public Map<String, Object> login(@RequestBody User_Info user_Info, Model model){
		Map<String, Object> map = new HashMap<>();
		user_info = null;
		try {
			user_info = iUserService.login(user_Info);
			if(user_info != null && user_info.getU_id().length()>0) {
				map.put("success", "登入成功");
//				map.put("u_id", user_info.getU_id());
				map.put("loginBean", user_info);
				
				//登入成功:把使用者資訊加到sessionScope上
				model.addAttribute("loginBean", user_info);
			} else if(user_info == null) {
				map.put("fail", "帳號或密碼錯誤，請再試一次...");
			}
		} catch (Exception e) {
			System.out.println("********************************");
			System.out.println("有exception，在\'登入controller\'");
			System.out.println("********************************");
			map.put("fail", e.getMessage());
		}
		return map;
	}
	
	//檢查帳號是否可用
	@PostMapping(path = "/checkUserId", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> checkUserId(@RequestParam String u_id){
		Map<String, String> map = new HashMap<>();
		String user_id = iUserService.checkUserId(u_id);
		map.put("u_id", user_id);
		return map;
	}
	
	//會員註冊
	@PostMapping(path = "/userSignup", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> signup(@RequestBody User_Info user_Info){
		Map<String, String> map = new HashMap<>();
		int n = 0;
		try {
			n = iUserService.saveUser(user_Info);
			if(n == 1) {
				map.put("success", "註冊成功");
			}else if(n == -1) {
				map.put("fail", "帳號重複");
			}
		} catch (Exception e) {
			System.out.println("********************************");
			System.out.println("有exception，在\'會員註冊controller\'");
			System.out.println("********************************");
			map.put("fail", e.getMessage());
		}
		return map;
	}
	
	//查看全部會員資料
	@GetMapping(path = "/showAllUser.controller", produces = {"application/json"})
	@ResponseBody
	public List<User_Info> gotoFindAllUserPage() {
		List<User_Info> users = iUserService.showAllUsers();
		return users;
	}
	
	//刪除會員資料
	@DeleteMapping("/user.controller/{u_id}")
	@ResponseBody
	public Map<String, String> deleteUser(@PathVariable(required = true) String u_id){
		Map<String, String> map = new HashMap<>();
		try {
			iUserService.deleteUserById(u_id);
			map.put("success", "刪除成功");
		} catch (Exception e) {
			map.put("fail", "刪除失敗，請再試一次...");
			e.printStackTrace();
		}
		return map;
	}
	
	
	// 修改會員資料
	@PutMapping(path = "/updateUserinfo.controller/{id}", consumes = { "application/json" }, produces = {"application/json" })
	@ResponseBody
	public Map<String, String> updateUser(@RequestBody User_Info user_Info, @PathVariable String id) {
		System.out.println("前端傳進來的值:");
		System.out.println("id:"+user_Info.getU_id());
		System.out.println("psw:"+user_Info.getU_psw());
		System.out.println("lastname:"+user_Info.getU_lastname());
		System.out.println("firstname:"+user_Info.getU_firstname());
		System.out.println("bday:"+user_Info.getU_birthday());
		System.out.println("email:"+user_Info.getU_email());
		System.out.println("tel:"+user_Info.getU_tel());
		System.out.println("address:"+user_Info.getU_address());
		System.out.println("img:"+user_Info.getU_img());
		System.out.println("********************************");
		Map<String, String> map = new HashMap<>();
		User_Info usif = null;
		if (id != null) {
			usif = iUserService.getSingleUser(id);
			if (usif == null || usif.getU_id().length() == 0) {
				map.put("fail", "帳號: " + id + " 不存在!");
			} else {
				System.out.println("********************************");
				System.out.println("\'id!=null\'， 要修改的會員帳號為: " + id);
				System.out.println("********************************");
				try {
					iUserService.updateUser(user_Info);
					map.put("success", "資料修改成功!");
				} catch (Exception e) {
					map.put("fail", "修改失敗!");
				}
			}
		} else {
			System.out.println("id: " + id + "沒傳進來啊...厚唷");
		}
		return map;

	}
	
	
	
	public boolean checkIfLogin(Model model) {
		boolean result = false;
		try {
			User_Info userBean = (User_Info)model.getAttribute("loginBean");
			if(userBean!=null && userBean.getU_id()!=null && userBean.getU_id().length()>0) {
				System.out.println("在\'確認是否登入\'這邊的loginBean name: " + userBean.getU_firstname() + userBean.getU_lastname());
				result = true;
			}else {
				result = false;
			}
		} catch (Exception e) {
			result = false;
			System.out.println("有問題喔，在\'確認是否登入\'這邊......");
		}
		return result;
	}
	
	
	//0624新增ModelAttribute
	@ModelAttribute("userBean")
	public User_Info getLoginUserInfos(User_Info userBean, Model model){
		User_Info loginBean = (User_Info)model.getAttribute("loginBean");
		System.out.println("******************************************");
		User_Info userInfo = null;
		try {
			userInfo = iUserService.getSingleUser(loginBean.getU_id());
			System.out.println("******************************************");
			System.out.println("in getLoginUserIndos, id= " + userInfo.getU_id());
			System.out.println("******************************************");
		} catch (Exception e) {
			userInfo = new User_Info();
			System.out.println("no login Bean in getLoginUserInfos().......");
		}
		return userInfo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
