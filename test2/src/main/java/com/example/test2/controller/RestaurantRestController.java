package com.example.test2.controller;

import com.example.test2.entity.Restaurant;
import com.example.test2.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class RestaurantRestController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/nameToRestaurant")
    public List<Restaurant> getRestaurants(@RequestParam String name){
        List<Restaurant> restaurant = restaurantService.getRestaurantsByName(name);
        log.info(restaurant.toString());
        return restaurant;
    }
}
