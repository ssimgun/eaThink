package com.example.test2.repository;

import com.example.test2.entity.Weather_data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Weather_dataRepository extends JpaRepository<Weather_data, String> {

    Weather_data findBySecondName(String address);

}
