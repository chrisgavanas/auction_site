package com.webapplication.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webapplication.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findUserByUsernameAndPassword(String username, String password);
    User findUserByEmailAndPassword(String email, String password);
    User findUserByUsernameOrEmail(String username, String email);
}
