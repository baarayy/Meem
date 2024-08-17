package com.example.meme.dto;

import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserPaymentDTO(
        @NotNull
        Integer id,
        @NotNull
        Integer userId,
        @NotNull
        PaymentType type,
        @NotNull
        PaymentProvider provider,
        @NotNull
        Integer accountNo,
        @NotNull
        LocalDate expiryDate
) {
}
