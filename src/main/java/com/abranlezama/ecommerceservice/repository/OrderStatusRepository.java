package com.abranlezama.ecommerceservice.repository;

import com.abranlezama.ecommerceservice.model.OrderStatus;
import com.abranlezama.ecommerceservice.model.OrderStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Short> {

    Optional<OrderStatus> findByStatus(OrderStatusType statusType);
}
