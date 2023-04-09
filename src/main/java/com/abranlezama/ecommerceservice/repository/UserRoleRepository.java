package com.abranlezama.ecommerceservice.repository;

import com.abranlezama.ecommerceservice.model.UserRole;
import com.abranlezama.ecommerceservice.model.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {
}
