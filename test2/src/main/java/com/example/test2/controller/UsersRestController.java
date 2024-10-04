package com.example.test2.controller;

import com.example.test2.service.AddressService;
import com.example.test2.service.UserService;
import com.example.test2.service.weatherAPIConnect;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/*
 * UsersRestController - Mapping List : 회원 정보 관리
 * 1. 중복 아이디 체크 : /check-id
 * 2. 중복 닉네임 체크 : /check-nickname
 * 3. 회원 탈퇴 : /delete-user
 * 4. 주소 삭제 : /delete-address
 * 5. 주소 목록 주소 수정 : /update-address
 * 6. 대표 주소 수정 : /update-default-address
 * 7. 주소 선택 : /selectAddress
 */

@Slf4j
@RestController
public class UsersRestController {
    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;
    @Autowired
    weatherAPIConnect weatherAPIConnect;

//    1. 중복 아이디 체크
    @PostMapping("/check-id")
    public Map<String, Boolean> checkId(@RequestBody Map<String, String> payload) {
        String user_id = payload.get("userId");
        String currentId = payload.get("currentId");

        boolean exists = userService.existsByUserId(user_id, currentId);

        Map<String, Boolean> responseId = new HashMap<>();
        responseId.put("exists", exists);

        return responseId;
    }


//    2. 중복 닉네임 체크
    @PostMapping("/check-nickname")
    public Map<String, Boolean> checkNickname(@RequestBody Map<String, String> payload) {
        String nickname = payload.get("nickname");
        String currentNickname = payload.get("currentNickname");

        boolean exists = userService.existsByNickname(nickname, currentNickname);

        Map<String, Boolean> responseNickname = new HashMap<>();
        responseNickname.put("exists", exists);

        return responseNickname;
    }

//    3. 회원 탈퇴
    @PostMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestBody Map<String, Integer> payload, Model model) {
        Integer id = payload.get("Id");

        boolean deleted = userService.deleteUserById(id);

        if (deleted) {
            model.addAttribute("loggedInUser", null);
            return ResponseEntity.ok("탈퇴가 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("탈퇴 과정에서 오류가 발생했습니다.");
        }
    }

//    4. 주소 삭제
    @PostMapping("/delete-address")
    public ResponseEntity<String> deleteAddress(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("Id");
        boolean deleted = addressService.deleteAddressById(id);

        if (deleted) {
            return ResponseEntity.ok("삭제가 완료되었습니다");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 과정에서 오류가 발생했습니다");
        }
    }

//    5. 주소 목록 주소 수정
    @PostMapping("/update-address")
    public ResponseEntity<String> updateAddress(@RequestBody Map<String, String> payload, HttpSession session) {
        Integer id = Integer.valueOf(payload.get("id"));
        String address_name = payload.get("address_name");
        String address = payload.get("address");

        boolean updated = addressService.updateAddressById(id, address_name, address, session);

        if (updated) {
            return ResponseEntity.ok("수정이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 과정에 오류가 발생하였습니다,");
        }
    }

//    6. 대표 주소 수정
    @PostMapping("/update-default-address")
    public ResponseEntity<String> updateDefaultAddress(@RequestBody Map<String, String> payload, HttpSession session, Model model) {
        Integer id = Integer.valueOf(payload.get("id"));
        String address_name = payload.get("address_name");
        String address = payload.get("address");

        boolean updated = addressService.updateAddressById(id, address_name, address, session);

        if (updated) {
            userService.updateUserAddress(session, address, model);
            return ResponseEntity.ok("수정이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 과정에 오류가 발생하였습니다,");
        }
    }

//    7. 주소 선택
    @PostMapping("/selectAddress")
    public ResponseEntity<String> selectAddress(@RequestBody Map<String, Integer> payload, HttpSession session, Model model){
        Integer addressId = payload.get("id");

        boolean selected = userService.selectAddress(addressId, session, model);;

        if (selected){
            return ResponseEntity.ok("선택 완료");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("선택 과정에 오류가 발생하였습니다.");
        }
    }


}