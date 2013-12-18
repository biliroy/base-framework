package com.github.dactiv.orm.test.simple.hibernate;

import com.github.dactiv.orm.core.hibernate.support.BasicHibernateDao;
import com.github.dactiv.orm.test.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BasicHiberanteDaoSimple extends BasicHibernateDao<User, String>{

}
