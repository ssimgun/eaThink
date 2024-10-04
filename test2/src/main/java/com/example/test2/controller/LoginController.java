package com.example.test2.controller;

import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import com.example.test2.entity.Weather_data;
import com.example.test2.service.AddressService;
import com.example.test2.service.MenuRecommendationService;
import com.example.test2.service.UserService;
import com.example.test2.service.weatherAPIConnect;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/* EditController - Mapping List
 * 1. 로그인 버튼 클릭시 회원 정보 확인 : /login --> /main
 * 2. 회원 정보 로그아웃 : /logout
 */


@Slf4j
@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private weatherAPIConnect weatherAPIConnect;
    @Autowired
    private MenuRecommendationService menuRecommendationService;

    //   1. 로그인시 사용자 인증
    @PostMapping("/login")
    public String userlogin(@RequestParam String userId, @RequestParam String password, HttpServletRequest request, Model model) {
        // 사용자 인증 (DB의 값과 데이터 비교)
        Users users = userService.authenticate(userId, password);

        if (users != null) {
            // 로그인 정보 일치 session 생성
            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession();
            }
            //user 인증 선공 시 세션에 사용자 정보 저장
            session.setAttribute("loggedInUser", users);
            model.addAttribute("loggedInUser", users);
            //user 주소 정보 불러오기
            addressService.showAddressList(session, model);

            return "/main";
            // 로그인 정보 불일치 시 errorMessage 전송
        } else {
            model.addAttribute("error", "일치하는 로그인 정보가 없습니다.");
            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession();
            }
            Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);
            return "intensify/login";
        }

    }


    // 2. session 회원 정보 invalidate();
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main"; // 로그인 페이지로 리다렉트
    }

}
