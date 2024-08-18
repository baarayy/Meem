package com.example.meme.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record SessionResponseDTO(
        @NotNull
        Integer id,
        @NotNull
        Integer userId,
        @Positive
        Double total,
        @NotEmpty
        List<Integer> cartItemIds
) {
}
