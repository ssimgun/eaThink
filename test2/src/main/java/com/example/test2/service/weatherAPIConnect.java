package com.example.test2.service;

import com.example.test2.entity.Users;
import com.example.test2.entity.Weather_data;
import com.example.test2.repository.UserRepository;
import com.example.test2.repository.Weather_dataRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

@Slf4j
@Service
public class weatherAPIConnect {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Weather_dataRepository weather_dataRepository;

    // API 요청 URL 생성 메소드 retrun => URL url
    public URL getWeatherURL(Weather_data weather_data) throws IOException {
    // return URL ex)
        // : https://apihub.kma.go.kr/api/typ02/openApi/VilageFcstInfoService_2.0/getUltraSrtFcst
        // ?pageNo=1&numOfRows=1000&dataType=json - 고정값
        // &base_date=20210628&base_time=0630&nx=55&ny=127&authKey=4xxl_HhFRSScZfx4RWUkCw - 지역/시간에 따른 변경 값
        // base_date, base_time = LocalDateTime.now()로 추출
        // nx, ny = Weather_date 에 있는 정보에서 추출

        // API url
        String apiURL = "https://apihub.kma.go.kr/api/typ02/openApi/VilageFcstInfoService_2.0/getUltraSrtFcst";

        // 요청날짜 변수
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime previousHour = now.minusHours(1);
        // 입력 값에 맞도록 날짜 데이터 형식 변경(DateTimeFormatter.ofPattern)
        // 출력 값 (ex. 날짜 : 20240912, 시간 0900)
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH00");

        String base_date = now.format(formatterDay);
        String base_time = previousHour.format(formatterHour);

        // 요청지역 변수
        String nx = weather_data.getNx();
        String ny = weather_data.getNy();

        // 기본 변수 지정
        String authKey = "4xxl_HhFRSScZfx4RWUkCw";
        String pageNo = "1";
        String numOfRows = "1000";
        String dataType = "JSON";

        // urlBuilder 생성
        StringBuilder urlBuilder = new StringBuilder(apiURL);

        urlBuilder.append("?" + URLEncoder.encode("authKey", "UTF-8") + "=" + authKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNO", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(base_date, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(base_time, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));

        //완료된 요청 url 생성 httpURLConnection 객체 활용 api 요청
        URL url = new URL(urlBuilder.toString());

        //test
//        log.info("최종 url : " + url);

        return url;
    }

    // URL 요청 메소드
    public String getWeatherDate(URL url) throws IOException{
        // 입력 받은 url 을 HttpURLConnection 으로 연결하기
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 요청 메소드 설정
        connection.setRequestMethod("GET");

        // 요청 속성 설정
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        // 응답 코드 확인
        int responseCode = connection.getResponseCode();
//        log.info("Response Code : " + responseCode);

        // 응답 코드가 성공일때 응답 데이터 읽기
        BufferedReader bufferedReader = null;

        // 응답 코드가 성공적일때
        if(responseCode == HttpURLConnection.HTTP_OK){
            // inputStreamReder를 사용하여 응답 데이터를 읽음
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        }else{
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        // 요청 결과 출력을 위한 준비
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }

        // 시스템 자원 반납 및 연결해제
        bufferedReader.close();
        connection.disconnect();

        String result = stringBuilder.toString();
//        log.info("요청결과 : " + result);
        return result;

    }

    // 요청받아서 불러온 데이터 정제 후 저장하기
    public Weather_data getUltraSrtFcst(String result) throws ParseException {
        // API 반환 결과에서 필요데이터 추출
        Weather_data weather_data = new Weather_data();

        // 가져올 시간 현재 Time 에서 시만 가져오기 (1200)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH00");
        String targetFcstTime = now.format(formatterHour);
//        log.info("FcstHour : " + targetFcstTime);

        try {
//            1. 문자열 == result
//            2. parser
            JSONParser jsonParser = new JSONParser();
//            3. To Object
            Object object = jsonParser.parse(result);
//            4. To JSONObject
            JSONObject resultObject = (JSONObject) object;

            // 필요데이터 추출
            JSONObject response = (JSONObject) resultObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray itemList = (JSONArray) items.get("item");

            // 값을 가져올 List
            JSONArray selectList = new JSONArray();
            for (int i = 0; i< itemList.size(); i++){
                // JSON 파일의 item 가져와서 필요데이터만 가져오기
                JSONObject item = (JSONObject) itemList.get(i);
                // item 의 예측시간
                String itemFcstTime = (String) item.get("fcstTime");
                // 가져올 분류 구분
                String category = (String) item.get("category");

                // 조건에 맞는 데이터로 정제
                if(targetFcstTime.equals(itemFcstTime)){
                    switch (category){
                        case "T1H" :
                            selectList.add(item);
                            weather_data.setT1h((String) item.get("fcstValue"));
                            break;
                        case "RN1" :
                            selectList.add(item);
                            weather_data.setRn1((String) item.get("fcstValue"));
                            break;
                        case "SKY" :
                            selectList.add(item);
                            weather_data.setSky((String) item.get("fcstValue"));
                            break;
                        case "REH" :
                            selectList.add(item);
                            weather_data.setReh((String) item.get("fcstValue"));
                            break;
                        case "PTY" :
                            selectList.add(item);
                            weather_data.setPty((String) item.get("fcstValue"));
                            break;
                    }

                    }
                }

//            log.info("selectList : " + selectList);
//            log.info("weather_data : " + weather_data.getSky());
//            log.info("weather_data : " + weather_data.getPty());
//            log.info("weather_data : " + weather_data.getReh());
//            log.info("weather_data : " + weather_data.getRn1());
//            log.info("weather_data : " + weather_data.getT1h());
        } catch (Exception e){
            e.printStackTrace();
        }

        return weather_data;
    }

    //session 에 있는 회원 정보의 주소를 가지고
    @GetMapping("/get-userWeather")
    public Weather_data getUserWeather(HttpSession session, Model model){
        Users users = (Users) session.getAttribute("loggedInUser");
        String second_address;
        if(users != null) {
            String[] address = users.getAddress().split(" ");
            second_address = address[1];
            log.info("회원의 주소 : " + second_address);
        } else{
            second_address = "종로구";
        }

        Weather_data weather_data = weather_dataRepository.findBySecondName(second_address);

        // sky + pty 정보에 따른 이미지 변경
        String weatherImageName = weather_data.getPty();
        String skyValue = weather_data.getSky();
        if(weatherImageName.equals("0")){
            switch (skyValue){
                case "1":
                    weatherImageName = "sun";
                    break;
                case "3":
                    weatherImageName = "weather";
                    break;
                case "4":
                    weatherImageName = "cloudy";
                    break;
            }
        }


        session.setAttribute("weatherImageName", weatherImageName);
        session.setAttribute("weather", weather_data);
        model.addAttribute("weatherImageName", weatherImageName);
        model.addAttribute("weather", weather_data);

        log.info("이미지 이름 : " + weatherImageName);
        log.info("선택된 주소 : "  + weather_data.toString());

        return weather_data;
    }
}
