package org.exitsoft.orm.test.simple.hibernate;

import org.exitsoft.orm.core.hibernate.support.BasicHibernateDao;
import org.exitsoft.orm.test.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BasicHiberanteDaoSimple extends BasicHibernateDao<User, String>{

}
