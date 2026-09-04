package com.group5.springboot.dao.user;

import com.group5.springboot.dto.user.SignupRequest;
import com.group5.springboot.model.user.User_Info;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

	String checkUserId(String u_id);

	int saveUser(SignupRequest signupRequest);

	User_Info login(String u_id, String u_psw);

	List<User_Info> showAllUsers();

	User_Info getSingleUser(String u_id);

	void updateUser(User_Info user_Info);

	User_Info getUserInfoForForgetPassword(String userEmail);

	boolean setNewPasswordForForgetPsw(String email, String newPassword);

}
