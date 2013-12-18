package com.github.dactiv.showcase.dao.account;

import java.util.List;

import com.github.dactiv.orm.core.hibernate.support.HibernateSupportDao;
import com.github.dactiv.showcase.entity.account.Group;
import org.springframework.stereotype.Repository;

/**
 * 部门数据访问
 * 
 * @author vincent
 *
 */
@Repository
public class GroupDao extends HibernateSupportDao<Group, String>{

	/**
	 * 通过用户id获取所有资源
	 * 
	 * @param userId 用户id
	 * 
	 * @return List
	 */
	public List<Group> getUserGorups(String userId) {
		return findByQuery(Group.UserGroups, userId);
	}

}
