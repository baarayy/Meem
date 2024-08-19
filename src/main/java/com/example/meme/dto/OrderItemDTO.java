package com.example.meme.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemDTO(
        Integer id,
        @NotNull
        Integer orderId,
        @NotNull
        Integer productId,
        @Positive
        Integer quantity
) {
}
