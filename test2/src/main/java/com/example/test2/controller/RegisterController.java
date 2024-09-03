package com.example.test2.controller;

import com.example.test2.dto.UsersForm;
import com.example.test2.entity.Users;
import com.example.test2.repository.AddressRepository;
import com.example.test2.repository.UserRepository;
import com.example.test2.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegisterController {
//  리포지토리 가져오기
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;



//  회원가입 버튼 클릭시 작동
    @PostMapping("/add")
    public String register(UsersForm usersform){
        userService.registerUser(usersform);
        return "redirect:/loginpage";
    }


//  회원정보 수정 완료 버튼 클릭시
    @PostMapping("/update")
    public String update(UsersForm usersform){
        userService.updateUser(usersform);
        log.info("아이디값 : " + usersform.getId());

//      메인으로 돌아가게 바꿔야함
        return "/home";
    }

}
