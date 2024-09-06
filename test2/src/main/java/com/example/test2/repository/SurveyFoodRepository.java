package com.example.test2.repository;

import com.example.test2.entity.SurveyFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyFoodRepository extends JpaRepository<SurveyFood, Integer> {
    List<SurveyFood> findByType(SurveyFood.FoodType type);

}
