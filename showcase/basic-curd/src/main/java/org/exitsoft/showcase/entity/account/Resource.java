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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.exitsoft.showcase.common.SystemVariableUtils;
import org.exitsoft.showcase.common.enumeration.entity.ResourceType;
import org.exitsoft.showcase.entity.IdEntity;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;


/**
 * 资源安全类
 * 
 * @author vincent
 *
 */
@Entity
@Table(name="TB_RESOURCE")
@NamedQueries({
	@NamedQuery(name=Resource.UserResources,
				query="select rl from User u left join u.groupsList gl left join gl.resourcesList rl where u.id=?1 and gl.type= '03' order by rl.sort"),
	@NamedQuery(name=Resource.LeafTureNotAssociated,
				query="from Resource r where r.leaf = 1 and (select count(sr) from Resource sr where sr.parent.id = r.id) = 0")
})
public class Resource extends IdEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 通过用户id和资源类型获取该用户下的所有资源
	 */
	public static final String UserResources = "userResources";
	
	/**
	 * 获取所有资源的leaf = true 并且没有子类的资源
	 */
	public static final String LeafTureNotAssociated = "leafTureNotAssociated";
	
	//名称
	private String name;
	//action url
	private String value;
	//父类
	private Resource parent;
	//顺序值
	private Long sort;
	//是否包含叶子节点
	private boolean leaf;
	//子类
	private List<Resource> children = new ArrayList<Resource>();
	//备注
	private String remark;
	//资源类型
	private String type;
	//资源所对应的组集合
	private List<Group> groupsList = new ArrayList<Group>();
	//shiro permission 字符串
	private String permission;
	//图标
	private String icon;
	
	/**
	 * 构造方法
	 */
	public Resource() {
		
	}

	/**
	 * 获取资源名称
	 * 
	 * @return String
	 */
	@Column(length=32,nullable=false,unique=true)
	public String getName() {
		return name;
	}

	/**
	 * 设置资源名称
	 * 
	 * @param name 资源名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取资源操作URL
	 * 
	 * @return String
	 */
	@Column(length=256)
	public String getValue() {
		return value;
	}

	/**
	 * 设置资源操作URL
	 * 
	 * @param value 资源操作URL
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取父类资源
	 * 
	 * @return {@link Resource}
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_PARENT_ID")
	public Resource getParent() {
		return parent;
	}

	/**
	 * 设置父类资源
	 * 
	 * @param parent 父类资源
	 */
	public void setParent(Resource parent) {
		this.parent = parent;
	}

	/**
	 * 获取顺序值
	 * 
	 * @return Integer
	 */
	@Column(nullable=false)
	public Long getSort() {
		return sort;
	}

	/**
	 * 设置顺序值
	 * 
	 * @param sort 顺序值
	 */
	public void setSort(Long sort) {
		this.sort = sort;
	}

	
	/**
	 * 获取当前资源是否包含叶子节点,如果是返回ture，否则返回false
	 * 
	 * @return boolean
	 */
	public Boolean getLeaf() {
		return this.leaf;
	}
	
	/**
	 * 设置当前资源是否包含叶子节点,
	 * 
	 * @return boolean ture表示是，否则返回false
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * 获取子类资源
	 * 
	 * @return List
	 */
	@OrderBy("sort ASC")
	@OneToMany(mappedBy = "parent",fetch = FetchType.LAZY,cascade={CascadeType.ALL})
	public List<Resource> getChildren() {
		return children;
	}

	/**
	 * 设置子类资源
	 * 
	 * @param children 子类资源
	 */
	public void setChildren(List<Resource> children) {
		this.children = children;
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
	 * 获取资源类型
	 * @return String
	 */
	@Column(nullable=false,length=2)
	public String getType() {
		return type;
	}

	/**
	 * 设置资源类型
	 * @param type 类型
	 * @see ResourceType
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 获取该资源对应的组集合
	 * 
	 * @return List
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "TB_GROUP_RESOURCE", joinColumns = { @JoinColumn(name = "FK_RESOURCE_ID") }, inverseJoinColumns = { @JoinColumn(name = "FK_GROUP_ID") })
	public List<Group> getGroupsList() {
		return groupsList;
	}

	/**
	 * 设置该资源对应的组集合
	 * 
	 * @param groupsList 组集合
	 */
	public void setGroupsList(List<Group> groupsList) {
		this.groupsList = groupsList;
	}

	/**
	 * 获取父类名称
	 * 
	 * @return String 
	 */
	@Transient
	public String getParentName() {
		
		return this.parent == null ? "" : parent.getName();
	}
	
	/**
	 * 获取父类ID
	 * 
	 * @return String
	 */
	@Transient
	public String getParentId() {
		return this.parent == null ? "" : parent.getId();
	}

	/**
	 * 获取permission字符串
	 * 
	 * @return String 
	 */
	@Column(length=64)
	public String getPermission() {
		return permission;
	}

	/**
	 * 设置permission字符串
	 * @param permission 字符串
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * 获取资源图标
	 * 
	 * @return String
	 */
	@Column(length=32)
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置资源图标
	 * @param icon 图标css class
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * 获取资源类型的名称
	 * 
	 * @return String
	 */
	@Transient
	public String getTypeName() {
		return SystemVariableUtils.getName(ResourceType.class, this.type);
	}
	
}
