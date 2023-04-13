package com.abranlezama.ecommerceservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "cart_id"}))
@IdClass(CartItemPK.class)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CartItem {

    @Id
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ToString.Exclude
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(nullable = false)
    private Short quantity;
}
