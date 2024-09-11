package com.example.test2.controller;

import com.example.test2.dto.AddressForm;
import com.example.test2.entity.Address;
import com.example.test2.repository.AddressRepository;
import com.example.test2.service.AddressService;
import com.example.test2.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UsersRestController {
    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;
    @Autowired
    AddressRepository addressRepository;

    @PostMapping("/check-id")
    public  Map<String, Boolean> checkId(@RequestBody Map<String, String> payload){
        String user_id = payload.get("userId");
        String currentId = payload.get("currentId");
        log.info(user_id);

        boolean exists = userService.existsByUserId(user_id, currentId);

        Map<String, Boolean> responseId = new HashMap<>();
        responseId.put("exists", exists);

        return responseId;
    }

    @PostMapping("/check-nickname")
    public Map<String, Boolean> checkNickname(@RequestBody Map<String, String> payload){
        String nickname = payload.get("nickname");
        String currentNickname = payload.get("currentNickname");
        log.info(nickname);

        boolean exists = userService.existsByNickname(nickname, currentNickname);

        Map<String, Boolean> responseNickname = new HashMap<>();
        responseNickname.put("exists", exists);

        return responseNickname;
    }

    @PostMapping("/delete-user")
    public  ResponseEntity<String> deleteUser(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("Id");

        boolean deleted = userService.deleteUserById(id);

        if (deleted) {
            return ResponseEntity.ok("탈퇴가 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("탈퇴 과정에서 오류가 발생했습니다.");
        }
    }

    @PostMapping("/delete-address")
    public ResponseEntity<String> deleteAddress(@RequestBody Map<String, Integer> payload){
        Integer id = payload.get("Id");
        boolean deleted = addressService.deleteAddressById(id);

        if (deleted){
            return ResponseEntity.ok("삭제가 완료되었습니다");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("탈퇴 과정에서 오류가 발생했습니다");
        }
    }

    @PostMapping("/update-address")
    public ResponseEntity<String> updateAddress(@RequestBody Map<String, String> payload, HttpSession session){
        Integer id = Integer.valueOf(payload.get("id"));
        String address_name = payload.get("address_name");
        String address = payload.get("address");

        boolean updated = addressService.updateAddressById(id, address_name, address, session);

        if(updated){
            return ResponseEntity.ok("수정이 완료되었습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 과정에 오류가 발생하였습니다,");
        }
    }
}