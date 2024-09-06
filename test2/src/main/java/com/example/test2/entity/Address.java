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
                "id=" + id +
                ", address='" + address + '\'' +
                ", address_name='" + address_name + '\'' +
                ", users='" + users + '\'' +
                '}';
    }
}
