package org.exitsoft.orm.test.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="TB_ACCOUNT_ROLE")
public class Role extends UniversallyUniqueIdentifier{
	//角色名称
	private String name;
	//拥有菜单
	private List<Menu> menuList;
	
	/**
	 * 构造方法
	 */
	
	public Role() {
		
	}

	/**
	 * 获取角色名称
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置角色名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany
	@JoinTable(name = "TB_ACCOUNT_ROLE_MENU", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "MENU_ID") })
	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
}
