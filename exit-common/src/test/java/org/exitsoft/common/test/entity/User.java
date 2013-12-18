package org.exitsoft.common.test.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.exitsoft.common.utils.CollectionUtils;

@XmlRootElement(name="user")
@SuppressWarnings("serial")
public class User extends UniversallyUniqueIdentifier{
	//登录名称
	private String username = "vincent";
	//登录密码
	private String password = "dkfjsoj42kl34j2348aslkdfj";
	//真实名称
	private String realname = "chen";
	//状态 
	private Integer state = new Integer(1);
	//邮件
	private String email = null;
	//用户所在的组
	private List<Group> groupsList;
	
	/**
	 * 构造方法
	 */
	public User() {
	}
	
	/**
	 * 获取登录名称
	 * 
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置登录名称
	 * 
	 * @param username 登录名称
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取登录密码
	 * 
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置登录密码
	 * 
	 * @param password 登录密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取真实姓名
	 * 
	 * @return String
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * 设置真实名称
	 * 
	 * @param realName 真实姓名
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}


	/**
	 * 获取用户状态
	 * 
	 * @return Integer
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * 设置用户状态
	 * 
	 * @param state 用户状态
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	
	/**
	 * 获取邮件
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置邮件 
	 * @param email 邮件地址 
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * 获取该用户所在的组
	 * 
	 * @return List
	 */
	public List<Group> getGroupsList() {
		return groupsList;
	}
	
	/**
	 * 设置用户所在的组
	 * 
	 * @param groupsList 组集合
	 */
	public void setGroupsList(List<Group> groupsList) {
		this.groupsList = groupsList;
	}
	
	/**
	 * 获取所在组所用名称
	 * 
	 * @return String
	 */
	public String getGroupNames() {
		return CollectionUtils.extractToString(this.groupsList, "name", ",");
	}

}
