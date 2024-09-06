package com.example.test2.controller;

import com.example.test2.dto.UsersForm;
import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import com.example.test2.repository.AddressRepository;
import com.example.test2.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/myInfo")
public class EditController {
    @Autowired
    UserService userService;
    @Autowired
    AddressRepository addressRepository;

    //    회원정보 수정 페이지 이동
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

    //  회원정보 수정 완료 버튼 클릭시
    @PostMapping("/update")
    public String update(UsersForm usersform){
        userService.updateUser(usersform);
        log.info("아이디값 : " + usersform.getId());

        return "redirect:/main";
    }

//    주소 목록 페이지 이동
    @GetMapping("/addressList")
    public String addressList(HttpSession session, Model model){
        Users user = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", user);
        if (user != null){
            Address address = addressRepository.findById(user.getId()).orElse(null);
            model.addAttribute("address", address);

            return "intensify/addressList";
        }
        else {
            log.info(user.toString());
            return "intensify/maintest";
        }
    }

    //  주소 추가 버튼 클릭시 작동
//    @PostMapping("/addAddress")
//    public String addAddress(AddressForm addressForm){
//        userService.addAddress(addressForm);
//
//        return "redirect:/myInfo/addressList";
//    }
}
