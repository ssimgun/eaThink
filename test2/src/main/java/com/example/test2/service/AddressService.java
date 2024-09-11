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

@Slf4j
@Service
public class AddressService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    public void addAddress(Users userSession, AddressForm addressForm){
        Users users = userRepository.findById(userSession.getId()).orElse(null);
        Address address = addressForm.toEntity(users);
        log.info(address.toString());
        addressRepository.save(address);
    }

    public boolean deleteAddressById(Integer Id){
        Address address= addressRepository.findById(Id).orElse(null);

        if (Id != null){
            addressRepository.delete(address);
            return true;
        }else{
            return false;
        }
    }

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

    public void showAddressList(HttpSession session, Model model){
        Users usersSession = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", usersSession);

        if (usersSession != null) {
            Users users = userRepository.findById(usersSession.getId()).orElse(null);
            log.info(users.toString());
            if (users != null) {
                List<Address> addresses = users.getAddressList();

                if(!addresses.isEmpty()){
                    Integer minId = addresses.stream()
                            .map(Address::getId)
                            .min(Integer::compareTo)
                            .orElse(null);
                    addresses.forEach(address -> address.setDefault(address.getId().equals(minId)));
                }

                model.addAttribute("address", addresses);

            }
        }
    }
}
