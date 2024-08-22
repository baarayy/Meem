package com.example.meme.dto;

import com.example.meme.models.Role;
import com.example.meme.utils.RoleEnum;

import java.util.List;

public record UserRegistrationResponseDTO(
        Integer id,
        String username,
        String firstname,
        String lastname,
        String email,
        String phone,
        Role role,
        List<Integer>orderIds
) {
}
