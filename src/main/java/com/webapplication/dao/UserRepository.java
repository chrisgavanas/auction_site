package com.webapplication.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webapplication.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findUserByUsernameOrEmail(String username, String email);
    User findUserByUserId(int id);
    @Query("select u from User u where (u.username = ?1 or u.email= ?2) and u.password = ?3")
    User findUserByUsernameOrEmailAndPassword(String username, String email, String password);
}
