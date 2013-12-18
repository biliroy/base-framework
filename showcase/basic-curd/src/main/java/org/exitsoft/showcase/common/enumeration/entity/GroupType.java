package org.exitsoft.showcase.common.enumeration.entity;

import org.exitsoft.showcase.common.enumeration.ValueEnum;

/**
 * 组类型
 * 
 * @author vincent
 *
 */
public enum GroupType implements ValueEnum<String>{

	/**
	 * 机构类型
	 */
	Organization("01","机构"),
	/**
	 * 部门类型 
	 */
	Department("02","部门"),
	/**
	 * 角色组类型
	 */
	RoleGorup("03","角色组");
	
	private GroupType(String value,String name) {
		this.value = value;
		this.name = name;
	}
	
	private String value;
	
	private String name;

	/**
	 * 获取类型值
	 * 
	 * @return String
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 获取类型名称
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}
}
