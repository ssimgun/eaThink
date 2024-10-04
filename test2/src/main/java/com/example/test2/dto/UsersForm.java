package com.example.test2.dto;

import com.example.test2.entity.Users;
import lombok.*;

import java.time.LocalDateTime;

//* 유저 정보 저장 DTO
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UsersForm {
    private Integer id;
    private String userId;
    private String password;
    private String nickname;
    private Integer gender;
    private Integer ages;
    private String address;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    private String address_name;

    // DTO -> ENTITY 변환
    public Users toEntity() {
        // @AllArgsConstructor가 자동으로 생성자를 생성함
        Users user = new Users();
        user.setId(this.id);
        user.setUserId(this.userId);
        user.setPassword(this.password);
        user.setNickname(this.nickname);
        user.setGender(this.gender);
        user.setAges(this.ages);
        user.setAddress(this.address);
        user.setCreateDate(this.createDate);
        user.setModifiedDate(this.modifiedDate);
        return user;
    }
}
