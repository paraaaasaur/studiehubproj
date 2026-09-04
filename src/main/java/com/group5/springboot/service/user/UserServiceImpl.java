package com.group5.springboot.service.user;

import com.group5.springboot.dao.user.UserDao;
import com.group5.springboot.dto.user.UpdateProfileForm;
import com.group5.springboot.dto.user.UpdateProfileView;
import com.group5.springboot.dto.user.SignupRequest;
import com.group5.springboot.model.user.User_Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	final UserDao userDao;


	@Autowired
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}


	@Override
	public String checkUserId(String u_id) {
		return userDao.checkUserId(u_id);
	}

	@Override
	public int saveUser(SignupRequest signupRequest) {
		int n = userDao.saveUser(signupRequest);
		return n;
	}

	@Override
	public User_Info login(String u_id, String u_psw) {
		return userDao.login(u_id, u_psw);
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
	public void changePassword(String u_id, String new_psw) {
		var found = userDao.getSingleUser(u_id);
		found.setU_psw(new_psw);

		userDao.updateUser(found);
	}

	@Override
	public User_Info getUserInfoForForgetPassword(String userEmail) {
		return userDao.getUserInfoForForgetPassword(userEmail);
	}
	
	@Override
	public boolean setNewPasswordForForgetPsw(String email, String newPassword) {
		return userDao.setNewPasswordForForgetPsw(email, newPassword);
	}

	@Override
	public User_Info applyToEntity(String currentUserId, UpdateProfileForm updateProfileForm) {
		User_Info dbEntity = userDao.getSingleUser(currentUserId);
		dbEntity.setU_id(currentUserId);
		dbEntity.setU_lastname(updateProfileForm.getU_lastname());
		dbEntity.setU_firstname(updateProfileForm.getU_firstname());
		dbEntity.setU_address(updateProfileForm.getU_address());
		dbEntity.setU_email(updateProfileForm.getU_email());
		dbEntity.setU_tel(updateProfileForm.getU_tel());
		dbEntity.setU_birthday(updateProfileForm.getU_birthday());
		dbEntity.setU_gender(updateProfileForm.getU_gender());
		dbEntity.setUploadImage(updateProfileForm.getUploadImage());

		return dbEntity;
	}

	@Override
	public UpdateProfileView mapToUpdateProfileView(User_Info currentUser) {
		return new UpdateProfileView(
				currentUser.getU_id(),
				currentUser.getU_lastname(),
				currentUser.getU_firstname(),
				currentUser.getU_address(),
				currentUser.getU_email(),
				currentUser.getU_tel(),
				currentUser.getU_birthday(),
				currentUser.getU_gender()
		);
	}

	@Override
	public UpdateProfileView mapToUpdateProfileView(String currentUserId, UpdateProfileForm updateProfileForm) {
		return new UpdateProfileView(
				currentUserId,
				updateProfileForm.getU_lastname(),
				updateProfileForm.getU_firstname(),
				updateProfileForm.getU_address(),
				updateProfileForm.getU_email(),
				updateProfileForm.getU_tel(),
				updateProfileForm.getU_birthday(),
				updateProfileForm.getU_gender()
		);
	}
}