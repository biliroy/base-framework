package org.exitsoft.showcase.entity.foundation.variable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.exitsoft.common.type.FieldType;
import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.showcase.entity.IdEntity;


/**
 * 数据字典实体
 * 
 * @author vincent
 *
 */
@Entity
@Table(name="TB_DATA_DICTIONARY")
public class DataDictionary extends IdEntity{
	
	private static final long serialVersionUID = 1L;
	
	//名称
	private String name;
	//值
	private String value;
	//类型
	private String type = "S";
	//备注
	private String remark;
	//所属类别
	public DictionaryCategory category;
	
	public DataDictionary() {
		
	}
	
	public DataDictionary(String name, String value, String type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}

	/**
	 * 获取名称
	 * 
	 * @return String
	 */
	@Column(length=256,nullable=false)
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取值
	 * 
	 * @return String
	 */
	@Column(length=32,nullable=false)
	public String getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value 值
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取值类型
	 * 
	 * @return String
	 */
	@Column(length=1,nullable=false)
	public String getType() {
		return type;
	}

	/**
	 * 设置值类型
	 * 
	 * @param type 值类型
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
	 * 获取所属类别
	 * 
	 * @return {@link DictionaryCategory}
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "FK_CATEGORY_ID", nullable=false)
	public DictionaryCategory getCategory() {
		return category;
	}

	/**
	 * 设置所属类别
	 * 
	 * @param category 所属类别
	 */
	public void setCategory(DictionaryCategory category) {
		this.category = category;
	}
	
	/**
	 * 根据type属性的值获取真正的值
	 * 
	 * @return Object
	 */
	@Transient
	public Object getReadValue() {
		return ConvertUtils.convertToObject(this.value, FieldType.valueOf(type).getValue());
	}
}
