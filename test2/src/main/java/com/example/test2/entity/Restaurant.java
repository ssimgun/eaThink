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

    @Column(name = "total_reviews")
    private long total_reviews;

    @Column(name = "positive_reviews")
    private long positive_reviews;

    @Column(name = "negative_reviews")
    private long negative_reviews;

    @Column(name = "revisit_above_3")
    private long revisit_above_3;

    @Column(name = "final_score")
    private double final_score;

    @Column(name = "tag1")
    private String tag1;

    @Column(name = "tag2")
    private String tag2;

    @Column(name = "tag3")
    private String tag3;

    @Column(name = "category")
    private String category;
}
