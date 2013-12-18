package org.exitsoft.orm.test.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="TB_ACCOUNT_MENU")
@NamedQueries({
	@NamedQuery(name="QueryUserMenuJpa",query="select ml from " +
			"User u left join u.roleList rl " +
			"left join rl.menuList ml " +
			"where u.id=?1 and u.state=?2"),
	@NamedQuery(name="QueryUserMenu",query="select ml from " +
			"User u left join u.roleList rl " +
			"left join rl.menuList ml " +
			"where u.id=? and u.state=?")
})
public class Menu extends UniversallyUniqueIdentifier{
	//名称
	private String name;
	//所属父类
	private Menu parent;
	//拥有子菜单
	private List<Menu> childerList;
	
	private int type;
	
	/**
	 * 构造方法
	 */
	public Menu() {
		
	}

	/**
	 * 获取菜单名称
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置菜单名称
	 * @param name 菜单名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取父类菜单
	 * @return {@link Menu}
	 */
	@ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
	public Menu getParent() {
		return parent;
	}

	/**
	 * 设置父类菜单
	 * @param parent 父类菜单
	 */
	public void setParent(Menu parent) {
		this.parent = parent;
	}

	/**
	 * 获取子菜单
	 * @return List
	 */
	@OneToMany(mappedBy = "parent",cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	public List<Menu> getChilderList() {
		return childerList;
	}

	/**
	 * 设置子菜单
	 * @param childerList 子菜单
	 */
	public void setChilderList(List<Menu> childerList) {
		this.childerList = childerList;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
