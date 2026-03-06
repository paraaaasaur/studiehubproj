package com.group5.springboot.controller.user;

import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.group5.springboot.annotation.auth.RejectsUser;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.config.StorageConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.user.UserService;
import com.group5.springboot.utils.EmailSenderService;
import com.group5.springboot.utils.SystemUtils;
import com.group5.springboot.validate.UserValidator;

@Controller
@SessionAttributes(names = "loginBean")
public class UserController {
	final UserService userService;
	User_Info user_info;
	final UserValidator userValidator;
	final ServletContext context;
	final EmailSenderService emailService;

	private final String AVATAR_STORAGE_DIR;


	@Autowired
	public UserController(UserService userService, User_Info userInfo, UserValidator userValidator, ServletContext context, EmailSenderService emailService, StorageConfigProperties props) {
		this.userService = userService;
		user_info = userInfo;
		this.userValidator = userValidator;
		this.context = context;
		this.emailService = emailService;
		this.AVATAR_STORAGE_DIR = props.getUserAvatarUploadStorageDir();
	}


	@RejectsUser
	@GetMapping(path = "/gotologin.controller")
	public String gotoLoginPage() {
		return "user/login";
	}
	
	@RejectsUser
	@GetMapping(path = "/gotosignup.controller")
	public String gotoSignupPage() {
		return "user/signup";
	}
	
	@RequiresUser
	@GetMapping(path = "/gotoUpdateUserinfo.controller")
	public String gotoUpdateUserinfo() {
		return "user/updateUser";
	}
	
	@RequiresUser
	@GetMapping(path = "/gotoChangePassword.controller")
	public String gotoChangePassword() {
		return "user/changePassword";
	}

	@RejectsUser
	@PostMapping(path = "/login.controller", produces = {"application/json"})
	@ResponseBody
	public Map<String, Object> login(@RequestBody User_Info user_Info, Model model){
		Map<String, Object> map = new HashMap<>();
		user_info = null;
		try {
			user_info = userService.login(user_Info);
			if(user_info != null && user_info.getU_id().length()>0) {
				map.put("success", "登入成功");
				map.put("loginBean", user_info);
				
				model.addAttribute("loginBean", user_info);
			} else if(user_info == null) {
				map.put("fail", "帳號或密碼錯誤，請再試一次...");
			}
		} catch (Exception e) {
			map.put("fail", e.getMessage());
		}
		return map;
	}
	
	@PostMapping(path = "/checkUserId", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> checkUserId(@RequestParam String u_id){
		Map<String, String> map = new HashMap<>();
		String user_id = userService.checkUserId(u_id);
		map.put("u_id", user_id);
		return map;
	}
	
	@RejectsUser
	@PostMapping(path = "/userSignup", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> signup(@RequestBody User_Info user_Info){
		Map<String, String> map = new HashMap<>();
		try {
			if(!(user_Info.getU_email().trim().contains("@"))) {
				map.put("formatError", "信箱格式錯誤!");
				return map;
			}
		} catch (Exception e) {
			map.put("fail", e.getMessage());
		}
		
		
		int n = 0;
		try {
			n = userService.saveUser(user_Info);
			if(n == 1) {
				map.put("success", "註冊成功");
				//寄成功註冊的信件
				String body = "用戶: " + user_Info.getU_id() + " 您好，歡迎註冊成為Studie Hub的會員，祝您使用愉快!";
				emailService.sendSimpleEmail(user_Info.getU_email(),
											 body,
											 "Studie Hub 會員註冊成功通知");
			}else if(n == -1) {
				map.put("fail", "帳號重複");
			}
		} catch (Exception e) {
			map.put("fail", e.getMessage());
		}
		return map;
	}

	@RequiresUser
	@PostMapping("/changePassword.controller")
	public String changePassword(
			@ModelAttribute("userBean") User_Info user_Info,
			RedirectAttributes ra,
			@RequestParam String u_psw, @RequestParam String cfm_psw,
			Model model, SessionStatus status
	) {
		if(!(u_psw.equals(cfm_psw))) {
			ra.addFlashAttribute("errorMessageOfChangingPassword", "兩次密碼不同");
			return "redirect:/gotoChangePassword.controller";
		}
		
		userService.updateUser(user_Info);
		updateLoginBean(model, status);
		ra.addFlashAttribute("successMessageOfChangingPassword", "修改成功");
		return "redirect:/";
	}
	
	@RequiresUser
	@PostMapping("/updateUserinfo.controller")
	public String updateUser(
			@ModelAttribute("userBean") User_Info user_Info,
			BindingResult bindingResult,
			RedirectAttributes ra,
			Model model, SessionStatus status
	) {
		userValidator.validate(user_Info, bindingResult);
		if(bindingResult.hasErrors()) {
			return "user/updateUser";
		}
		
		Blob blob = null;
		String mimeType = "";
		String ogfName = "";
		MultipartFile uploadImage = user_Info.getUploadImage();
		if(uploadImage != null && uploadImage.getSize() > 0) {
			try {
				InputStream is = uploadImage.getInputStream();
				ogfName = uploadImage.getOriginalFilename();
				blob = SystemUtils.inputStreamToBlob(is);
				mimeType = context.getMimeType(ogfName);
				user_Info.setU_img(blob);
				user_Info.setMimeType(mimeType);
				String ext = StringUtils.getFilenameExtension(ogfName);
				try {
					File imageFolder = new File(AVATAR_STORAGE_DIR);
					if (!imageFolder.exists())
						imageFolder.mkdirs();
					String imageFilename = "MemberImage_" + user_Info.getU_id() + "." + ext;
					File file = new File(imageFolder, imageFilename);
					uploadImage.transferTo(file);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		userService.updateUser(user_Info);
		updateLoginBean(model, status);
		ra.addFlashAttribute("successMessage", "修改成功");
		return "redirect:/gotoUpdateUserinfo.controller";
	}


	// ==================== helpers ====================
	public void updateLoginBean(Model model, SessionStatus status) {
		User_Info loginBean = (User_Info)model.getAttribute("loginBean");
		User_Info updateBean = userService.getSingleUser(loginBean.getU_id());
		model.addAttribute("loginBean", updateBean);
	}


	// ==================== @ModelAttributes ====================
	@ModelAttribute("userBean")
	public User_Info getLoginUserInfos(Model model) {
		User_Info loginBean = (User_Info)model.getAttribute("loginBean");
		User_Info userInfo = null;
		try {
			userInfo = userService.getSingleUser(loginBean.getU_id());
		} catch (Exception e) {
			userInfo = new User_Info();
		}
		return userInfo;
	}
	
	@ModelAttribute("genderList")
    public Map<String, String>  getGenderList(){
		Map<String, String> map = new LinkedHashMap<>();
		map.put("男", "男");
		map.put("女", "女");
		return map;
    }
}