package com.example.meme.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemDTO(
        Integer id,
        @NotNull
        Integer sessionId,
        @NotNull
        Integer productId,
        @Positive
        Integer quantity) {
}
