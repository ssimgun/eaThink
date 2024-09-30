package com.example.test2.service;

import com.example.test2.entity.*;
import com.example.test2.repository.SurveyFoodRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuRecommendationService {
    //음식 정보를 가져오기 위한 Repository
    @Autowired
    SurveyFoodRepository surveyFoodRepository;

    @Autowired
    SurveyFoodService surveyFoodService;

    //날씨 정보를 가져오기 위한 Repository
    @Autowired
    weatherAPIConnect weatherAPIConnect;

    public List<SurveyFood> getMenuRecommendationScore(HttpSession session) {
        // 점수를 저장할 SurveyFood entity 가져오기
        List<SurveyFood> surveyFoodScore = surveyFoodRepository.findAll();
        List<SurveyFood> personalFoodScore = new ArrayList<>();


        // 유저의 선호도 점수 가져오기
        if (session.getAttribute("loggedInUser") != null) {
            Users user = (Users) session.getAttribute("loggedInUser");
            Integer userId = user.getId();

            // 유저 설문 값을 저장할 리스트

            if (userId != null) {
                List<UserFoodPreferences> userFoodPreferences = surveyFoodService.findById_UserId(userId);


                // 설문 음식 리스트에서 유저가 검사한 값이 있으면 유저 검사값 추가 아니면 '5'점
                for (SurveyFood score : surveyFoodScore) {
                    score.setPreference(5); //기본 점수 5점

                    for (UserFoodPreferences preferencess : userFoodPreferences) { //사용자가 응답한 점수가 있으면 점수 변경
                        if (score.getFood_id() != null && score.getFood_id().equals(preferencess.getSurveyFood().getFood_id())) {
                            score.setPreference(preferencess.getPreference());
                        }
                    }
                    personalFoodScore.add(score);
                }
            }
            return personalFoodScore;
        } else {
            for (SurveyFood score : surveyFoodScore) {
                score.setPreference(5); //기본 점수 5점
                personalFoodScore.add(score);
            }
            return personalFoodScore;
        }
    }


    public List<SurveyFood> getScoreByweather(HttpSession session, List<SurveyFood> personalFoodScore) {
        Weather_data weather_data = (Weather_data) session.getAttribute("weather");

        //계절구분을 위해 오늘 날짜 가져오기
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatterM = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("HH");
        String formattedDateTimeM = today.format(formatterM);
        String formattedDateTimeT = today.format(formatterT);
        List<String> brunchT = Arrays.asList("15", "16", "17");
        List<String> lunchT = Arrays.asList("09", "10", "11","12", "13","14");
        List<String> DrinkT = Arrays.asList("18", "19","20", "21", "22");


        Integer pty = Integer.parseInt(weather_data.getPty());
        Integer t1h = Integer.parseInt(weather_data.getT1h());

        //rain
        List<String> rainHigh = Arrays.asList("포장마차", "파전/모듬전/빈대떡", "삼계탕/백숙/찜닭", "철판구이/볶음밥", "중국음식", "오리훈제/구이/로스/탕", "곱창/양/대창/막창", "사케", "쌀국수");
        List<String> rainLow = Arrays.asList("유로피언레스토랑", "햄버거", "남미음식", "아이스크림", "비빔밥/돌솥밥/쌈밥", "냉면", "족발/보쌈", "카레");

        //snow
        List<String> snowHigh = Arrays.asList("삼겹살/목살", "유로피언레스토랑", "설렁탕/곰탕/도가니탕", "바", "와인", "패밀리레스토랑", "동남아음식");
        List<String> snowLow = Arrays.asList("감자탕", "냉면", "퓨전레스토랑", "아이스크림", "카레", "남미음식", "한정식", "양/대창/막창");

        //season
        List<String> springFoods = Arrays.asList("한정식", "철판구이/볶음밥", "낙지", "오리훈제/구이/로스/탕", "파전/모듬전/빈대떡", "불고기/갈비살/차돌박이", "비빔밥/돌솥밥/쌈밥");
        List<String> summerFoods = Arrays.asList("햄버거", "삼계탕/백숙/찜닭", "냉면", "아이스크림", "카레", "장어구이/꼼장어", "전복", "치킨/훈제", "오리훈제/구이/로스/탕", "포장마차");
        List<String> fallFoods = Arrays.asList("맥주/호프", "전라도음식", "브런치", "바", "설렁탕/곰탕/도가니탕", "동남아음식", "순두부");
        List<String> winterFoods = Arrays.asList("회", "해물탕/해물요리/꽃게", "쌀국수", "사케", "칵테일", "바", "씨푸드", "동남아음식", "와인", "스테이크하우스");

        //Time
        List<String> brunch = Arrays.asList("베이커리/제과점", "카페/커피숍", "컵케익", "도넛", "브런치", "간식/디저트", "카페테리아/식당", "애견카페", "북카페");
        List<String> lunch = Arrays.asList("패스트푸드", "돈가스", "한정식", "설렁탕/곰탕/도가니탕", "칼국수/국수/우동/쫄면", "찌개/전골/국/탕", "비빔밥/돌솥밥/쌈밥", "해장국/국밥", "순대국", "감자탕", "순두부");
        List<String> drinks = Arrays.asList("소주", "막걸리/동동주", "포장마차", "실내포장마차", "사케", "맥주/호프", "와인", "바", "칵테일", "호텔바");

        List<SurveyFood> finalFoodScore = new ArrayList<>();
        // 온도에 따른 가중치
        Integer point = 5;
        for (SurveyFood foodScore : personalFoodScore) {

            foodScore.setWeatherScore(foodScore.getWeatherScore() == null ? 0 : foodScore.getWeatherScore());

            if (t1h > 24) {
                if (summerFoods.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() + point);
                }
            } else if (t1h < 7) {
                if (winterFoods.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() + point);
                }
            } // 24-7 사이의 봄
            else if (formattedDateTimeM.equals("03") || formattedDateTimeM.equals("04") || formattedDateTimeM.equals("05")) {
                if (springFoods.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() + point);
                }
            }  // 24-7 사이의 가을
            else if (formattedDateTimeM.equals("09") || formattedDateTimeM.equals("10") || formattedDateTimeM.equals("11")) {
                if (fallFoods.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() + point);
                }
            }
            // 비가 오는 경우
            if (pty == 1 || pty == 2 || pty == 5) {
                if (rainHigh.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() + point);
                }
                if (rainLow.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() - point);
                }
                // 눈이 오는 경우
            } else if (pty == 3 || pty == 6 || pty == 7) {
                if (snowHigh.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() + point);
                }
                if (snowLow.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() - point);
                }
            }

            if (brunchT.contains(formattedDateTimeT)){
                if (brunch.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() + point);
                }
            }else if (lunchT.contains(formattedDateTimeT)){
                if (lunch.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() + point);
                }
            }else if (DrinkT.contains(formattedDateTimeT)){
                if (drinks.contains(foodScore.getName())) {
                    foodScore.setWeatherScore(foodScore.getWeatherScore() + point);
                }
            }

            foodScore.setSumScore(foodScore.getPreference() + foodScore.getWeatherScore());
            finalFoodScore.add(foodScore);

        }

        return finalFoodScore;
    }

    public List<SurveyFood> randomTop(List<SurveyFood> finalFoodScore) {
        // getSumScore를 기준으로 내림차순 정렬
        finalFoodScore.sort(Comparator.comparingInt(SurveyFood::getSumScore).reversed());

        List<SurveyFood> topFood = finalFoodScore.stream()
                .limit(10)
                .collect(Collectors.toList());

        Collections.shuffle(topFood);

        return  topFood;

    }

    public SurveyFood getRecommendation(List<SurveyFood> foodList) {
        if (foodList.isEmpty()) {
            return null; // 리스트가 비어있는 경우 null 반환
        }

        Random random = new Random();
        return foodList.get(random.nextInt(foodList.size())); // 랜덤으로 하나의 음식 선택
    }

}