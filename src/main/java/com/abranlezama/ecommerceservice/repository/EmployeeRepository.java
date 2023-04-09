package com.abranlezama.ecommerceservice.repository;

import com.abranlezama.ecommerceservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUserId(Long userId);
}
