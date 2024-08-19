package com.example.meme.dto;

import com.example.meme.utils.Role;

import java.util.List;

public record UserResponseDTO(
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
