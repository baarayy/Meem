package com.example.meme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserAdressDTO(
        @NotNull
        Integer id,
        @NotNull
        Integer userId,
        @NotBlank
        String addressLine1,
        String addressLine2,
        @NotBlank
        String city,
        @NotBlank
        String postalCode,
        @NotBlank
        String country
) {
}
