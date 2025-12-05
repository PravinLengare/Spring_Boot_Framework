package com.example.SSWIthAuthentication.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "user1")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
}
