package com.example.test2.dto;

import com.example.test2.entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

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
    // Dto --> ENTITY 로 변환
    public Users toEntity() {
        return new Users(id, this.userId, this.password, this.nickname, this.gender, this.ages, this.address
        , createDate, modifiedDate);}
}
