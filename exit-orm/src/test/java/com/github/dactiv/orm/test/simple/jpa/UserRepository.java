package com.github.dactiv.orm.test.simple.jpa;

import com.github.dactiv.orm.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, String>,JpaSpecificationExecutor<User>{

}
