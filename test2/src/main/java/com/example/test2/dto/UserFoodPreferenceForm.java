package com.example.test2.dto;

import lombok.*;

//* 유저별 설문 정보 저장 DTO
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserFoodPreferenceForm {
//    private Integer userId;
    private Integer foodId;
    private int preference;
}
