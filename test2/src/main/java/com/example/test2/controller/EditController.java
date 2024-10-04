package com.example.test2.controller;

import com.example.test2.dto.AddressForm;
import com.example.test2.dto.UsersForm;
import com.example.test2.entity.Users;
import com.example.test2.entity.Weather_data;
import com.example.test2.service.AddressService;
import com.example.test2.service.UserService;
import com.example.test2.service.weatherAPIConnect;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * EditController - Mapping List
 * @RequestMapping("/myInfo)
 * 1. 회원정보 수정 페이지 이동 : /edit
 * 2. 회원정보 수정 완료 버튼(업데이트) : /update
 * 3. 회원 주소 목록 페이지 이동 : /addressList
 * 4. 회원 주소 추가하기 버튼 : /addAddress
 */

@Slf4j
@Controller
@RequestMapping("/myInfo")
public class EditController {
    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;
    @Autowired
    weatherAPIConnect weatherAPIConnect;


    //   1. 회원정보 수정 페이지 이동
    @GetMapping("/edit")
    public String edit(HttpSession session, Model model) {
        // 사용자 정보 불러오기
        userService.setUserSessionInfo(session, model);
        Users userSession = (Users) session.getAttribute("loggedInUser");

        if (userSession != null) {
            // 사용자 ID를 이용하여 모델 업데이트
            userService.editUser(userSession.getId(), model);
            addressService.showAddressList(session, model);
            return "intensify/edit";
        } else {
            // 사용자가 세션에 없으면 로그인 페이지로 리다이렉트
            return "redirect:/login?error";
        }
    }


    //   2.  회원정보 수정 완료 버튼 클릭시
    @PostMapping("/update")
    public String update(UsersForm usersform) {
        userService.updateUser(usersform);

        return "redirect:/myInfo/edit";
    }


    //  3.주소 목록 페이지 이동
    @GetMapping("/addressList")
    public String addressList(HttpSession session, Model model) {
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);

        if (session != null) {
            addressService.showAddressList(session, model);
            return "intensify/addressList";
        }
        return "/main";
    }


    //  4.주소 추가 버튼 클릭시 작동
    @PostMapping("/addAddress")
    public String addAddress(HttpSession session, AddressForm addressForm) {
        Users userSession = (Users) session.getAttribute("loggedInUser");

        addressService.addAddress(userSession, addressForm);

        return "redirect:/myInfo/addressList";
    }


}


