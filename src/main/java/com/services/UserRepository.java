package com.services;

import com.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ericweidman on 2/3/17.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserName(String userName);
    User findByIsAdmin(Boolean isAdmin);
}
