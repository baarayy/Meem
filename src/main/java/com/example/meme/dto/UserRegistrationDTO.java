package com.example.meme.dto;

import com.example.meme.utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record UserRegistrationDTO(
        @NotNull
        Integer id,
        @NotBlank
        String username,
        @Pattern(regexp= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message= "password must be at least 8 characters,"
                        + " one lowercase letter,"
                        + " one uppercase letter,"
                        + " one number,"
                        + " and one special character")
        String password,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email(message = "Email should be valid")
        String email,
        @Pattern(regexp="^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$")
        String phone,
        Role role,
        List<Integer> orderIds
) {
}
