package com.github.dactiv.showcase.dao.foundation.variable;

import com.github.dactiv.orm.core.hibernate.support.HibernateSupportDao;
import com.github.dactiv.showcase.entity.foundation.variable.DictionaryCategory;
import org.springframework.stereotype.Repository;

/**
 * 字典类别数据访问
 * 
 * @author vincent
 *
 */
@Repository
public class DictionaryCategoryDao extends HibernateSupportDao<DictionaryCategory, String>{

}
