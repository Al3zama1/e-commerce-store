package com.abranlezama.ecommerceservice.mapstruct.mapper;

import com.abranlezama.ecommerceservice.dto.order.OrderDto;
import com.abranlezama.ecommerceservice.dto.order.OrderItemDto;
import com.abranlezama.ecommerceservice.model.CustomerOrder;
import com.abranlezama.ecommerceservice.model.OrderItem;
import com.abranlezama.ecommerceservice.model.OrderStatusType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

@Mapper
public interface OrderMapper {

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.id", target = "productId")
    OrderItemDto mapOrderItemToDto(OrderItem orderItem);

    @Mapping(source = "orderStatus.status", target = "orderStatus")
    @Mapping(source = "dateDelivered", target = "dateDelivered")
    @Mapping(source = "dateShipped", target = "dateShipped")
    OrderDto mapOrderToDto(CustomerOrder customerOrder);

    @ValueMappings({
            @ValueMapping(source = "PROCESSING", target = "Processing"),
            @ValueMapping(source = "SHIPPED", target = "Shipped"),
            @ValueMapping(source = "DELIVERED", target = "Delivered"),
            @ValueMapping(source = "CANCELLED", target = "Cancelled")
    })
    String mapOrderStatus(OrderStatusType statusType);
}
