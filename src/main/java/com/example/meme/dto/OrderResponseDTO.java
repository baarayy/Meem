package com.example.meme.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderResponseDTO(
        Integer id,
        @NotNull
        Integer userId,
        @Positive
        Double total,
        @NotNull
        Integer paymentDetailId,
        List<Integer> orderItemIds
) {
}
