package com.example.test2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "address")
    private String address;

    @Column(name = "address_name")
    private String address_name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private Users users;

    @Override
    public String toString() {
        return "address{" +
                "아이디=" + id +
                ", 주소='" + address + '\'' +
                ", 주소 별칭='" + address_name + '\'' +
                ", 유저 아이디='" + users + '\'' +
                '}';
    }
}
