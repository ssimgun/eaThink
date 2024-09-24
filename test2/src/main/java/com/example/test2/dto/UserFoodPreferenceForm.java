package com.example.test2.dto;

import lombok.*;

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
