package com.example.meme.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderDTO(
        Integer id,
        @NotNull
        Integer userId,
        @NotNull
        Integer paymentDetailId,
        List<Integer>orderItemIds
) {
}
