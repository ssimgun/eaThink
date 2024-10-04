package com.example.test2.dto;

import com.example.test2.entity.Address;
import com.example.test2.entity.Users;
import com.example.test2.repository.UserRepository;
import lombok.*;

//* 유저 주소 정보 저장 DTO
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class AddressForm {
    private Integer id;
    private String address;
    private String address_name;
    private Integer userId;

    private UserRepository userRepository;

    //  DTO -> Entity 변환
    public Address toEntity(Users users) {
        Address addressed = new Address();
        addressed.setAddress(this.address);
        addressed.setAddress_name(this.address_name);
        addressed.setUsers(users);

        return addressed;
    }
}

