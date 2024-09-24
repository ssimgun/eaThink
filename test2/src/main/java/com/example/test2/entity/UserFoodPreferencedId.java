package com.example.test2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Embeddable
public class UserFoodPreferencedId implements java.io.Serializable{
    private Integer userId;
    private Integer foodId;

    // equals()와 hashCode() 메서드 구현
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof UserFoodPreferencedId)) return false;
        UserFoodPreferencedId that = (UserFoodPreferencedId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(foodId, that.foodId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(userId,foodId);
    }
}
