package org.exitsoft.showcase.entity.account;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.exitsoft.common.utils.CollectionUtils;
import org.exitsoft.showcase.common.SystemVariableUtils;
import org.exitsoft.showcase.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.common.enumeration.entity.State;
import org.exitsoft.showcase.entity.IdEntity;
import org.hibernate.annotations.NamedQuery;

/**
 * 组实体
 * 
 * @author vincent
 *
 */
@Entity
@Table(name="TB_GROUP")
@NamedQuery(name=Group.UserGroups,
            query="select gl from User u left join u.groupsList gl  where u.id=?1 and gl.type= '03'")
public class Group extends IdEntity{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 获取用户所有组集合NamedQuery
	 */
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
	//拥有资源
	private List<Resource> resourcesList = new ArrayList<Resource>();
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
	@Column(length=32,nullable=false,unique=true)
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
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "FK_PARENT_ID")
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
	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent",cascade={CascadeType.ALL})
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
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "TB_GROUP_USER", joinColumns = { @JoinColumn(name = "FK_GROUP_ID") }, inverseJoinColumns = { @JoinColumn(name = "FK_USER_ID") })
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
	 * 获取拥有资源
	 * 
	 * @return List
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "TB_GROUP_RESOURCE", joinColumns = { @JoinColumn(name = "FK_GROUP_ID") }, inverseJoinColumns = { @JoinColumn(name = "FK_RESOURCE_ID") })
	public List<Resource> getResourcesList() {
		return resourcesList;
	}

	/**
	 * 设置该组的操作资源
	 * 
	 * @param resourcesList 资源集合
	 */
	public void setResourcesList(List<Resource> resourcesList) {
		this.resourcesList = resourcesList;
	}

	/**
	 * 获取组类型
	 * 
	 * @return String
	 */
	@Column(nullable=false,length=2)
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
	@Column(length=512)
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
	@Column(nullable=false,length = 1)
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
	 * 获取所有成员的id
	 * 
	 * @return List
	 */
	@Transient
	public List<String> getMemberIds() {
		return CollectionUtils.extractToList(this.membersList, "id");
	}
	
	/**
	 * 获取当前实体是否是为根节点,如果是返回ture，否则返回false
	 * 
	 * @return boolean
	 */
	@Transient
	public boolean getLeaf() {
		return this.children != null && children.size() > 0;
		
	}
	
	/**
	 * 获取shiro role字符串
	 * @return String
	 */
	@Column(length=64)
	public String getRole() {
		return role;
	}

	/**
	 * 设置shiro role字符串
	 * @param role 字符串
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * 获取shiro role连定义的值
	 * @return String
	 */
	@Column(length=256)
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

	/**
	 * 获取父类ID
	 * 
	 * @return String
	 */
	@Transient
	public String getParentId() {
		
		return this.parent == null ? "" : this.parent.getId();
	}
	
	/**
	 * 获取父类名称
	 * 
	 * @return String
	 */
	@Transient
	public String getParentName(){
		return this.parent == null ? "" : this.parent.getName();
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
}
