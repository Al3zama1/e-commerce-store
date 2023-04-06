package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity(name = "employee_role")
public class EmployeeRole {
    @EmbeddedId
    private EmployeeRoleId employeeRoleId;
}
