package org.exitsoft.orm.test.simple.jpa;

import org.exitsoft.orm.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, String>,JpaSpecificationExecutor<User>{

}
