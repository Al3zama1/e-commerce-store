package com.abranlezama.ecommerceservice.repository;

import com.abranlezama.ecommerceservice.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findAllByCustomer_User_Id(Long userId);
    Optional<CustomerOrder> findByIdAndCustomer_User_Id(long orderId, long userId);
}
