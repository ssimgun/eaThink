package com.example.test2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name ="number_address")
    private String number_address;

    @Column(name ="road_address")
    private String road_address;

    @Column(name ="x")
    private double x;

    @Column(name ="y")
    private double y;
}
