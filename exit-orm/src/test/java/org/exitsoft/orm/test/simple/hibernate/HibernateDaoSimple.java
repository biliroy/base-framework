package org.exitsoft.orm.test.simple.hibernate;

import org.exitsoft.orm.core.hibernate.support.HibernateSupportDao;
import org.exitsoft.orm.test.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateDaoSimple extends HibernateSupportDao<User, String>{

}
