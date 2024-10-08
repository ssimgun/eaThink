package com.example.test2.controller;

import com.example.test2.entity.*;
import com.example.test2.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/*
 * RestaurantRestController - Mapping List : 음식점 목록
 * @RequestMapping("/survey")
 * 1. 24개 랜덤 설문 : /randomSurvey
 * 2. 전체 음식 설문 : /allSurvey
 * 3. 사용자 설문 값 저장 /submit
 * 4. 음식 태그 추천 : /recommendation
 */

@Slf4j
@Controller
@RequestMapping("/survey")
public class SurveyFoodController {
    @Autowired
    private SurveyFoodService service;
    @Autowired
    AddressService addressService;
    @Autowired
    private MenuRecommendationService menuRecommendationService;
    @Autowired
    private UserService userService;

    // 1. 24개 랜덤 설문 2. 전체음식 설문
    @GetMapping("/{surveyType}Survey")
    public String getRandomFoods(@PathVariable String surveyType, HttpSession session, Model model) {
        // 유저 정보 불러 오기
        userService.setUserSessionInfo(session, model);
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/main";
        }

        addressService.showAddressList(session, model);

        // 사용자의 음식 선호도를 조회
        List<UserFoodPreferences> preferences = service.findById_UserId(loggedInUser.getId());

        //Map으로 변환 : <foodId : preference>
        Map<Integer, Integer> preferenceMap = preferences.stream()
                .collect(Collectors.toMap(preference -> preference.getId().getFoodId(),
                        UserFoodPreferences::getPreference));
        model.addAttribute("preferenceMap", preferenceMap);

        // 카테고리별 음식 랜덤 추출
        String[] foodTypes = {"양식", "카페_디저트", "한식", "고기_구이", "일식_중식_세계음식", "나이트라이프", "요리수준"};
        Map<String, List<SurveyFood>> foodsMap = new HashMap<>();

        for (String foodType : foodTypes) {
            List<SurveyFood> foods;

            if ("random".equals(surveyType)) {
                foods = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf(foodType), 4);
            } else{
                foods = service.findByType(SurveyFood.FoodType.valueOf(foodType));
            }

            foodsMap.put(foodType, foods);
        }

        // 카테고리별 음식 리스트에 선호도 점수 매핑
        List<SurveyFood> allRandomFoods = foodsMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        service.mapPreferences(allRandomFoods, preferenceMap);

        foodsMap.forEach(model::addAttribute);

        List<SurveyFood> cookinglevel = foodsMap.get("요리수준");
        if(cookinglevel != null && !cookinglevel.isEmpty()) {
            // 요리 수준 값만 추출
            List<Integer> cookinglevelPreferences = cookinglevel.stream()
                    .map(SurveyFood::getPreference).toList();
            // 요리수준 값 가져오기
            Integer userCookingPreference = cookinglevelPreferences.get(cookinglevelPreferences.size() -1);

            if(userCookingPreference.equals(1)){
                model.addAttribute("level1", userCookingPreference);
            } else if (userCookingPreference.equals(2)) {
                model.addAttribute("level2", userCookingPreference);
            } else if (userCookingPreference.equals(3)) {
                model.addAttribute("level3", userCookingPreference);
            } else if (userCookingPreference.equals(4)) {
                model.addAttribute("level4", userCookingPreference);
            }
        }

        if ("random".equals(surveyType)) {
            return "intensify/randomsurvey";
        } else {
            return "intensify/allsurvey";
        }

    }

    // 3. 사용자 설문 값 저장
    @PostMapping("/submit")
    public String submitSurvey(@RequestParam Map<String, String> preferences, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        Integer userId = user.getId();

        Map<Integer, Integer> preferencesMap = new HashMap<>();
        for (Map.Entry<String, String> entry : preferences.entrySet()) {
            Integer foodId = Integer.parseInt(entry.getKey());
            Integer preferenceValue = Integer.parseInt(entry.getValue());

            preferencesMap.put(foodId, preferenceValue);

        }
        service.savePreference(userId, preferencesMap);

        return "redirect:/main";
    }

    // 4. main 화면 음식 출력
    @ResponseBody
    @GetMapping("/recommendation")
    public SurveyFood getRecommendation(HttpSession session) {
        // 랜덤 음식 추천
        List<SurveyFood> personalFoodScore = menuRecommendationService.getMenuRecommendationScore(session);
        List<SurveyFood> finalFoodScore = menuRecommendationService.getScoreByweather(session, personalFoodScore);
        List<SurveyFood> randomizedList = menuRecommendationService.randomTop(finalFoodScore);
        SurveyFood recommendationFood = menuRecommendationService.getRecommendation(randomizedList);

        return recommendationFood;
    }

}
