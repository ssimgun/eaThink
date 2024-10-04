package com.example.test2.service;

import com.example.test2.entity.SurveyFood;
import com.example.test2.entity.UserFoodPreferencedId;
import com.example.test2.entity.UserFoodPreferences;
import com.example.test2.entity.Users;
import com.example.test2.repository.SurveyFoodRepository;
import com.example.test2.repository.UserFoodPreferenceRepository;
import com.example.test2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
* SurveyFoodService : 음식 설문 관련 기능
* 1. 특정 Foodtype의 음식 조회 : findByType
* 2. 특정 FoodType의 랜덤 음식 n개  : getRadomFoodsByType
* 3. 사용자 설문값 저장하기 : savePreference
* 4. 유저 설문값 불러오기 : findById_UserId
* 5. 사용자 점수 값을 음식 목록 출력시 value에 출력 메소드 mapPreferences
*/

@Service
public class SurveyFoodService {
    @Autowired
    private SurveyFoodRepository repository;
    @Autowired
    private UserFoodPreferenceRepository userFoodPreferenceRepository;
    @Autowired
    private UserRepository userRepository;

    // 1. 특정 Foodtype의 음식 조회 : findByType
    public List<SurveyFood> findByType(SurveyFood.FoodType type) {
        return repository.findByType(type);
    }

    // 2. 특정 FoodType의 랜덤 음식 n개  : getRadomFoodsByType
    public List<SurveyFood> getRadomFoodsByType(SurveyFood.FoodType type, int count) {
        List<SurveyFood> foodsByType = findByType(type);
        Collections.shuffle(foodsByType);
        return foodsByType.stream().limit(count).toList();
    }

    // 3. 사용자 설문값 저장하기 : savePreference
    public void savePreference(Integer userId, Map<Integer, Integer> preferenceMap) {
        // 사용자 엔티티 조회
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 아이디 찾지 못함 : " + userId));

        // 저장할 UserFoodPreferences 리스트 생성
        for (Map.Entry<Integer, Integer> entry : preferenceMap.entrySet()) {
            //SurveyFood 엔티티 조회(음식 ID로 조회)
            SurveyFood surveyFood = repository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("음식 정보 찾지 못함 : " + entry.getKey()));

//            복합키 생성 : entry.getKey() = food_id
            UserFoodPreferencedId id = new UserFoodPreferencedId();
            id.setUserId(user.getId());
            id.setFoodId(entry.getKey());

            //유저점수 엔티티생성
            UserFoodPreferences userFoodPreferences = new UserFoodPreferences();
            userFoodPreferences.setId(id);
            userFoodPreferences.setUser(user);
            userFoodPreferences.setSurveyFood(surveyFood);
            userFoodPreferences.setPreference(entry.getValue());

            userFoodPreferenceRepository.save(userFoodPreferences);
        }

    }

    // 4. 유저 설문값 불러오기 : findById_UserId
    public List<UserFoodPreferences> findById_UserId(Integer userId) {
        return userFoodPreferenceRepository.findById_UserId(userId);
    }

    // 5. 사용자 점수 값을 음식 목록 출력시 value에 출력 메소드 mapPreferences
    public void mapPreferences(List<SurveyFood> foods, Map<Integer, Integer> preferenceMap) {
        foods.forEach(food -> {
            Integer foodId = food.getFood_id();
            //Map에 있는 사용자 지정 값을 가져와서 food에 설정 없으면 기본 값 5)
            food.setPreference(preferenceMap.getOrDefault(foodId, 5));
        });
    }


}
