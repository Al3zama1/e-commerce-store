package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Float total;
    private LocalDateTime datePlaced;
    private LocalDateTime dateShipped;
    private LocalDateTime dateDelivered;

    @OneToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private OrderStatus orderStatus;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderItem> orderItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerOrder that = (CustomerOrder) o;
        return Objects.equals(id, that.id) && Objects.equals(total, that.total) &&
                Objects.equals(datePlaced, that.datePlaced) && Objects.equals(dateShipped, that.dateShipped) &&
                Objects.equals(dateDelivered, that.dateDelivered) && Objects.equals(orderStatus, that.orderStatus) &&
                Objects.equals(customer, that.customer) && Objects.equals(orderItems, that.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, total, datePlaced, dateShipped, dateDelivered, orderStatus, customer, orderItems);
    }
}
