package com.example.meme.dto;

import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public record PaymentDetailDTO(
        Integer id,
        @NotNull
        Integer orderId,
        @NotNull
        PaymentProvider provider,
        @NotNull
        PaymentStatus status
) {
}
