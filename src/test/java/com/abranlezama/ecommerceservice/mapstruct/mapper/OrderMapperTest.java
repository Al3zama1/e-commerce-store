package com.abranlezama.ecommerceservice.mapstruct.mapper;

import com.abranlezama.ecommerceservice.dto.order.OrderDto;
import com.abranlezama.ecommerceservice.dto.order.OrderItemDto;
import com.abranlezama.ecommerceservice.model.*;
import com.abranlezama.ecommerceservice.objectmother.CustomerMother;
import com.abranlezama.ecommerceservice.objectmother.OrderItemMother;
import com.abranlezama.ecommerceservice.objectmother.OrderMother;
import com.abranlezama.ecommerceservice.objectmother.ProductMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrderMapperTest {

    private OrderMapperImpl cut;

    @BeforeEach
    void setUp() {
        cut = new OrderMapperImpl();
    }

    @Test
    void shouldMapOrderEntityToDto() {
        Customer customer = CustomerMother.customer().build();
        OrderStatus orderStatus = new OrderStatus((short) 1, OrderStatusType.PROCESSING);
        Product product = ProductMother.saveProduct().build();
        OrderItem orderItem = OrderItemMother.orderItem()
                .product(product)
                .quantity((short) 1)
                .price(product.getPrice())
                .build();
        CustomerOrder customerOrder = OrderMother.order()
                .customer(customer)
                .orderStatus(orderStatus)
                .orderItems(List.of(orderItem))
                .total(500F)
                .build();

        // When
        OrderDto orderDto = cut.mapOrderToDto(customerOrder);

        // Then
        assertThat(orderDto.total()).isEqualTo(500);
        assertThat(orderDto.orderStatus()).isEqualTo("Processing");
    }

    @Test
    void shouldMapOrderItemEntityToDto() {
        // Given
        OrderItem orderItem = OrderItemMother.orderItem()
                .price(30F)
                .product(ProductMother.saveProduct().id(1L).build())
                .build();

        // When
        OrderItemDto orderItemDto = cut.mapOrderItemToDto(orderItem);

        // Then
        assertThat(orderItemDto.productId()).isEqualTo(1L);
        assertThat(orderItemDto.productName()).isEqualTo(orderItem.getProduct().getName());
        assertThat(orderItemDto.price()).isEqualTo(orderItem.getPrice());
        assertThat(orderItemDto.quantity()).isEqualTo(orderItem.getQuantity());
    }

    @Test
    void shouldMapOrderStatusEnumToString() {
        // Given
        // When

        // Then
        assertThat(cut.mapOrderStatus(OrderStatusType.PROCESSING)).isEqualTo("Processing");
        assertThat(cut.mapOrderStatus(OrderStatusType.SHIPPED)).isEqualTo("Shipped");
        assertThat(cut.mapOrderStatus(OrderStatusType.CANCELLED)).isEqualTo("Cancelled");
        assertThat(cut.mapOrderStatus(OrderStatusType.DELIVERED)).isEqualTo("Delivered");
    }

}
