package com.group5.springboot.service.user;

import com.group5.springboot.dto.user.ProfileForm;
import com.group5.springboot.dto.user.ProfileView;
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

	User_Info applyToEntity(String currentUserId, ProfileForm profileForm);

	ProfileView mapToProfileView(User_Info currentUser);

	ProfileView mapToProfileView(String currentUserId, ProfileForm profileForm);
}