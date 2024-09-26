package com.example.test2.controller;

import com.example.test2.entity.Restaurant;
import com.example.test2.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/category")
    public List<Restaurant> getRestaurants(@RequestParam String name){
        return restaurantService.getRestaurantsByCategory(name);
    }
}
