package com.example.test2.controller;

import com.example.test2.dto.UserFoodPreferenceForm;
import com.example.test2.entity.*;
import com.example.test2.repository.SurveyFoodRepository;
import com.example.test2.repository.UserFoodPreferenceRepository;
import com.example.test2.service.AddressService;
import com.example.test2.service.SurveyFoodService;
import com.example.test2.service.weatherAPIConnect;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/survey")
public class SurveyFoodController {
    @Autowired
    private SurveyFoodService service;
    @Autowired
    private SurveyFoodRepository surveyFoodRepository;
    @Autowired
    weatherAPIConnect weatherAPIConnect;
    @Autowired
    UserFoodPreferenceRepository userFoodPreferenceRepository;
    @Autowired
    AddressService addressService;

    // 나의 음식 취향 선택 시 간편 설문 || 전체 설문 선택
    @GetMapping("/surveySelect")
    public String surveySelect(HttpSession session, Model model) {
        //날씨 정보 가져오기
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        Address address = (Address) session.getAttribute("selectAddress");

        //사용자 session 에서 유저 아이디 가져오기
        model.addAttribute("loggedInUser", loggedInUser);

        if(loggedInUser != null){
            addressService.showAddressList(session, model);
            model.addAttribute("selectAddress", address);
            return "intensify/selectsurvey";
        }else {
            log.info("session이 널 입니다.");
            model.addAttribute("error", "로그인 후 사용 가능한 서비스입니다.");
            return "redirect:/main";
        }

    }

    // 24개 랜덤 설문
    @GetMapping("/randomSurvey")
    public String getRandomFoods(HttpSession session, Model model) {
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        Address address = (Address) session.getAttribute("selectAddress");

        //사용자 session 에서 유저 아이디 가져오기
        model.addAttribute("loggedInUser", loggedInUser);

        if(loggedInUser != null){
            addressService.showAddressList(session, model);
            model.addAttribute("selectAddress", address);
        }else {
            log.info("session이 널 입니다.");
            return "redirect:/main";
        }

        //사용자 session 에서 유저 아이디 가져오기
        Integer userId = loggedInUser.getId();

        log.info("현재 로그인된 유저 아이디(번호) : " + loggedInUser.getId());
        // 사용자의 음식 선호도를 조회
        List<UserFoodPreferences> preferences = service.findById_UserId(userId);
        log.info("사용자 음식 점수 리스트 : " + preferences.toString());

        //Map으로 변환 : foodId를 키로, preference를 값으로
        Map<Integer, Integer> preferenceMap = preferences.stream()
                .collect(Collectors.toMap(preference -> preference.getId().getFoodId(),
                        UserFoodPreferences::getPreference));

        log.info("preferenceMap : " + preferenceMap);
        model.addAttribute("preferenceMap", preferenceMap);


        // 카테고리별 음식 랜점 추출
        List<SurveyFood> randomFoodsWestern = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("양식"), 4);
        List<SurveyFood> randomFoodsCafe = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("카페_디저트"), 4);
        List<SurveyFood> randomFoodsKorea = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("한식"), 4);
        List<SurveyFood> randomFoodsMeat = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("고기_구이"), 4);
        List<SurveyFood> randomFoodsWorlds = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("일식_중식_세계음식"), 4);
        List<SurveyFood> randomFoodsNight = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("나이트라이프"), 4);

        // 모든 음식 리스트 통합
        List<SurveyFood> allRandomFoods = new ArrayList<>();
        allRandomFoods.addAll(randomFoodsWestern);
        allRandomFoods.addAll(randomFoodsCafe);
        allRandomFoods.addAll(randomFoodsKorea);
        allRandomFoods.addAll(randomFoodsMeat);
        allRandomFoods.addAll(randomFoodsWorlds);
        allRandomFoods.addAll(randomFoodsNight);

        //카테고리별 음식 리스트에 선호도 점수를 매핑
        service.mapPreferences(allRandomFoods, preferenceMap);

        //모델에 추가
        model.addAttribute("Western", randomFoodsWestern);
        model.addAttribute("Cafe", randomFoodsCafe);
        model.addAttribute("Korea", randomFoodsKorea);
        model.addAttribute("Meat", randomFoodsMeat);
        model.addAttribute("Worlds", randomFoodsWorlds);
        model.addAttribute("Night", randomFoodsNight);
//        model.addAttribute("allFoods", allRandomFoods);
        return "intensify/randomsurvey";
    }

    // 전체 음식 설문
    @GetMapping("/allSurvey")
    public String allRandomFoods(HttpSession session, Model model) {
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        Address address = (Address) session.getAttribute("selectAddress");

        //사용자 session 에서 유저 아이디 가져오기
        model.addAttribute("loggedInUser", loggedInUser);

        if(loggedInUser != null){
            addressService.showAddressList(session, model);
            model.addAttribute("selectAddress", address);
        }else {
            log.info("session이 널 입니다.");
            return "redirect:/main";
        }

        Integer userId = loggedInUser.getId();

        log.info("현재 로그인된 유저 아이디(번호) : " + loggedInUser.getId());
        // 사용자의 음식 선호도를 조회
        List<UserFoodPreferences> preferences = service.findById_UserId(userId);
        log.info("사용자 음식 점수 리스트 : " + preferences.toString());

        //Map으로 변환 : foodId를 키로, preference를 값으로
        Map<Integer, Integer> preferenceMap = preferences.stream()
                .collect(Collectors.toMap(preference -> preference.getId().getFoodId(),
                        UserFoodPreferences::getPreference));

        log.info("preferenceMap : " + preferenceMap);
        model.addAttribute("preferenceMap", preferenceMap);


        // 카테고리별 음식 랜점 추출
        List<SurveyFood> allFoods = surveyFoodRepository.findAll();

        //카테고리별 음식 리스트에 선호도 점수를 매핑
        service.mapPreferences(allFoods, preferenceMap);

        //모델에 추가
        model.addAttribute("allfoods", allFoods);

//        model.addAttribute("allFoods", allRandomFoods);
        return "intensify/allsurvey";
    }


    // 사용자 설문 값 저장
    @PostMapping("/submit")
    public String submitSurvey(@RequestParam Map<String, String> preferences, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        Integer userId = user.getId();

        log.info("웹에서 받아오는 값 " + preferences.toString());
        Map<Integer, Integer> preferencesMap = new HashMap<>();
        for (Map.Entry<String, String> entry : preferences.entrySet()) {
            log.info("key 값의 타입 : " + entry.getKey());
            log.info("key 값의 타입 : " + entry.getValue());
            Integer foodId = Integer.parseInt(entry.getKey());
            Integer preferenceValue = Integer.parseInt(entry.getValue());

            preferencesMap.put(foodId, preferenceValue);

//            UserFoodPreferencedId userFoodPreferencedId = new UserFoodPreferencedId(userId, foodId);
//            UserFoodPreferences userFoodPreferences = new UserFoodPreferences(userFoodPreferencedId, new Users(userId), new SurveyFood(foodId), preferenceValue);

        }
        service.savePreference(userId, preferencesMap);

        return "redirect:/home";
    }

}
