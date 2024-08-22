package com.example.meme.dto;

import jakarta.validation.constraints.NotNull;

public record UserLoginRequestDTO(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
