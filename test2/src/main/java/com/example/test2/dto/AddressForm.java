package com.example.test2.dto;

import com.example.test2.entity.Address;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class AddressForm {
    private Integer id;
    private String address;
    private String address_name;

    //  DTO -> Entity 변환
    public Address toEntity() {
        return new Address(this.id, this.address, this.address_name);
    }
}

