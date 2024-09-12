package com.example.test2.controller;

import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import com.example.test2.entity.Weather_data;
import com.example.test2.service.AddressService;
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

@Slf4j
@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private weatherAPIConnect weatherAPIConnect;

    @PostMapping("/login")
    public String userlogin(@RequestParam String userId, @RequestParam String password, HttpServletRequest request, Model model){
        // 사용자 인증
        Users users = userService.authenticate(userId, password);

        if(users != null){
            HttpSession session = request.getSession(false);
            if(session == null){
                log.info("새 세션 생성");
                session = request.getSession();
            }
            //user 인증 선공 시 세션에 사용자 정보 저장
            session.setAttribute("loggedInUser", users);
            log.info(session.getAttribute("loggedInUser").toString());

            model.addAttribute("loggedInUser", users);

            return "/home";
        }else{
            model.addAttribute("error", "일치하는 로그인 정보가 없습니다.");
            return "redirect:/login?error";

        }

    }

//        로그인 시 메인 화면 이동
    @RequestMapping("/home")
    public String home(HttpSession session, Model model){
        log.info("session check : {}",session.getAttribute("loggedInUser"));
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        Address address = (Address) session.getAttribute("selectAddress");

        // 날시 데이터 가져오기
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);
//        model.addAttribute("weather",weather_data);

        if(loggedInUser != null){
            log.info("home : {}", loggedInUser.toString());
            addressService.showAddressList(session, model);
            model.addAttribute("selectAddress", address);
            log.info("address : {}", model.getAttribute("address"));
            return "intensify/maintest";
        }else {
            log.info("session이 널 입니다.");
            return "redirect:/main";
        }
    }

    @GetMapping ("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/main"; // 로그인 페이지로 리다렉트
    }


}
