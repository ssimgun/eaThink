package com.example.test2.controller;

import com.example.test2.entity.Restaurant;
import com.example.test2.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * RestaurantRestController - Mapping List : 음식점 목록
 * 1.해당 식당의 정보 가져오기 : /nameToRestaurant
 */


@Slf4j
@RestController
public class RestaurantRestController {
    @Autowired
    private RestaurantService restaurantService;

    // 1.  해당 식당의 정보 가져오기
    @GetMapping("/nameToRestaurant")
    public List<Restaurant> getRestaurants(@RequestParam String name) {
        List<Restaurant> restaurant = restaurantService.getRestaurantsByName(name);
        return restaurant;
    }
}
