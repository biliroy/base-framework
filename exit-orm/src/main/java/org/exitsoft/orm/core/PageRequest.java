/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exitsoft.orm.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


/**
 * 分页参数封装类.
 * @author vincent
 */
public class PageRequest implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 第几页
	 */
	protected int pageNo = 1;
	
	/**
	 * 每页大小
	 */
	protected int pageSize = 10;

	/**
	 * 排序字段
	 */
	protected String orderBy = null;
	
	/**
	 * 排序方式
	 * @see Sort
	 */
	protected String orderDir = null;

	/**
	 * 是否默认计算总记录数.
	 */
	protected boolean countTotal = true;

	/**
	 * 构造方法
	 */
	public PageRequest() {
	}

	/**
	 * 构造方法
	 * @param pageNo 第几页
	 * @param pageSize 每页分页大小
	 */
	public PageRequest(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	/**
	 * 获得当前页的页号, 序号从1开始, 默认为1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页的页号, 序号从1开始, 低于1时自动调整为1.
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * 获得每页的记录数量, 默认为10.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量, 低于1时自动调整为1.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = 1;
		}
	}

	/**
	 * 获得排序字段, 无默认值. 多个排序字段时用','分隔.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段, 多个排序字段时用','分隔.
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 获得排序方向, 无默认值.
	 */
	public String getOrderDir() {
		return orderDir;
	}

	/**
	 * 设置排序方式向.
	 * 
	 * @param orderDir 可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public void setOrderDir(final String orderDir) {
		String lowcaseOrderDir = StringUtils.lowerCase(orderDir);

		//检查order字符串的合法值
		String[] orderDirs = StringUtils.split(lowcaseOrderDir, ',');
		
		if (ArrayUtils.isEmpty(orderDirs)) {
			return ;
		}
		
		for (String orderDirStr : orderDirs) {
			if (!StringUtils.equals(Sort.DESC, orderDirStr) && !StringUtils.equals(Sort.ASC, orderDirStr)) {
				throw new IllegalArgumentException("排序方向" + orderDirStr + "不是合法值");
			}
		}

		this.orderDir = lowcaseOrderDir;
	}

	/**
	 * 获得排序参数.
	 */
	@SuppressWarnings("unchecked")
	public List<Sort> getSort() {
		if (orderBy == null || orderDir == null) {
			return Collections.EMPTY_LIST;
		}
		String[] orderBys = StringUtils.split(orderBy, ',');
		String[] orderDirs = StringUtils.split(orderDir, ',');
		Assert.isTrue(orderBys.length == orderDirs.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

		List<Sort> orders = new ArrayList<PageRequest.Sort>();
		for (int i = 0; i < orderBys.length; i++) {
			orders.add(new Sort(orderBys[i], orderDirs[i]));
		}

		return orders;
	}

	/**
	 * 是否已设置排序字段,无默认值.
	 */
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(orderDir));
	}

	/**
	 * 是否默认计算总记录数.
	 */
	public boolean isCountTotal() {
		return countTotal;
	}

	/**
	 * 设置是否默认计算总记录数.
	 */
	public void setCountTotal(boolean countTotal) {
		this.countTotal = countTotal;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置, 序号从0开始.
	 */
	public int getOffset() {
		return ((pageNo - 1) * pageSize);
	}
	
	/**
	 * 获取Order By后的sql字符串
	 * @return String
	 */
	public String getOrderSortString() {
		List<Sort> list = this.getSort();
		StringBuffer buffer = new StringBuffer();
		for (Iterator<Sort> it = list.iterator(); it.hasNext();) {
			Sort sort = it.next();
			buffer.append(sort.property + " " + sort.dir).append(",");
		}
		
		return StringUtils.substringBeforeLast(buffer.toString(), ",");
	}

	/**
	 * 排序方式
	 * 
	 * @author vincent
	 *
	 */
	public static class Sort {
		public static final String ASC = "asc";
		public static final String DESC = "desc";

		private final String property;
		private final String dir;
		
		/**
		 * 构造方法
		 * @param property 要排序的字段
		 * @param dir 排序方式
		 */
		public Sort(String property, String dir) {
			this.property = property;
			this.dir = dir;
		}
		
		/**
		 * 获取排序字段
		 * @return String
		 */
		public String getProperty() {
			return property;
		}

		/**
		 * 获取排序方式
		 * @return
		 */
		public String getDir() {
			return dir;
		}
	}
}
