package com.example.test2.service;

import com.example.test2.dto.AddressForm;
import com.example.test2.dto.UsersForm;
import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import com.example.test2.entity.Weather_data;
import com.example.test2.repository.AddressRepository;
import com.example.test2.repository.SurveyFoodRepository;
import com.example.test2.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private  AddressService addressService;

    @Autowired
    private weatherAPIConnect weatherAPIConnect;

    @Autowired
    private SurveyFoodRepository surveyFoodRepository;


//    로그인 시 회원 정보 있는지 확인
    public Users authenticate(String userId, String password){

        Users user = userRepository.findByUserIdAndPassword(userId, password);
        return user;
    }

//    회원 가입 클릭 시 DB에 회원 정보 및 주소 정보 저장
    @Transactional
    public void registerUser(UsersForm usersform, AddressForm addressForm) {
        Users users = usersform.toEntity();
        userRepository.save(users);
        log.info(users.toString());

        addressForm.setAddress_name(usersform.getAddress_name());
        addressForm.setAddress(usersform.getAddress());
        Address address = addressForm.toEntity(users);
        addressRepository.save(address);
        log.info(address.toString());

        users.getAddressList().add(address);
        userRepository.save(users);

    }

//  회원정보 수정
    @Transactional
    public void editUser(Integer id, Model model) {
        Users users = userRepository.findById(id).orElse(null);

        model.addAttribute("users", users);

        Integer genderVal = users.getGender();
        // 성별에 따라 Boolean 값 설정
        boolean isMale = (genderVal != null && genderVal == 0);
        boolean isFemale = (genderVal != null && genderVal == 1);

        // 모델에 성별 정보 추가
        model.addAttribute("isMale", isMale);
        model.addAttribute("isFemale", isFemale);

        Integer agesVal = users.getAges();
        boolean teenager = (agesVal != null && agesVal == 1);
        boolean twenties = (agesVal != null && agesVal == 2);
        boolean thirties = (agesVal != null && agesVal == 3);
        boolean forties = (agesVal != null && agesVal == 4);
        boolean fifties = (agesVal != null && agesVal == 5);
        boolean sixties = (agesVal != null && agesVal == 6);

        model.addAttribute("teenager", teenager);
        model.addAttribute("twenties", twenties);
        model.addAttribute("thirties", thirties);
        model.addAttribute("forties", forties);
        model.addAttribute("fifties", fifties);
        model.addAttribute("sixties", sixties);
    }



    @Transactional
    public void updateUser(UsersForm usersform) {
        Users usersEntity = usersform.toEntity();

        Users target = userRepository.findById(usersEntity.getId()).orElse(null);
        if (target != null) {
            userRepository.save(usersEntity);
        }
        log.info(usersEntity.toString());

        AddressForm addressform = new AddressForm();
        addressform.setId(usersEntity.getId());
        addressform.setAddress(usersEntity.getAddress());
        addressform.setAddress_name(usersform.getAddress_name());
        Address address = addressform.toEntity(usersEntity);

        Address adTarget = addressRepository.findById(usersEntity.getId()).orElse(null);
        if (adTarget != null) {
            addressRepository.save(address);
        }
        log.info(address.toString());
    }
    public boolean existsByUserId(String userId, String currentId){
        if(userId.equals(currentId)){
            return false;
        }

        return userRepository.existsByUserId(userId);
    }

    public boolean existsByNickname(String nickname, String currentNickname){
        if(nickname.equals(currentNickname)){
            return false;
        }

        return userRepository.existsByNickname(nickname);
    }

    public boolean deleteUserById(Integer Id){
        if (Id != null){
            addressRepository.deleteById(Id);
            surveyFoodRepository.deleteById(Id);
            userRepository.deleteById(Id);

            return true;
        }else {
            return false;
        }
    }

    public void updateUserAddress(HttpSession session, String address, Model model){
        Users userSession = (Users) session.getAttribute("loggedInUser");

        Users users = userRepository.findById(userSession.getId()).orElse(null);
        if(users!=null){
            users.setAddress(address);
            userRepository.save(users);
            model.addAttribute("loggedInUser",users);
        }
    }

    public boolean selectAddress(Integer AddressId, HttpSession session, Model model){
        Users userSession = (Users) session.getAttribute("loggedInUser");
        Address select = addressRepository.findById(AddressId).orElse(null);

        if(userSession != null && select != null){
            Users users = userRepository.findById(userSession.getId()).orElse(null);

            if(users != null){
                users.setAddress(select.getAddress());
                userRepository.save(users);

                List<Address> selected = users.getAddressList();
                Address address1 = selected.stream()
                        .filter(a -> a.getId().equals(AddressId))
                        .findFirst()
                        .orElse(null);

                model.addAttribute("loggedInUser",users);
                session.setAttribute("loggedInUser",users);
                session.setAttribute("selectAddress",address1);
            }

            return true;
        }else{
            return false;
        }
    }

    public void setUserSessionInfo(HttpSession session, Model model){
        // 세션에서 사용자 정보와 주소 정보 가져오기
        Users userSession = (Users) session.getAttribute("loggedInUser");
        addressService.showAddressList(session, model);

        // 날씨 정보 가져와서 모델에 추가
        Weather_data weather_data = weatherAPIConnect.getUserWeather(session, model);
        model.addAttribute("weatherData", weather_data);
    }
}
