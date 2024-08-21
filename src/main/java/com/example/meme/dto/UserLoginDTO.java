package com.example.meme.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank
        String username,
        @NotBlank
        String Password
) {
}
