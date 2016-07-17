package com.webapplication.dao;

import com.webapplication.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    User findUserByUsernameOrEmail(String username, String email);

    User findUserByUserId(Integer id);

    Long countByEmail(String email);

    @Query("select u from User u where (u.username = ?1 or u.email= ?2) and u.password = ?3")
    User findUserByUsernameOrEmailAndPassword(String username, String email, String password);

    List<User> findUserByIsVerified(Boolean isVerified, Pageable pageable);
}
