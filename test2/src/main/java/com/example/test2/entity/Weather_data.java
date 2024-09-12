package com.example.test2.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "weather_data")
public class Weather_data {
    @Id
    private String ID;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "nx")
    private String nx;

    @Column(name = "ny")
    private String ny;

    @Column(name = "t1h")
    private String t1h;

    @Column(name = "rn1")
    private String rn1;

    @Column(name = "sky")
    private String sky;

    @Column(name = "reh")
    private String reh;

    @Column(name = "pty")
    private String pty;
}
