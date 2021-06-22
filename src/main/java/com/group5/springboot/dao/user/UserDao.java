package com.group5.springboot.dao.user;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group5.springboot.model.user.User_Info;

@Repository
public class UserDao implements IUserDao {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	User_Info user_info;
	
	@Override
	//檢查帳號是否可用
	public String checkUserId(String u_id) {
		try {
			User_Info user_Info = em.find(User_Info.class, u_id);
			if(user_Info == null) {
				return "";
			}else {
				System.out.println("********************************");
				System.out.println("送查詢的u_id: \'" + user_Info.getU_id() + "\'");
				System.out.println("********************************");
				return "帳號已存在";
			}
		} catch (Exception e) {
			System.out.println("********************************");
			System.out.println("厚唷 有錯誤訊息，在\'檢查帳號是否可用\'");
			System.out.println("********************************");
			return "Error，請再試一次!";
		}
		
	}
	
	@Override
	//會員註冊
	public int saveUser(User_Info user_Info) {
		int n = 0;
		boolean exist = isUserExist(user_Info);
		if(exist) {
			return -1;
		}
		try {
			em.persist(user_Info);
			n = 1;
		} catch (Exception e) {
			System.out.println("********************************");
			System.out.println("厚唷 有錯誤訊息，在\'會員註冊\'");
			n = -2;
			System.out.println("********************************");
		}
		return n;
	}
	
	
	@Override
	//登入
	public User_Info login(User_Info user_Info) {
		user_info = null;
		String hql = "from User_Info where u_id=:id and u_psw=:psw";
		try {
			Query<User_Info> query = (Query<User_Info>) em.createQuery(hql, User_Info.class)
			.setParameter("id", user_Info.getU_id())
			.setParameter("psw", user_Info.getU_psw());
			User_Info loginBean = query.uniqueResult();
			if(loginBean != null && !(loginBean.getU_id().length() == 0)) {
				//測試
				System.out.println("********************************");
				System.out.println("id: " + loginBean.getU_id() + ", psw: " + loginBean.getU_psw());
				System.out.println("********************************");
				user_info = loginBean;
			}else {
				user_info = null;
			}
		} catch (Exception e) {
			System.out.println("********************************");
			System.out.println("厚唷 有錯誤訊息，在\'登入\'");
			System.out.println("********************************");
		}
		return user_info;
	}
	
	
	
	
	@Override
	//檢查u_id是否存在
	public boolean isUserExist(User_Info user_Info) {
		System.out.println("********************************");
		System.out.println("user_Infor: " + user_Info.getU_id());
		System.out.println("********************************");
		boolean exist = false;
		try {
			User_Info ckResult = em.find(User_Info.class, user_Info.getU_id());
			if(!(ckResult == null) && !(ckResult.getU_id().length() == 0) ) {
			exist = true;
			}
		} catch (Exception e) {
			System.out.println("********************************");
			System.out.println("又有錯了 厚唷，在\'檢查u_id是否存在\'");
			System.out.println("********************************");
		}
		System.out.println("********************************");
		System.out.println("帳號已存在? : " + exist);
		System.out.println("********************************");
		return exist;
	}
	
	@Override
	//查看全部會員資料
	@SuppressWarnings("unchecked")
	public List<User_Info> showAllUsers(){
		String hql = "from User_Info";
		List<User_Info> list = em.createQuery(hql).getResultList();
		return list;
	}
	
	
	@Override
	//讀取單筆會員資料(全部會員資料到刪除單筆資料)
	public User_Info getSingleUser(String u_id) {
		User_Info getUserResult = em.find(User_Info.class, u_id);
		return getUserResult;
	}
	
	@Override
	//刪除會員資料
	public void deleteUserById(String u_id) {
		user_info.setU_id(u_id);
		em.remove(user_info);
	}
	
	@Override
	// 修改會員資料
	public void updateUser(User_Info user_Info) {
		em.merge(user_Info);
	}
	
	
	
	
	
	
	
}
