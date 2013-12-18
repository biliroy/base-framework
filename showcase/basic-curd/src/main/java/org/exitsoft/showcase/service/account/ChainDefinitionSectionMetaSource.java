package org.exitsoft.showcase.service.account;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.exitsoft.showcase.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.entity.account.Group;
import org.exitsoft.showcase.entity.account.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 借助spring {@link FactoryBean} 对apache shiro的premission进行动态创建
 * 
 * @author vincent
 *
 */
public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section>{

	@Autowired
	private AccountManager accountManager;
	
	//shiro默认的链接定义
	private String filterChainDefinitions;
	
	/**
	 * 通过filterChainDefinitions对默认的链接过滤定义
	 * 
	 * @param filterChainDefinitions 默认的接过滤定义
	 */
	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}
	
	@Override
	public Section getObject() throws BeansException {
		Ini ini = new Ini();
        //加载默认的url
        ini.load(filterChainDefinitions);
        Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        //循环数据库资源的url
        for (Resource resource : accountManager.getResources()) {
        	if(StringUtils.isNotEmpty(resource.getValue()) && StringUtils.isNotEmpty(resource.getPermission())) {
        		section.put(resource.getValue(), resource.getPermission());
        	}
        }
        
        //循环数据库组的url
        for (Group group : accountManager.getGroup(GroupType.RoleGorup)) {
        	if(StringUtils.isNotEmpty(group.getValue()) && StringUtils.isNotEmpty(group.getRole())) {
        		section.put(group.getValue(), group.getRole());
        	}
        }
        
        return section;
	}
	
	@Override
	public Class<?> getObjectType() {
		return Section.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}

	
}
