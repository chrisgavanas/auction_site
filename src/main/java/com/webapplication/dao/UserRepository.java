package com.webapplication.dao;

import com.webapplication.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findUserByUsernameOrEmail(String username, String email);

    User findUserByUsername(String username);

    User findUserByUserId(String userId);

    User findUserByUserIdAndUsername(String userId, String username);

    Long countByEmail(String email);

    List<User> findUserByIsVerified(Boolean isVerified, Pageable pageable);

    Long countUserByIsVerified(Boolean isVerified);

}
