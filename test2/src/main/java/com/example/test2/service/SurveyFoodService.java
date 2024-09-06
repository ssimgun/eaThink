package com.example.test2.service;

import com.example.test2.entity.SurveyFood;
import com.example.test2.repository.SurveyFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SurveyFoodService {
    @Autowired
    private SurveyFoodRepository repository;

    // 특정 Foodtype의 음식 조회
    public List<SurveyFood> findByType(SurveyFood.FoodType type){
        return repository.findByType(type);
    }

    // 특정 FoodType의 랜덤 음식 n개 선택
    public List<SurveyFood> getRadomFoodsByType(SurveyFood.FoodType type, int count){
        List<SurveyFood> foodsByType = findByType(type);
        Collections.shuffle(foodsByType);
        return foodsByType.stream().limit(count).toList();
    }

}
