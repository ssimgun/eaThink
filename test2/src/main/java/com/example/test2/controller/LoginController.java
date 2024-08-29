package com.example.test2.controller;

import com.example.test2.entity.Users;
import com.example.test2.service.UserService;
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
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String userlogin(@RequestParam String userId, @RequestParam String password, HttpServletRequest request, Model model){
        // 사용자 인증
        Users users = userService.authenticate(userId, password);

        if(users != null){
            //user 인증 선공 시 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", users);
            log.info(session.getAttribute("loggedInUser").toString());
            model.addAttribute("loggedInUser", users);
            return "/login/home";
        }else{
            model.addAttribute("error", "일치하는 로그인 정보가 없습니다.");
            return "redirect:/login?error";

        }

    }

//        로그인 시 메인 화면 이동
    @RequestMapping("/home")
    public String home(HttpSession session, Model model){
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        log.info("home :", loggedInUser.toString());

        if(loggedInUser != null){
            model.addAttribute("loggedInUser", loggedInUser);
            return "intensify/maintest";
        }else {
            return "redirect:/main";
        }
    }

    @GetMapping ("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/main"; // 로그인 페이지로 리다렉트
    }


}
