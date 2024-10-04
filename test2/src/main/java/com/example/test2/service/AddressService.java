package com.example.test2.service;

import com.example.test2.dto.AddressForm;
import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import com.example.test2.repository.AddressRepository;
import com.example.test2.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

/*
* AddressService : 주소 제어 기능 관리
* 1. 주소 추가시 해당 사용자 아이디 주소목록에 추가 : addAddress
* 2. 해당 주소ID 값이 있는지 확인하여 삭제 : deleteAddressById
* 3. 유저와 주소값 대조 후 update : updateAddressById
* 4. 내정보 칸에 주소목록 띄어줌 : showAddressList
*/

@Slf4j
@Service
public class AddressService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    // 1. 주소 추가시 해당 사용자 아이디 주소목록에 추가
    public void addAddress(Users userSession, AddressForm addressForm){
        Users users = userRepository.findById(userSession.getId()).orElse(null);
        Address address = addressForm.toEntity(users);
        addressRepository.save(address);
    }

    // 2. 해당 주소ID 값이 있는지 확인하여 삭제
    public boolean deleteAddressById(Integer Id){
        Address address= addressRepository.findById(Id).orElse(null);

        if (Id != null){
            addressRepository.delete(address);
            return true;
        }else{
            return false;
        }
    }

    // 3. 유저와 주소값 대조 후 update
    public boolean updateAddressById(Integer id, String address_name, String address, HttpSession session){
        Users users = (Users) session.getAttribute("loggedInUser");

        if(users == null){
            return false;
        }

        Address addressRow = addressRepository.findById(id).orElse(null);

        if(addressRow != null){
            addressRow.setAddress_name(address_name);
            addressRow.setAddress(address);
            addressRepository.save(addressRow);

            return true;

        }else{
            return false;
        }

    }

    // 4. 내정보 칸에 주소목록 띄어줌
    public void showAddressList(HttpSession session, Model model){
        Users usersSession = (Users) session.getAttribute("loggedInUser");

        if (usersSession != null) {
            Users users = userRepository.findById(usersSession.getId()).orElse(null);
            if (users != null) {
                List<Address> addresses = users.getAddressList();
                Address selectAddress = addresses.stream()
                        .filter(address -> address.getAddress().equals(users.getAddress()))
                        .findFirst()
                        .orElse(null);

                if(!addresses.isEmpty()){
                    Integer minId = addresses.stream()
                            .map(Address::getId)
                            .min(Integer::compareTo)
                            .orElse(null);
                    addresses.forEach(address -> address.setDefault(address.getId().equals(minId)));
                }
                session.setAttribute("selectAddress",selectAddress);
                model.addAttribute("loggedInUser", users);
                model.addAttribute("addresses", addresses);
                model.addAttribute("selectAddress", selectAddress);

            }
        }
    }

}
