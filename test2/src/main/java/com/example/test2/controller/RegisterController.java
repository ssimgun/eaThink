package com.example.test2.controller;

import com.example.test2.dto.AddressForm;
import com.example.test2.dto.UsersForm;
import com.example.test2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegisterController {
//  리포지토리 가져오기
    @Autowired
    private UserService userService;

//  회원가입 버튼 클릭시 작동
    @PostMapping("/add")
    public String register(UsersForm usersform, AddressForm addressForm){
        userService.registerUser(usersform, addressForm);
        return "redirect:/loginpage";
    }

}
