package com.group5.springboot.dao.user;

import com.group5.springboot.dto.user.SignupRequest;
import com.group5.springboot.model.user.User_Info;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
	final EntityManager em;


	@Autowired
	public UserDaoImpl(EntityManager em) {
		this.em = em;
	}


	@Override
	public String checkUserId(String u_id) {
		try {
			User_Info user_Info = em.find(User_Info.class, u_id);
			if (user_Info == null) {
				return "";
			} else {
				return "帳號已存在";
			}
		} catch (Exception e) {
			return "Error，請再試一次!";
		}
	}
	
	@Override
	public int saveUser(SignupRequest signupRequest) {
		int n = 0;
		boolean exist = false;

		User_Info ckResult = em.find(User_Info.class, signupRequest.getU_id());
		if (!(ckResult == null) && !(ckResult.getU_id().length() == 0)) {
			exist = true;
		}

		if(exist) {
			return -1;
		}

		try {
			em.persist(signupRequest.toEntity());
			n = 1;
		} catch (Exception e) {
			n = -2;
		}
		return n;
	}

	@Override
	public User_Info login(String u_id, String u_psw) {
		User_Info user_info = null;
		String hql = "from User_Info where u_id=:id and u_psw=:psw";
		try {
			Query<User_Info> query = (Query<User_Info>) em.createQuery(hql, User_Info.class)
					.setParameter("id", u_id)
					.setParameter("psw", u_psw);
			User_Info loginBean = query.uniqueResult();
			if (loginBean != null && !(loginBean.getU_id().length() == 0)) {
				user_info = loginBean;
			} else {
				user_info = null;
			}
		} catch (Exception e) {
			// do nothing
		}
		return user_info;
	}
	
	@Override
	public List<User_Info> showAllUsers() {
		String hql = "from User_Info";
		List<User_Info> list = em.createQuery(hql).getResultList();
		return list;
	}

	@Override
	public User_Info getSingleUser(String u_id) {
		return em.find(User_Info.class, u_id);
	}

	@Override
	public void updateUser(User_Info user_Info) {
		em.merge(user_Info);
	}
	
	@Override
	public User_Info getUserInfoForForgetPassword(String userEmail) {
		User_Info user_info = null;
		String hql = "from User_Info where u_email=:email";
		try {
			Query<User_Info> query = (Query<User_Info>) em.createQuery(hql, User_Info.class)
					.setParameter("email", userEmail);
			User_Info result = query.uniqueResult();
			if (result != null && !(result.getU_id().length() == 0)) {
				user_info = result;
			} else {
				user_info = null;
			}
		} catch (Exception e) {
			// do nothing
		}

		return user_info;
	}

	@Override
	public boolean setNewPasswordForForgetPsw(String email, String newPassword) {
		boolean result = false;
		try {
			javax.persistence.Query query = em.createNativeQuery("UPDATE user_info SET u_psw = :password WHERE u_email = :inputEmail", User_Info.class);
			query.setParameter("password", newPassword);
			query.setParameter("inputEmail", email);
			int executeUpdate = query.executeUpdate();
			if (executeUpdate > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}