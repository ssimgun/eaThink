package com.example.test2.controller;

import com.example.test2.entity.Weather_data;
import com.example.test2.repository.Weather_dataRepository;
import com.example.test2.service.weatherAPIConnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;

/*
 * WeatherTestController - Mapping List : 날씨 데이터 컨트롤
 * 1. 정각마다 서울시 구별 날씨 정보 업데이트 : getWeather()
 */

@Slf4j
@Controller
public class WeatherTestController {
    @Autowired
    private Weather_dataRepository Weather_dataRepository;
    @Autowired
    private weatherAPIConnect weahterAPIConnect;

    // 1. 정각마다 주소지별 날씨 정보 업데이트
    // 매시각 정각에 업데이트
    @Scheduled(cron = "0 0 */1 * * *")
    public String getWeather() throws Exception {

        // DB에 등록된 주소 정보 가져오기
        List<Weather_data> weather_datas = Weather_dataRepository.findAll();

        // 날씨 API load Time
        long start_time = System.currentTimeMillis();

        for(Weather_data weather_data : weather_datas) {
            URL url = weahterAPIConnect.getWeatherURL(weather_data);
            String result = weahterAPIConnect.getWeatherDate(url);
            Weather_data weather = weahterAPIConnect.getUltraSrtFcst(result);

            weather_data.setPty(weather.getPty());
            weather_data.setReh(weather.getReh());
            weather_data.setSky(weather.getSky());
            weather_data.setRn1(weather.getRn1());
            weather_data.setT1h(weather.getT1h());

//            DB에 주소별 날씨 정보 등록
            Weather_dataRepository.save(weather_data);
        }

        log.info("날씨 데이터 업데이트 완료");
        long end_time = System.currentTimeMillis();
        log.info("러닝타임 : " + ((end_time - start_time) / 1000));

        return null;
    }

}
