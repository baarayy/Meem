package com.example.meme.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record UserShoppingSessionDTO(
        @NotNull
        Integer id,
        @NotNull
        Integer userId,
        @NotEmpty
        List<Integer>cartItemIds
) {
}
