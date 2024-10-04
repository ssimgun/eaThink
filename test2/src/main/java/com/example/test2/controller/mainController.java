package com.example.test2.controller;

import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import com.example.test2.entity.Weather_data;
import com.example.test2.repository.AddressRepository;
import com.example.test2.repository.UserRepository;
import com.example.test2.service.AddressService;
import com.example.test2.service.MenuRecommendationService;
import com.example.test2.service.UserService;
import com.example.test2.service.weatherAPIConnect;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*
 * MainController - Mapping List : 페이지이동
 * 1. 매인 페이지 : /main
 * 2. 로그인 페이지 : /loginpage
 * 3. 회원가입 페이지 이동 : /register
 * 4. 설문조사 선택 페이지로 이동 : /surveySelect
 */


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
    @Autowired
    private AddressService addressService;


    // 1. 메인 페이지 이동
    @RequestMapping("/main")
    public String mainLogin(HttpSession session, Model model) {
        //회원 정보, 주소, 날씨 데이터 호출
        userService.setUserSessionInfo(session, model);
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        Address address = (Address) session.getAttribute("selectAddress");


        if (loggedInUser != null) {
            addressService.showAddressList(session, model);
        }
        return "intensify/maintest";
    }

    // 2. 로그인 페이지로 이동
    @GetMapping("/loginpage")
    public String login(HttpSession session, Model model) {
        userService.setUserSessionInfo(session, model);

        // 사용자 인증
        return "intensify/login";
    }

    // 3. 회원가입 페이지로 이동
    @GetMapping("/register")
    public String registerpage(HttpSession session, Model model) {
        userService.setUserSessionInfo(session, model);
        return "intensify/register";
    }

    // 4. 설문조사 선택 페이지로 이동
    @GetMapping("/surveySelect")
    public String surveySelect(HttpSession session, Model model) {
        userService.setUserSessionInfo(session, model);
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");


        if (loggedInUser != null) {
            addressService.showAddressList(session, model);
            return "intensify/selectsurvey";
        } else {
            model.addAttribute("error", "로그인 후 사용 가능한 서비스입니다.");
            return "redirect:/main";
        }
    }
}
