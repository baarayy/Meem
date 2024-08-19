package com.example.meme.dto;

import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentDetailResponseDTO(
        Integer id,
        @NotNull
        Integer orderId,
        @Positive
        Double amount,
        @NotNull
        PaymentProvider provider,
        @NotNull
        PaymentStatus status
) {
}
