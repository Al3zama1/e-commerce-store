package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customer_id;
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;
    @Column(length = 50, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 50)
    private String address;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
}
