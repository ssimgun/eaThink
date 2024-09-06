package com.example.test2.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "surveyfoods")
public class SurveyFood {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String image_url;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FoodType type;

    public enum FoodType{
        한식, 중식, 일식, 양식
    }

}
