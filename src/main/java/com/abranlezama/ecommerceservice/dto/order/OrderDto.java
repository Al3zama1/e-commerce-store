package com.abranlezama.ecommerceservice.dto.order;

import com.abranlezama.ecommerceservice.model.OrderStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDto(
        @Positive Float total,
        @NotNull LocalDateTime datePlaced,
        LocalDateTime dateShipped,
        LocalDateTime dateDelivered,
        @NotBlank String orderStatus

) { }
