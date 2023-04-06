package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "customer_order")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "date_placed", nullable = false)
    private LocalDateTime datePlaced;
    @Column(name = "order_total", nullable = false)
    private Double orderTotal;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
