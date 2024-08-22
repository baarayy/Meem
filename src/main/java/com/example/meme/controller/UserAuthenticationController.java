package com.example.meme.controller;

import com.example.meme.dto.UserLoginRequestDTO;
import com.example.meme.dto.UserLoginResponseDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
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
    @PostMapping("/signup")
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(@Valid @RequestBody UserRegistrationRequestDTO x){

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(x));
        } catch(ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(
            @RequestBody @Valid UserLoginRequestDTO x
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.loginUser(x));
        } catch(ConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PreAuthorize("hasRole('SUPERADMIN')")
    @Operation(summary = "Register a new  Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin is successfully Registered"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping("/admin/register")
    public ResponseEntity<UserRegistrationResponseDTO> registerAdmin(
            @Valid @RequestBody UserRegistrationRequestDTO x){

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.registerAdmin(x));
        } catch(ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
