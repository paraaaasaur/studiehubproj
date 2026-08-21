package com.group5.springboot.service.user;

import com.group5.springboot.dao.user.UserDao;
import com.group5.springboot.dto.user.ProfileForm;
import com.group5.springboot.dto.user.ProfileView;
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
	public User_Info applyToEntity(String currentUserId, ProfileForm profileForm) {
		User_Info dbEntity = userDao.getSingleUser(currentUserId);
		dbEntity.setU_id(currentUserId);
		dbEntity.setU_lastname(profileForm.getU_lastname());
		dbEntity.setU_firstname(profileForm.getU_firstname());
		dbEntity.setU_address(profileForm.getU_address());
		dbEntity.setU_email(profileForm.getU_email());
		dbEntity.setU_tel(profileForm.getU_tel());
		dbEntity.setU_birthday(profileForm.getU_birthday());
		dbEntity.setU_gender(profileForm.getU_gender());
		dbEntity.setUploadImage(profileForm.getUploadImage());

		return dbEntity;
	}

	@Override
	public ProfileView mapToProfileView(User_Info currentUser) {
		return new ProfileView(
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
	public ProfileView mapToProfileView(String currentUserId, ProfileForm profileForm) {
		return new ProfileView(
				currentUserId,
				profileForm.getU_lastname(),
				profileForm.getU_firstname(),
				profileForm.getU_address(),
				profileForm.getU_email(),
				profileForm.getU_tel(),
				profileForm.getU_birthday(),
				profileForm.getU_gender()
		);
	}
}