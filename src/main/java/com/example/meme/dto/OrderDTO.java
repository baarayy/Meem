package com.example.meme.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderDTO(
        @NotNull
        Integer id,
        @NotNull
        Integer userId,
        @Positive
        double total,
        @NotNull
        Integer paymentDetailId,
        List<Integer>orderItemIds
) {
}
