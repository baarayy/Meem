package com.example.meme.controller;

import com.example.meme.dto.UserRegistrationRequestDTO;
import com.example.meme.dto.UserRegistrationResponseDTO;
import com.example.meme.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class UserAuthenticationController {

    private final UserService service;

    @Operation(summary="Register a new  User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User is successfully Registered"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(@Valid @RequestBody UserRegistrationRequestDTO x){

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(x));
        } catch(ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    @Operation(summary = "Register a new  Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin is successfully Registered"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping("/admin")
    public ResponseEntity<UserRegistrationResponseDTO> registerAdmin(
            @Valid @RequestBody UserRegistrationRequestDTO x){

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.registerAdmin(x));
        } catch(ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
