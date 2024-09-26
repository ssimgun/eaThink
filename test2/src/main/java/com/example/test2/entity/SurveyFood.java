package com.example.test2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "surveyfoods")
public class SurveyFood {

    @Id
    private Integer food_id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FoodType type;


    @Transient // 데이터베이스에 맵핑 되지 않도록 함
    private Integer preference; // 사용자 점수를 저장할 변수

    @Transient
    private Integer weatherScore; //날씨에 따른 추천 점수

    @Transient
    private Integer sumScore; //최종 추천 점수

    public enum FoodType {
        양식, 카페_디저트, 한식, 고기_구이, 일식_중식_세계음식, 나이트라이프
    }

}
