package com.example.meme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record DiscountDTO(
        Integer id,
        @NotBlank
        String name,
        String desc,
        @Positive
        double percent,
        Boolean active,
        List<Integer> productIds
) {
}
