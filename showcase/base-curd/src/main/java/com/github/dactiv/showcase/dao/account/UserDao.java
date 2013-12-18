package com.github.dactiv.showcase.dao.account;

import com.github.dactiv.orm.core.hibernate.support.HibernateSupportDao;
import com.github.dactiv.showcase.entity.account.User;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问
 * @author maurice
 *
 */
@Repository
public class UserDao extends HibernateSupportDao<User, String>{

	/**
	 * 通过用户id更新用户密码
	 * 
	 * @param userId 用户id
	 * @param password 密码
	 */
	public void updatePassword(String userId, String password) {
		executeUpdate(User.UpdatePassword, password,userId);
	}

	
}
