package com.example.test2.controller;

import com.example.test2.entity.SurveyFood;
import com.example.test2.entity.Weather_data;
import com.example.test2.repository.AddressRepository;
import com.example.test2.repository.UserRepository;
import com.example.test2.service.MenuRecommendationService;
import com.example.test2.service.UserService;
import com.example.test2.service.weatherAPIConnect;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
public class mainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private weatherAPIConnect weatherAPIConnect;
    @Autowired
    private MenuRecommendationService menuRecommendationService;

//    메인 화면 이동
    @GetMapping("/main")
    public String main(HttpSession session, Model model){
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);
//        session.setAttribute("weather", weather_data);
//        model.addAttribute("weather", weather_data);

        return "intensify/maintest";
    }

    @GetMapping("/maptest")
    public String maptest(HttpSession session, Model model){
        return "maptest";
    }

//    로그인 페이지로 이동
    @GetMapping("/loginpage")
    public String login(HttpSession session, Model model){
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);

        // 사용자 인증
        return "intensify/login";
    }

//    회원가입 페이지로 이동
    @GetMapping("/register")
    public String registerpage(HttpSession session , Model model){
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);
        return "intensify/register";
    }

}
