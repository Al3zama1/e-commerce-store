package com.abranlezama.ecommerceservice.repository;

import com.abranlezama.ecommerceservice.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findAllByCustomer_Id(Long customerId);
}
