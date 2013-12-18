package com.github.dactiv.orm.test.simple.hibernate;

import com.github.dactiv.orm.core.hibernate.support.HibernateSupportDao;
import com.github.dactiv.orm.test.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateDaoSimple extends HibernateSupportDao<User, String>{

}
