package com.group5.springboot.service.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group5.springboot.model.user.User_Info;

//@Repository
@Service
public interface IUserService {

	// 檢查帳號是否可用
	public String checkUserId(String u_id);

	// 會員註冊
	public int saveUser(User_Info user_Info);

	// 檢查u_id是否存在
	public boolean isUserExist(User_Info user_Info);

	// 登入
	public User_Info login(User_Info user_Info);

	// 查看全部會員資料
	public List<User_Info> showAllUsers();

	// 讀取單筆會員資料(全部會員資料到刪除單筆資料)
	public User_Info getSingleUser(String u_id);

	// 刪除會員資料
	public void deleteUserById(String u_id);

	// 修改會員資料
	public void updateUser(User_Info user_Info);

}
