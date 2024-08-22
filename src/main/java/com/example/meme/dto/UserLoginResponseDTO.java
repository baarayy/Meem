package com.example.meme.dto;

import lombok.Data;

@Data
public class UserLoginResponseDTO {
    private String token;
    private long expiresIn;
}
