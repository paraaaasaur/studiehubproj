package com.group5.springboot.service.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group5.springboot.dao.user.IUserDao;
import com.group5.springboot.model.user.User_Info;

@Service
@Transactional
//@EnableTransactionManagement
public class UserService implements IUserService {
	@Autowired
	IUserDao iUserDao;

	@Override
	//// 檢查帳號是否可用
	public String checkUserId(String u_id) {
		return iUserDao.checkUserId(u_id);
	}

	@Override
	// 會員註冊
	public int saveUser(User_Info user_Info) {
		int n = iUserDao.saveUser(user_Info);
		return n;
	}

	@Override
	// 檢查u_id是否存在
	public boolean isUserExist(User_Info user_Info) {
		return iUserDao.isUserExist(user_Info);
	}

	@Override
	// 登入
	public User_Info login(User_Info user_Info) {
		return iUserDao.login(user_Info);
	}

	@Override
	// 查看全部會員資料
	public List<User_Info> showAllUsers() {
		return iUserDao.showAllUsers();
	}

	@Override
	// 讀取單筆會員資料(全部會員資料到刪除單筆資料)
	public User_Info getSingleUser(String u_id) {
		return iUserDao.getSingleUser(u_id);
	}

	@Override
	// 刪除會員資料
	public void deleteUserById(String u_id) {
		iUserDao.deleteUserById(u_id);
	}

	@Override
	// 修改會員資料
	public void updateUser(User_Info user_Info) {
		iUserDao.updateUser(user_Info);
	}
	

}
