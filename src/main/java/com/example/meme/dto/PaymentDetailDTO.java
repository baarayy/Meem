package com.example.meme.dto;

import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentDetailDTO(
        @NotNull
        Integer id,
        @NotNull
        Integer orderId,
        @Positive
        Integer amount,
        @NotNull
        PaymentProvider provider,
        @NotNull
        PaymentStatus status
) {
}
