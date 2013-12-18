package org.exitsoft.common.test.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

/**
 * UUID主键父类
 * 
 * @author vincent
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings("serial")
public class UniversallyUniqueIdentifier implements Serializable{
	
	protected String id;
	
	/**
	 * 获取主键ID
	 * 
	 * @return String
	 */
	public String getId() {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return this.id;
	}

	/**
	 * 设置主键ID，
	 * @param id
	 */
	public void setId(String id) {
		if (StringUtils.isEmpty(id)) {
			this.id = null;
		} else {
			this.id = id;
		}
		
	}

}
