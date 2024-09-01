package com.example.test2.service;

import com.example.test2.dto.AddressForm;
import com.example.test2.dto.UsersForm;
import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import com.example.test2.repository.AddressRepository;
import com.example.test2.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

//    로그인 시 회원 정보 있는지 확인
    public Users authenticate(String userId, String password){

        Users user = userRepository.findByUserIdAndPassword(userId, password);
        return user;
    }

//    회원 가입 클릭 시 DB에 회원 정보 및 주소 정보 저장
    @Transactional
    public void registerUser(UsersForm usersform) {
        Users users = usersform.toEntity();
        userRepository.save(users);
        log.info(users.toString());

        AddressForm addressform = new AddressForm();
        addressform.setId(users.getId());
        addressform.setAddress(users.getAddress());
        addressform.setAddress_name(usersform.getAddress_name());
        Address address = addressform.toEntity();
        addressRepository.save(address);
        log.info(address.toString());
    }

    @Transactional
    public void editUser(@PathVariable Integer id, Model model) {
        Users users = userRepository.findById(id).orElse(null);
        Address address = addressRepository.findById(id).orElse(null);

        model.addAttribute("users", users);
        model.addAttribute("address", address);

        if (users != null) {
            Integer genderVal = users.getGender();
            // 성별에 따라 Boolean 값 설정
            boolean isMale = (genderVal != null && genderVal == 0);
            boolean isFemale = (genderVal != null && genderVal == 1);

            // 모델에 성별 정보 추가
            model.addAttribute("isMale", isMale);
            model.addAttribute("isFemale", isFemale);
        }

        if(users != null){
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
        Address address = addressform.toEntity();

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
            userRepository.deleteById(Id);
            return true;
        }else {
            return false;
        }
    }
}
