package com.example.test2.controller;

import com.example.test2.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
}