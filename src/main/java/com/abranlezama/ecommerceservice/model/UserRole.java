package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.*;

@Entity(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;
    @Enumerated(value = EnumType.STRING)
    private UserType name;
}
