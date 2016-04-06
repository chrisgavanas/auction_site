package com.webapplication.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webapplication.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	User findUserByUsernameOrEmail(String username, String email);
    User findUserByPasswordAndUsernameOrEmail(String password, String username, String email);
}
