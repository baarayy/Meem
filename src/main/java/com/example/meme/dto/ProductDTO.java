package com.example.meme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductDTO(
        Integer id,
        @NotBlank
        @Size(min=3,max=100,message="Product name must be between 3 and 100 characters")
        String name,
        String desc,
        @NotBlank
        @Size(max=16)
        String sku,
        @Positive(message="Product price must be positive")
        Double price,
        Integer categoryId,
        @NotNull
        Integer inventoryId,
        Integer discountId,
        List<Integer> orderItemsIds

) {
}
