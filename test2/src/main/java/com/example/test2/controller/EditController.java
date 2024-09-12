package com.example.test2.controller;

import com.example.test2.dto.AddressForm;
import com.example.test2.dto.UsersForm;
import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import com.example.test2.entity.Weather_data;
import com.example.test2.repository.AddressRepository;
import com.example.test2.repository.UserRepository;
import com.example.test2.service.AddressService;
import com.example.test2.service.UserService;
import com.example.test2.service.weatherAPIConnect;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/myInfo")
public class EditController {
    @Autowired
    UserService userService;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressService addressService;
    @Autowired
    weatherAPIConnect weatherAPIConnect;

    //    회원정보 수정 페이지 이동
    @GetMapping("/edit")
    public String edit(HttpSession session, Model model) {
        Users userSession = (Users) session.getAttribute("loggedInUser");  // 세션에서 사용자 정보 가져오기
        Address selectAddress = (Address) session.getAttribute("selectAddress");

        model.addAttribute("loggedInUser", userSession);
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);

        model.addAttribute("selectAddress", selectAddress);

        if (userSession != null) {
            userService.editUser(userSession.getId(), model);  // 사용자 ID를 이용하여 모델 업데이트
            addressService.showAddressList(session, model);
            return "intensify/edit";
        } else {
            // 사용자가 세션에 없으면 로그인 페이지로 리다이렉트
            return "redirect:/login?error";
        }
    }

    //  회원정보 수정 완료 버튼 클릭시
    @PostMapping("/update")
    public String update(UsersForm usersform) {
        userService.updateUser(usersform);
        log.info("아이디값 : " + usersform.getId());

        return "redirect:/myInfo/edit";
    }

    //    주소 목록 페이지 이동
    @GetMapping("/addressList")
    public String addressList(HttpSession session, Model model) {
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);

        if (session != null){
            addressService.showAddressList(session, model);
            return "intensify/addressList";
        }
        return "intensify/maintest";

    }
//  주소 추가 버튼 클릭시 작동
    @PostMapping("/addAddress")
    public String addAddress(HttpSession session, AddressForm addressForm){
        Users userSession = (Users) session.getAttribute("loggedInUser");

        addressService.addAddress(userSession , addressForm);

        return "redirect:/myInfo/addressList";
    }
}


