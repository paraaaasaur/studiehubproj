package com.group5.springboot.service.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group5.springboot.dao.user.IUserDao;
import com.group5.springboot.model.user.User_Info;

@Service
@Transactional
public class UserService implements IUserService {
	@Autowired IUserDao iUserDao;


	@Override
	public String checkUserId(String u_id) {
		return iUserDao.checkUserId(u_id);
	}

	@Override
	public int saveUser(User_Info user_Info) {
		int n = iUserDao.saveUser(user_Info);
		return n;
	}

	@Override
	public User_Info login(User_Info user_Info) {
		return iUserDao.login(user_Info);
	}

	@Override
	public List<User_Info> showAllUsers() {
		return iUserDao.showAllUsers();
	}

	@Override
	public User_Info getSingleUser(String u_id) {
		return iUserDao.getSingleUser(u_id);
	}

	@Override
	public void updateUser(User_Info user_Info) {
		iUserDao.updateUser(user_Info);
	}

	@Override
	public User_Info getUserInfoForForgetPassword(String userEmail) {
		return iUserDao.getUserInfoForForgetPassword(userEmail);
	}
	
	@Override
	public boolean setNewPasswordForForgetPsw(String email, String newPassword) {
		return iUserDao.setNewPasswordForForgetPsw(email, newPassword);
	}
}