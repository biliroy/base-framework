package org.exitsoft.showcase.entity.foundation.variable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.exitsoft.showcase.entity.IdEntity;

/**
 * 字典类别实体
 * 
 * @author vincent
 *
 */
@Entity
@Table(name="TB_DICTIONARY_CATEGORY")
public class DictionaryCategory extends IdEntity{
	
	private static final long serialVersionUID = 1L;
	
	//名称
	private String name;
	//代码
	private String code;
	//备注
	private String remark;
	//拥有数据字典
	private List<DataDictionary> dataDictionariesList = new ArrayList<DataDictionary>();
	//拥有叶子节点
	private List<DictionaryCategory> children = new ArrayList<DictionaryCategory>();
	//对应父类
	private DictionaryCategory parent;
	
	public DictionaryCategory() {
		
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
	 * 
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取代码
	 * 
	 * @return String
	 */
	@Column(length=128,nullable=false,unique=true)
	public String getCode() {
		return code;
	}

	/**
	 * 设置代码
	 * 
	 * @param code 代码
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取拥有的数据字典集合
	 * 
	 * @return List
	 */
	@OneToMany(fetch=FetchType.LAZY,mappedBy="category",cascade={CascadeType.ALL})
	public List<DataDictionary> getDataDictionariesList() {
		return dataDictionariesList;
	}

	/**
	 * 设置拥有的数据字典集合
	 * 
	 * @param dataDictionariesList 数据字典集合
	 */
	public void setDataDictionariesList(List<DataDictionary> dataDictionariesList) {
		this.dataDictionariesList = dataDictionariesList;
	}

	/**
	 * 获取所有叶子节点
	 * 
	 * @return List
	 */
	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent",cascade={CascadeType.ALL})
	public List<DictionaryCategory> getChildren() {
		return children;
	}

	/**
	 * 设置叶子节点
	 * 
	 * @param children 叶子节点
	 */
	public void setChildren(List<DictionaryCategory> children) {
		this.children = children;
	}

	/**
	 * 获取父类节点
	 * 
	 * @return {@link DictionaryCategory}
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "FK_PARENT_ID")
	public DictionaryCategory getParent() {
		return parent;
	}

	/**
	 * 设置父类节点
	 * 
	 * @param parent 父类节点
	 */
	public void setParent(DictionaryCategory parent) {
		this.parent = parent;
	}
	
	/**
	 * 获取当前实体是否是为根节点,如果是返回ture，否则返回false
	 * 
	 * @return boolean
	 */
	@Transient
	public Boolean getLeaf() {
		return this.children != null && this.getChildren().size() > 0;
	}
	
	/**
	 * 获取父类名称
	 * 
	 * @return String
	 */
	@Transient
	public String getParentName() {
		return this.parent == null ? "" : this.parent.getName();
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
	
}
