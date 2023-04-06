package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class EmployeeRoleId implements Serializable {
    @Column(name = "employee_id")
    private Long employeeId;
    @Column(name = "role_id")
    private Long roleId;
}
