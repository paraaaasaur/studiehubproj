package com.group5.springboot.service.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group5.springboot.model.user.User_Info;

@Service
public interface IUserService {
	String checkUserId(String u_id);

	int saveUser(User_Info user_Info);

	User_Info login(User_Info user_Info);

	List<User_Info> showAllUsers();

	User_Info getSingleUser(String u_id);

	void updateUser(User_Info user_Info);
	
	User_Info getUserInfoForForgetPassword(String userEmail);

	boolean setNewPasswordForForgetPsw(String email, String newPassword);

}
