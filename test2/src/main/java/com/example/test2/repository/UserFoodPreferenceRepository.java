package com.example.test2.repository;

import com.example.test2.entity.UserFoodPreferencedId;
import com.example.test2.entity.UserFoodPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFoodPreferenceRepository extends JpaRepository<UserFoodPreferences, UserFoodPreferencedId> {

    List<UserFoodPreferences> findById_UserId(Integer userId);
}
