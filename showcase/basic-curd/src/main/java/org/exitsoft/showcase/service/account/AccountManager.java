package org.exitsoft.showcase.service.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.orm.core.RestrictionNames;
import org.exitsoft.showcase.common.SystemVariableUtils;
import org.exitsoft.showcase.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.common.enumeration.entity.ResourceType;
import org.exitsoft.showcase.dao.account.GroupDao;
import org.exitsoft.showcase.dao.account.ResourceDao;
import org.exitsoft.showcase.dao.account.UserDao;
import org.exitsoft.showcase.entity.account.Group;
import org.exitsoft.showcase.entity.account.Resource;
import org.exitsoft.showcase.entity.account.User;
import org.exitsoft.showcase.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户管理业务逻辑
 * 
 * @author vincent
 *
 */
@Service
@Transactional
public class AccountManager {
	
	//用户数据访问
	@Autowired
	private UserDao userDao;
	
	//资源数据访问
	@Autowired
	private ResourceDao resourceDao;
	
	//组数据访问
	@Autowired
	private GroupDao groupDao;
	
	//------------------------------用户管理-----------------------------------//
	
	/**
	 * 更新当前用户密码
	 * 
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * 
	 */
	//当修改成功后将shiro的认证缓存也更新，包正下次登录也不需要在次查询数据库
	@CacheEvict(value="shiroAuthenticationCache",
			  	key="T(org.exitsoft.showcase.common.SystemVariableUtils)." +
					"getSessionVariable()." +
					"getUser()." +
					"getUsername()")
	public void updateUserPassword(String oldPassword, String newPassword) {
		User user = SystemVariableUtils.getSessionVariable().getUser();
		
		oldPassword = new SimpleHash("MD5", oldPassword.toCharArray()).toString();
		
		if (!user.getPassword().equals(oldPassword)) {
			throw new ServiceException("旧密码不正确.");
		}
		
		String temp = new SimpleHash("MD5",newPassword).toHex();
		userDao.updatePassword(user.getId(),temp);
		user.setPassword(temp); 
	}
	
	
	/**
	 * 通过id获取用户实体
	 * @param id 用户id
	 */
	public User getUser(String id) {
		return userDao.load(id);
	}
	
	/**
	 * 通过属性过滤器查询用户分页
	 * 
	 * @param request 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<User> searchUserPage(PageRequest request,List<PropertyFilter> filters) {
		
		return userDao.findPage(request, filters);
		
	}
	
	/**
	 * 新增用户
	 * 
	 * @param entity 用户实体
	 */
	public void insertUser(User entity) {
		if (!isUsernameUnique(entity.getUsername())) {
			throw new ServiceException("用户名已存在");
		}
		
		String password = new SimpleHash("MD5", entity.getPassword()).toHex();
		
		entity.setPassword(password);
		userDao.insert(entity);
	}
	
	/**
	 * 更新用户
	 * 
	 * @param entity 用户实体
	 */
	//当更新后将shiro的认证缓存也更新，保证shiro和当前的用户一致
	@CacheEvict(value="shiroAuthenticationCache",key="#entity.getUsername()")
	public void updateUser(User entity) {
		userDao.update(entity);
	}
	
	/**
	 * 是否唯一的用户名 如果是返回true,否则返回false
	 * 
	 * @param username 用户名
	 * 
	 * @return boolean
	 */
	public boolean isUsernameUnique(String username) {
		return getUserByUsername(username) == null;
	}
	
	/**
	 * 删除用户
	 * 
	 * @param ids 用户id集合
	 */
	public void deleteUsers(List<String> ids) {
		userDao.deleteAll(ids);
	}

	/**
	 * 通过用户名获取用户实体
	 * 
	 * @param username 用户实体
	 * 
	 * @return {@link User}
	 */
	public User getUserByUsername(String username) {
		return userDao.findUniqueByProperty("username", username);
	}
	
	//------------------------------资源管理-----------------------------------//
	
	/**
	 * 通过id获取资源实体
	 * 
	 * @param id 资源id
	 * 
	 * @return {@link Resource}
	 */
	public Resource getResource(String id) {
		return resourceDao.load(id);
	}
	
	/**
	 * 通过id集合获取资源资源
	 * 
	 * @param ids 资源集合
	 * 
	 * @return List
	 */
	public List<Resource> getResources(List<String> ids) {
		return resourceDao.get(ids);
	}
	
	/**
	 * 通过属性过滤器查询资源分页
	 * 
	 * @param request 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<Resource> searchResourcePage(PageRequest request,List<PropertyFilter> filters) {
		return resourceDao.findPage(request, filters);
	}
	
	/**
	 * 保存资源实体
	 * 
	 * @param entity 资源实体
	 */
	public void saveResource(Resource entity) {
		
		if(entity.getSort() == null) {
			entity.setSort(resourceDao.entityCount() + 1);
		}
		
		if (entity.getParent() != null) {
			entity.getParent().setLeaf(true);
		}
		
		resourceDao.save(entity);
		resourceDao.refreshAllLeaf();
	}
	
	/**
	 * 通过资源id删除资源
	 * 
	 * @param ids 资源id集合 
	 */
	public void deleteResources(List<String> ids) {
		resourceDao.deleteAll(ids);
		resourceDao.refreshAllLeaf();
	}
	
	/**
	 * 获取所有资源
	 * 
	 * @param ignoreIdValue 要忽略的id属性值
	 * 
	 * @return List
	 */
	public List<Resource> getResources(String... ignoreIdValue) {
		
		if(ArrayUtils.isNotEmpty(ignoreIdValue)) {
			return resourceDao.findByProperty("id", ignoreIdValue, RestrictionNames.NIN);
		}
		
		return resourceDao.getAll();
	}
	
	/**
	 * 获取资源实体的总记录数
	 * 
	 * @return long
	 */
	public long getResourceCount() {
		return resourceDao.entityCount();
	}
	
	/**
	 * 通过用户id获取该用户下的所有资源
	 * 
	 * @param userId 用户id
	 * 
	 * @return List
	 */
	public List<Resource> getUserResources(String userId) {
		return resourceDao.getUserResources(userId);
	}
	
	/**
	 * 并合子类资源到父类中，返回一个新的资源集合
	 * 
	 * @param list 资源集合
	 * @param resourceType 不需要并合的资源类型
	 */
	public List<Resource> mergeResourcesToParent(List<Resource> list,ResourceType ignoreType) {
		List<Resource> result = new ArrayList<Resource>();
		
		for (Resource r : list) {
			if (r.getParent() == null && !StringUtils.equals(ignoreType.getValue(),r.getType())) {
				mergeResourcesToParent(list,r,ignoreType);
				result.add(r);
			}
		}
		
		return result;
	}
	
	/**
	 * 遍历list中的数据,如果数据的父类与parent相等，将数据加入到parent的children中
	 * 
	 * @param list 资源集合
	 * @param parent 父类对象
	 * @param ignoreType 不需要加入到parent的资源类型
	 */
	private void mergeResourcesToParent(List<Resource> list, Resource parent,ResourceType ignoreType) {
		if (!parent.getLeaf()) {
			return ;
		}
		
		parent.setChildren(new ArrayList<Resource>());
		
		for (Resource r: list) {
			//这是一个递归过程，如果当前遍历的r资源的parentId等于parent父类对象的id，将会在次递归r对象。通过遍历list是否也存在r对象的子级。
			if (!StringUtils.equals(r.getType(), ignoreType.getValue()) && StringUtils.equals(r.getParentId(),parent.getId()) ) {
				r.setChildren(null);
				mergeResourcesToParent(list,r,ignoreType);
				parent.getChildren().add(r);
			}
			
		}
	}
	
	//------------------------------组管理-----------------------------------//
	
	/**
	 * 通过id获取组实体
	 * 
	 * @param id 组id
	 * 
	 * @return {@link Group}
	 */
	public Group getGroup(String id) {
		return groupDao.load(id);
	}
	
	/**
	 * 通过组id，获取组集合
	 * 
	 * @param ids id集合
	 * 
	 * @return List
	 */
	public List<Group> getGroups(List<String> ids) {
		return groupDao.get(ids);
	}
	
	/**
	 * 保存组实体
	 * 
	 * @param entity 组实体
	 */
	@CacheEvict(value="shiroAuthorizationCache",allEntries=true)
	public void saveGroup(Group entity) {
		groupDao.save(entity);
	}
	
	/**
	 * 删除组实体
	 * 
	 * @param ids 组id
	 */
	@CacheEvict(value="shiroAuthorizationCache",allEntries=true)
	public void deleteGroups(List<String> ids) {
		groupDao.deleteAll(ids);
	}

	/**
	 * 通过属性过滤器查询组分页
	 * 
	 * @param request 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<Group> searchGroupPage(PageRequest request,List<PropertyFilter> filters) {
		
		return groupDao.findPage(request, filters);
	}
	
	/**
	 * 根据组类型获取所有组信息
	 * 
	 * @param groupType 组类型
	 * @param ignoreIdValue 要忽略的id属性值
	 * 
	 * @return List
	 */
	public List<Group> getGroup(GroupType groupType,String... ignoreIdValue) {
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		
		if (ArrayUtils.isNotEmpty(ignoreIdValue)) {
			filters.add(PropertyFilters.build("NES_id", StringUtils.join(ignoreIdValue,",")));
		}
		
		filters.add(PropertyFilters.build("EQS_type", groupType.getValue()));
		
		return groupDao.findByPropertyFilter(filters);
	}

	/**
	 * 通过用户id获取所有资源
	 * 
	 * @param userId 用户id
	 * 
	 * @return List
	 */
	public List<Group> getUserGroups(String userId) {
		return groupDao.getUserGorups(userId);
	}

}
