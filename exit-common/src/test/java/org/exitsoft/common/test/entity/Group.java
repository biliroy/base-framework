package org.exitsoft.common.test.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * 组实体
 * 
 * @author vincent
 *
 */
@XmlRootElement
@SuppressWarnings("serial")
public class Group extends UniversallyUniqueIdentifier{

	public static final String UserGroups = "userGroups";
	
	//名称
	private String name;
	//成员
	private List<User> membersList = new ArrayList<User>();
	//上级组
	private Group parent;
	//下级组集合
	private List<Group> children = new ArrayList<Group>();
	//类型
	private String type;
	//备注
	private String remark;
	//状态
	private int state;
	//shiro role 字符串
	private String role;
	//shiro role连定义的值
	private String value;
	
	/**
	 * 构造方法
	 */
	public Group() {
		
	}

	/**
	 * 获取组名称
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置组名称
	 * 
	 * @param name 组名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取上级组
	 * 
	 * @return {@link Group}
	 */
	public Group getParent() {
		return parent;
	}

	/**
	 * 设置上级组
	 * 
	 * @param parent 组实体
	 */
	public void setParent(Group parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级组集合
	 * 
	 * @return List
	 */
	public List<Group> getChildren() {
		
		return children;
	}

	/**
	 * 设置下级组集合
	 * 
	 * @param children 下级组集合
	 */
	public void setChildren(List<Group> children) {
		this.children = children;
	}
	
	/**
	 * 获取组成员
	 * 
	 * @return List
	 */
	public List<User> getMembersList() {
		return membersList;
	}

	/**
	 * 设置组成员
	 * 
	 * @param membersList 用户集合
	 */
	public void setMembersList(List<User> membersList) {
		this.membersList = membersList;
	}

	/**
	 * 获取组类型
	 * 
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置组类型
	 * 
	 * @param type 类型
	 * 
	 * @see GroupType
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 获取备注
	 * 
	 * @return String
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 获取组状态
	 * @return
	 */
	public int getState() {
		return state;
	}

	/**
	 * 设置组状态
	 * @param state
	 */
	public void setState(int state) {
		this.state = state;
	}
	
	/**
	 * 获取当前实体是否是为根节点,如果是返回ture，否则返回false
	 * 
	 * @return boolean
	 */
	public boolean getLeaf() {
		return this.children != null && children.size() > 0;
		
	}
	
	/**
	 * 获取shiro role字符串
	 * @return String
	 */
	public String getRole() {
		return role;
	}

	/**
	 * 设置shiro role字符串
	 * @param role字符串
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * 获取shiro role连定义的值
	 * @return String
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置 shiro role连定义的值
	 * @param value 值
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
