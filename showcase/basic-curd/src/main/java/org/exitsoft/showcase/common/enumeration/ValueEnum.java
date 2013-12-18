package org.exitsoft.showcase.common.enumeration;

/**
 * 针对键为String值任意Object类型的枚举接口父类
 * 
 * @author vincent
 *
 * @param <V>
 */
public interface ValueEnum<V> {
	
	/**
	 * 获取值
	 * 
	 * @return Object
	 */
	public V getValue();

	/**
	 * 获取名称
	 * 
	 * @return String
	 */
	public String getName();
}

