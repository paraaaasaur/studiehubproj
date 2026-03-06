package com.group5.springboot.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.group5.springboot.model.user.User_Info;

@Repository
public interface IUserDao {

	String checkUserId(String u_id);

	int saveUser(User_Info user_Info);

	User_Info login(User_Info user_Info);

	List<User_Info> showAllUsers();

	User_Info getSingleUser(String u_id);

	void updateUser(User_Info user_Info);

	User_Info getUserInfoForForgetPassword(String userEmail);

	boolean setNewPasswordForForgetPsw(String email, String newPassword);

}
