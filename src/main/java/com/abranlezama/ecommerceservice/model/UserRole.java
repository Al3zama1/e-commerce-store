package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;
    @Enumerated(value = EnumType.STRING)
    private UserType name;
    @ManyToMany(mappedBy = "employeeRoles")
    private Set<Employee> employees = new HashSet<>();
}
