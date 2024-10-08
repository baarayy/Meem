package com.example.meme.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InventoryDTO(
        Integer id,
        @PositiveOrZero
        Integer quantity
) {
}
