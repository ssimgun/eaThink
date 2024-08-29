package com.example.test2.repository;

import org.springframework.stereotype.Repository;
import com.example.test2.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>{

    Users findByUserIdAndPassword(String userId, String password);

    Users findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByNickname(String nickname);

}
