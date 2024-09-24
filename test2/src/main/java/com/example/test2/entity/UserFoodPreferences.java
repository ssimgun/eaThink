package com.example.test2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "user_food_preferences")
public class UserFoodPreferences {

    @EmbeddedId
    private UserFoodPreferencedId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user; //Users 엔티티와의 관계

    @ManyToOne
    @MapsId("foodId")
    @JoinColumn(name = "food_id", referencedColumnName = "food_id")
    private SurveyFood surveyFood; // SurveyFood 엔티티와의 관계

    @Column(name = "preference")
    private int preference;
}
