package com.example.test2.controller;

import com.example.test2.entity.SurveyFood;
import com.example.test2.entity.Weather_data;
import com.example.test2.service.SurveyFoodService;
import com.example.test2.service.weatherAPIConnect;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/survey")
public class SurveyFoodController {
    @Autowired
    private SurveyFoodService service;
    @Autowired
    weatherAPIConnect weatherAPIConnect;

    @GetMapping("/foods")
    public String getRandomFoods(HttpSession session , Model model){
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);

        List<SurveyFood> randomFoodsKorea = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("한식"), 5);
        List<SurveyFood> randomFoodsChinese = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("중식"), 5);
        List<SurveyFood> randomFoodsJapan = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("일식"), 5);
        List<SurveyFood> randomFoodsWestern = service.getRadomFoodsByType(SurveyFood.FoodType.valueOf("양식"), 5);
        model.addAttribute("koreaFoods", randomFoodsKorea);
        model.addAttribute("chineseFoods", randomFoodsChinese);
        model.addAttribute("japanFoods", randomFoodsJapan);
        model.addAttribute("westernFoods", randomFoodsWestern);
        return "intensify/survey";
    }

}
