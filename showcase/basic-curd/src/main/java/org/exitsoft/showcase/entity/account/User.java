package org.exitsoft.showcase.entity.account;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.exitsoft.common.utils.CollectionUtils;
import org.exitsoft.showcase.common.SystemVariableUtils;
import org.exitsoft.showcase.common.enumeration.entity.State;
import org.exitsoft.showcase.entity.IdEntity;
import org.hibernate.annotations.NamedQuery;

/**
 * 用户类
 * @author vincent
 *
 */
@Entity
@Table(name="TB_USER")
@NamedQuery(name=User.UpdatePassword,query="update User u set u.password = ?1 where u.id = ?2")
public class User extends IdEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 更新用户密码NamedQuery
	 */
	public static final String UpdatePassword = "updatePassword";
	
	//登录名称
	private String username;
	//登录密码
	private String password;
	//真实名称
	private String realname;
	//状态 
	private Integer state;
	//邮件
	private String email;
	//用户所在的组
	private List<Group> groupsList = new ArrayList<Group>();
	//用户头像
	private String portrait;
	
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
	@Column(length=32,unique=true,nullable=false,updatable=false)
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
	@Column(nullable=false,length=32,updatable=false)
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
	@Column(length=64,nullable=false)
	public String getRealname() {
		return realname;
	}

	/**
	 * 设置真实名称
	 * 
	 * @param realname 真实姓名
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}


	/**
	 * 获取用户状态
	 * 
	 * @return {@link State}
	 */
	@Column(nullable=false, length = 1)
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
	@Column(length=128)
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
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "TB_GROUP_USER", joinColumns = { @JoinColumn(name = "FK_USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "FK_GROUP_ID") })
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
	 * 获取状态名称
	 * 
	 * @return String
	 */
	@Transient
	public String getStateName() {
		return SystemVariableUtils.getName(State.class, this.state);
	}
	
	/**
	 * 获取所在组所用名称
	 * 
	 * @return String
	 */
	@Transient
	public String getGroupNames() {
		return CollectionUtils.extractToString(this.groupsList, "name", ",");
	}

	/**
	 * 获取用户头像
	 * 
	 * @return String
	 */
	@Column(length=256)
	public String getPortrait() {
		return portrait;
	}

	/**
	 * 设置用户头像
	 * 
	 * @param portrait 头像
	 */
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	
}
