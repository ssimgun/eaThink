package com.example.test2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "ages")
    private Integer ages;

    @Column(name = "address")
    private String address;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
