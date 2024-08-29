package com.example.test2.controller;

import com.example.test2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        log.info(user_id);
        boolean exists = userService.existsByUserId(user_id);

        Map<String, Boolean> responseId = new HashMap<>();
        responseId.put("exists", exists);

        return responseId;
    }

    @PostMapping("/check-nickname")
    public Map<String, Boolean> checkNickname(@RequestBody Map<String, String> payload){
        String nickname = payload.get("nickname");
        log.info(nickname);

        boolean exists = userService.existsByNickname(nickname);
        Map<String, Boolean> responseNickname = new HashMap<>();
        responseNickname.put("exists", exists);

        return responseNickname;
    }
}
