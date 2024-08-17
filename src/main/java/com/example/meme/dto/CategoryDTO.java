package com.example.meme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CategoryDTO(@NotNull Integer id,
                          @NotBlank
                          @Size(min=3,max=100,message="category name must be between 3 and 100 charachters") String name,
                          String desc,
                          List<Integer>productIds
                          ) {
}
