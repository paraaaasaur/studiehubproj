package com.group5.springboot.service.user;

import com.group5.springboot.dto.user.UpdateProfileForm;
import com.group5.springboot.dto.user.UpdateProfileView;
import com.group5.springboot.dto.user.SignupRequest;
import com.group5.springboot.model.user.User_Info;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
	String checkUserId(String u_id);

	int saveUser(SignupRequest signupRequest);

	User_Info login(String u_id, String u_psw);

	List<User_Info> showAllUsers();

	User_Info getSingleUser(String u_id);

	void updateUser(User_Info user_Info);

	void changePassword(String u_id, String new_psw);

	User_Info getUserInfoForForgetPassword(String userEmail);

	boolean setNewPasswordForForgetPsw(String email, String newPassword);

	User_Info applyToEntity(String currentUserId, UpdateProfileForm updateProfileForm);

	UpdateProfileView mapToUpdateProfileView(User_Info currentUser);

	UpdateProfileView mapToUpdateProfileView(String currentUserId, UpdateProfileForm updateProfileForm);
}