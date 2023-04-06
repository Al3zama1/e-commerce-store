package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.*;

@Entity(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer role_id;
    @Enumerated(value = EnumType.STRING)
    private UserType name;
}
