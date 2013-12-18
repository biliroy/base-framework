package org.exitsoft.orm.test.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.exitsoft.orm.annotation.StateDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "TB_ACCOUNT_USER")
@StateDelete(propertyName = "state", value = "3")
@NamedQueries({
	@NamedQuery(name="QueryUserJpa",query="from User u where u.loginName=?1"),
	@NamedQuery(name="QueryUser",query="from User u where u.loginName=?")
})
public class User extends UniversallyUniqueIdentifier {

	// 登录名称
	private String loginName;

	// 登录密码
	private String password;

	// 真实名称
	private String realName;

	private Integer state;
	
	private String wubiCode;
	
	private String pinyinCode;
	
	private Date createTime;

	// 拥有角色
	private List<Role> roleList;

	public User(String loginName, String password, String realName) {
		super();
		this.loginName = loginName;
		this.password = password;
		this.realName = realName;
	}

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
	public String getLoginName() {
		return loginName;
	}

	/**
	 * 设置登录名称
	 * 
	 * @param loginName
	 *            登录名称
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
	 * @param password
	 *            登录密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取真实姓名
	 * 
	 * @return String
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置真实名称
	 * 
	 * @param realName
	 *            真实姓名
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 获取拥有角色
	 * 
	 * @return {@link Role}
	 */
	@NotAudited
	@ManyToMany
	@JoinTable(name = "TB_ACCOUNT_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	public List<Role> getRoleList() {
		return roleList;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getWubiCode() {
		return wubiCode;
	}

	public void setWubiCode(String wubiCode) {
		this.wubiCode = wubiCode;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPinyinCode() {
		return pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	/**
	 * 设置用户角色
	 * 
	 * @param role
	 *            拥有角色
	 */
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
}
