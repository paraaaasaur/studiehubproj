package com.group5.springboot.controller.user;

import com.group5.springboot.annotation.auth.RejectsUser;
import com.group5.springboot.annotation.auth.RequiresUser;
import com.group5.springboot.config.StorageConfigProperties;
import com.group5.springboot.dto.user.ProfileForm;
import com.group5.springboot.dto.user.LoginRequest;
import com.group5.springboot.dto.user.SignupRequest;
import com.group5.springboot.model.user.User_Info;
import com.group5.springboot.service.user.UserService;
import com.group5.springboot.utils.EmailSenderService;
import com.group5.springboot.utils.SystemUtils;
import com.group5.springboot.validate.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@Controller
public class UserController {
	final UserService userService;
	final UserValidator userValidator;
	final ServletContext context;
	final EmailSenderService emailService;

	private final String AVATAR_STORAGE_DIR;


	@Autowired
	public UserController(UserService userService, UserValidator userValidator, ServletContext context, EmailSenderService emailService, StorageConfigProperties props) {
		this.userService = userService;
		this.userValidator = userValidator;
		this.context = context;
		this.emailService = emailService;
		this.AVATAR_STORAGE_DIR = props.getUserAvatarUploadStorageDir();
	}


	@RejectsUser
	@GetMapping(path = "/gotologin.controller")
	public String gotoLoginPage() {
		return "auth/login";
	}

	@RejectsUser
	@GetMapping(path = "/gotosignup.controller")
	public String gotoSignupPage() {
		return "users/signup";
	}

	@RequiresUser
	@GetMapping(path = "/gotoUpdateUserinfo.controller")
	public String gotoUpdateUserinfo(Model model, @SessionAttribute User_Info loginBean) {
		var dbUser = userService.getSingleUser(loginBean.getU_id());
		var view = userService.mapToProfileView(dbUser);
		model.addAttribute("view", view);
		model.addAttribute("genderList", getGenderList());
		return "users/edit-profile";
	}

	@RequiresUser
	@GetMapping(path = "/gotoChangePassword.controller")
	public String gotoChangePassword() {
		return "users/change-password";
	}

	@RejectsUser
	@PostMapping(path = "/login.controller", produces = {"application/json"})
	@ResponseBody
	public Map<String, Object> login(
			@RequestBody LoginRequest loginRequest,
			HttpSession session
	) {
		Map<String, Object> map = new HashMap<>();
		User_Info user_info = null;
		try {
			user_info = userService.login(loginRequest.getU_id(), loginRequest.getU_psw());
			if (user_info != null && user_info.getU_id().length() > 0) {
				map.put("success", "登入成功");
				map.put("loginBean", user_info);

				session.setAttribute("loginBean", user_info);
			} else if (user_info == null) {
				map.put("fail", "帳號或密碼錯誤，請再試一次...");
			}
		} catch (Exception e) {
			map.put("fail", e.getMessage());
		}
		return map;
	}

	@PostMapping(path = "/checkUserId", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> checkUserId(@RequestParam String u_id) {
		Map<String, String> map = new HashMap<>();
		String user_id = userService.checkUserId(u_id);
		map.put("u_id", user_id);
		return map;
	}

	@RejectsUser
	@PostMapping(path = "/userSignup", produces = {"application/json"})
	@ResponseBody
	public Map<String, String> signup(@RequestBody SignupRequest signupRequest) {
		Map<String, String> map = new HashMap<>();
		try {
			if (!(signupRequest.getU_email().trim().contains("@"))) {
				map.put("formatError", "信箱格式錯誤!");
				return map;
			}
		} catch (Exception e) {
			map.put("fail", e.getMessage());
		}


		int n = 0;
		try {
			n = userService.saveUser(signupRequest);
			if (n == 1) {
				map.put("success", "註冊成功");
				//寄成功註冊的信件
				String body = "用戶: " + signupRequest.getU_id() + " 您好，歡迎註冊成為Studie Hub的會員，祝您使用愉快!";
				emailService.sendSimpleEmail(signupRequest.getU_email(),
						body,
						"Studie Hub 會員註冊成功通知");
			} else if (n == -1) {
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
			@RequestParam String old_psw,
			@RequestParam String u_psw,
			@RequestParam String cfm_psw,
			RedirectAttributes ra,
			HttpSession session,
			@SessionAttribute User_Info loginBean
	) {
		if (!(u_psw.equals(cfm_psw))) {
			ra.addFlashAttribute("errorMessageOfChangingPassword", "兩次密碼不同");
			return "redirect:/gotoChangePassword.controller";
		}

		userService.changePassword(loginBean.getU_id(), u_psw);
		updateLoginBean(session);
		ra.addFlashAttribute("successMessageOfChangingPassword", "修改成功");
		return "redirect:/";
	}

	@RequiresUser
	@PostMapping("/updateUserinfo.controller")
	public String updateUser(
			ProfileForm form, @SessionAttribute User_Info loginBean,
			Model model, RedirectAttributes ra, HttpSession session
	) {
		var u_id = loginBean.getU_id();

		var result = userValidator.validate(form);
		if (result.hasErrors()) {
			var view = userService.mapToProfileView(u_id, form);
			model.addAttribute("view", view);
			model.addAttribute(MODEL_KEY_PREFIX + "view", result);
			model.addAttribute("genderList", getGenderList());
			return "users/edit-profile";
		}

		User_Info user_info = userService.applyToEntity(u_id, form);

		Blob blob = null;
		String mimeType = "";
		String ogfName = "";
		MultipartFile uploadImage = user_info.getUploadImage();
		if (uploadImage != null && uploadImage.getSize() > 0) {
			try {
				InputStream is = uploadImage.getInputStream();
				ogfName = uploadImage.getOriginalFilename();
				blob = SystemUtils.inputStreamToBlob(is);
				mimeType = context.getMimeType(ogfName);
				user_info.setU_img(blob);
				user_info.setMimeType(mimeType);
				String ext = StringUtils.getFilenameExtension(ogfName);
				try {
					File imageFolder = new File(AVATAR_STORAGE_DIR);
					if (!imageFolder.exists())
						imageFolder.mkdirs();
					String imageFilename = "MemberImage_" + user_info.getU_id() + "." + ext;
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

		userService.updateUser(user_info);
		updateLoginBean(session);
		ra.addFlashAttribute("successMessage", "修改成功");
		return "redirect:/gotoUpdateUserinfo.controller";
	}


	// ==================== helpers ====================
	public void updateLoginBean(HttpSession session) {
		String u_id = ((User_Info) session.getAttribute("loginBean")).getU_id();
		User_Info updateBean = userService.getSingleUser(u_id);
		session.setAttribute("loginBean", updateBean);
	}

	public Map<String, String> getGenderList() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("男", "男");
		map.put("女", "女");
		return map;
	}
}