package com.group5.springboot.service.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group5.springboot.dao.user.UserDao;
import com.group5.springboot.model.user.User_Info;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired UserDao userDao;


	@Override
	public String checkUserId(String u_id) {
		return userDao.checkUserId(u_id);
	}

	@Override
	public int saveUser(User_Info user_Info) {
		int n = userDao.saveUser(user_Info);
		return n;
	}

	@Override
	public User_Info login(User_Info user_Info) {
		return userDao.login(user_Info);
	}

	@Override
	public List<User_Info> showAllUsers() {
		return userDao.showAllUsers();
	}

	@Override
	public User_Info getSingleUser(String u_id) {
		return userDao.getSingleUser(u_id);
	}

	@Override
	public void updateUser(User_Info user_Info) {
		userDao.updateUser(user_Info);
	}

	@Override
	public User_Info getUserInfoForForgetPassword(String userEmail) {
		return userDao.getUserInfoForForgetPassword(userEmail);
	}
	
	@Override
	public boolean setNewPasswordForForgetPsw(String email, String newPassword) {
		return userDao.setNewPasswordForForgetPsw(email, newPassword);
	}
}