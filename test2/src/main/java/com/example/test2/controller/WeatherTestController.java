package com.example.test2.controller;

import com.example.test2.entity.Weather_data;
import com.example.test2.repository.Weather_dataRepository;
import com.example.test2.service.weatherAPIConnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Slf4j
@Controller
public class WeatherTestController {
    @Autowired
    private Weather_dataRepository Weather_dataRepository;

    @Autowired
    private weatherAPIConnect weahterAPIConnect;

    @Scheduled(cron = "0 0 */1 * * *")
    public String getWeather() throws Exception {

//        Weather_data weather_data = Weather_dataRepository.findById("1168000000").orElse(null);
        List<Weather_data> weather_datas = Weather_dataRepository.findAll();

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

//            log.info("weather 객체 정보 : " + weather_data.toString());

            Weather_dataRepository.save(weather_data);
//            log.info("날씨 데이터 업데이트 : " + weather_data.getID());
        }
//


        log.info("날씨 데이터 업데이트 완료");
        long end_time = System.currentTimeMillis();
        log.info("러닝타임 : " + ((end_time - start_time) / 1000));

        return null;
    }

//    @GetMapping("/weather")
//    public List<Weather_data> getWeather(@RequestBody Weather_data weather_data) {
//        long start_time = System.currentTimeMillis();
//
////      List<Weather_data> weather_data
////        api 연결을 통해 서비스 단 연결해서 데이터 가져올 부분
//
//        log.debug("날씨 조회 중");
//        long end_time = System.currentTimeMillis();
//        log.debug("러닝타임 : " + ((end_time - start_time) / 1000));
//
//        return weather_data;
//    }


}
