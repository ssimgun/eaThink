package com.example.test2.controller;

import com.example.test2.entity.Users;
import com.example.test2.repository.AddressRepository;
import com.example.test2.repository.UserRepository;
import com.example.test2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.management.remote.JMXAuthenticator;


@Slf4j
@Controller
public class mainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressRepository addressRepository;

//    메인 화면 이동
    @GetMapping("/main")
    public String main(HttpSession session, Model model){
        return "intensify/maintest";
    }

    @GetMapping("/maptest")
    public String maptest(HttpSession session, Model model){
        return "maptest";
    }

//    로그인 페이지로 이동
    @GetMapping("/loginpage")
    public String login(Model model){
        // 사용자 인증
        return "intensify/login";
    }

//    회원가입 페이지로 이동
    @GetMapping("/register")
    public String registerpage(Model model){

        return "intensify/register";
    }

//         회원정보 수정 페이지 이동
    @GetMapping("/edit")
    public String edit(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("loggedInUser");  // 세션에서 사용자 정보 가져오기
        model.addAttribute("loggedInUser", user);
        if (user != null) {
            userService.editUser(user.getId(), model);  // 사용자 ID를 이용하여 모델 업데이트

            return "intensify/edit";
        } else {
            // 사용자가 세션에 없으면 로그인 페이지로 리다이렉트
            return "redirect:/login?error";
        }
    }


}
