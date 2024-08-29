package com.example.test2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "address")
public class Address {
    @Id
    private Integer id;

    @Column(name = "address")
    private String address;

    @Column(name = "address_name")
    private String address_name;

}
