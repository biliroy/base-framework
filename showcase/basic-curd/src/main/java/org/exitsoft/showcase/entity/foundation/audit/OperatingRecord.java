package org.exitsoft.showcase.entity.foundation.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.exitsoft.showcase.common.SystemVariableUtils;
import org.exitsoft.showcase.common.enumeration.entity.OperatingState;
import org.exitsoft.showcase.entity.IdEntity;

/**
 * 操作记录类，记录用户的操作信息
 * 
 * @author vincent
 *
 */
@Entity
@Table(name="TB_OPERATING_RECORD")
public class OperatingRecord extends IdEntity{
	
	private static final long serialVersionUID = 1L;
	
	//操作人名称
	private String username;
	//操作人
	private String fkUserId;
	//操作开始时间
	private Date startDate;
	//操作结束时间
	private Date endDate;
	//操作目标
	private String operatingTarget;
	//ip地址
	private String ip;
	//操作的java方法
	private String method;
	//执行状态,1代表成，2代表执行时出现异常
	private Integer state;
	//模块名称
	private String module;
	//功能名称
	private String function;
	//描述
	private String remark;
	
	/**
	 * 构造方法
	 */
	public OperatingRecord() {
		
	}
	
	/**
	 * 获取操作人名称
	 * 
	 * @return String
	 */
	@Column(length=32)
	public String getUsername() {
		return username;
	}

	/**
	 * 设置操作人名称
	 * 
	 * @param username 操作人名称
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取操作人主键ID
	 * 
	 * @return String
	 */
	@Column(length=32)
	public String getFkUserId() {
		return fkUserId;
	}
	
	/**
	 * 设置操作人主键ID
	 * 
	 * @param fkUserId 操作人主键ID
	 */
	public void setFkUserId(String fkUserId) {
		this.fkUserId = fkUserId;
	}

	/**
	 * 获取操作开始时间
	 * 
	 * @return Date
	 */
	@Column(nullable=false)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * 设置操作开始时间
	 * 
	 * @param startDate 操作开始时间
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * 获取操作结束时间
	 * 
	 * @return Date
	 */
	@Column(nullable=false)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设置操作结束时间
	 * 
	 * @param endDate 操作结束时间
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取操作目标
	 * 
	 * @return String
	 */
	@Column(length=512,nullable=false)
	public String getOperatingTarget() {
		return operatingTarget;
	}

	/**
	 * 设置操作目标
	 * 
	 * @param operatingTarget 操作目标
	 */
	public void setOperatingTarget(String operatingTarget) {
		this.operatingTarget = operatingTarget;
	}

	/**
	 * 获取id地址
	 * 
	 * @return String
	 */
	@Column(length=64,nullable=false)
	public String getIp() {
		return ip;
	}

	/**
	 * 设置id地址
	 * 
	 * @param ip id地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 获取操作的java方法
	 * 
	 * @return String
	 */
	@Column(length=256,nullable=false)
	public String getMethod() {
		return method;
	}

	/**
	 * 设置操作的java方法
	 * 
	 * @param method java方法名称
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 获取执行状态,1代表成，2代表执行时出现异常
	 * 
	 * @return Integer
	 */
	@Column(nullable=false)
	public Integer getState() {
		return state;
	}

	/**
	 * 设置执行状态
	 * @param state 1代表成，2代表执行时出现异常
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	
	/**
	 * 获取模块名称
	 * 
	 * @return String
	 */
	@Column(length=128)
	public String getModule() {
		return module;
	}

	/**
	 * 设置模块名称
	 * 
	 * @param module 模块名称
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * 获取功能名称
	 * 
	 * @return String
	 */
	@Column(length=128)
	public String getFunction() {
		return function;
	}

	/**
	 * 设置功能名称
	 * 
	 * @param function 功能名称
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	/**
	 * 获取描述
	 * 
	 * @return String
	 */
	@Lob
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置描述
	 * 
	 * @param remark 描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 获取状态的中文名称
	 * 
	 * @return String
	 */
	@Transient
	public String getStateName() {
		return SystemVariableUtils.getName(OperatingState.class, this.state);
	}
	
}
