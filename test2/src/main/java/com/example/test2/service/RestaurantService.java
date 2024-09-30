package com.example.test2.service;

import com.example.test2.entity.Restaurant;
import com.example.test2.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository resturantRepository;

    public List<Restaurant> getRestaurantsByName(String name){
        return resturantRepository.findByName(name);
    }
}
