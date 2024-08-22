package com.example.meme.controller;

import com.example.meme.dto.UserPaymentDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.UserPaymentService;
import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user_payments")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserPaymentController {

    private final UserPaymentService service;

    @Operation(summary = "Retrieve All user payments", description = "Paginated Retrieval for all user payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of user payments is empty"),
            @ApiResponse(responseCode = "200", description = "Successful Retrieval of user payments List")
    })
    @GetMapping
    public ResponseEntity<Page<UserPaymentDTO>> findAll(
            @RequestParam(defaultValue="0")int page,
            @RequestParam (defaultValue="10")int size){
        var result = service.findAll(page, size);
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get User Payment By Id", description = "Retrieve a single User Payment by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Payment isn't found"),
            @ApiResponse(responseCode = "200", description = "User Payment was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserPaymentDTO> findById(@PathVariable Integer id){
        var userPayment = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userPayment);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Create a new  User Payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Payment is successfully created"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping
    public ResponseEntity<UserPaymentDTO> create(@Valid @RequestBody  UserPaymentDTO x){
        var createdUserPayment = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserPayment);
        } catch(ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update User Payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Payment isn't found"),
            @ApiResponse(responseCode = "200", description = "User Payment was successfully Updated"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserPaymentDTO> update(@PathVariable Integer id, @Valid @RequestBody UserPaymentDTO x){
        var updatedUserPayment = service.update(id, x);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(updatedUserPayment);
        } catch(IllegalArgumentException | ConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Delete User Payment By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Payment isn't found"),
            @ApiResponse(responseCode = "204", description = "User Payment was successfully Deleted"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get User Payment By User Id", description = "Retrieve a single User Payment by user Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Payment isn't found"),
            @ApiResponse(responseCode = "200", description = "User Payment was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<UserPaymentDTO> findPaymentByUserId(@PathVariable Integer id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findPaymentByUserId(id));
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get List of Payments By Provider Name", description = "Retrieve All User Payments by provider id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Payments With this provider"),
            @ApiResponse(responseCode = "200", description = "Payments were successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a wrong provider")
    })
    @GetMapping("/provider/{providerName}")
    public ResponseEntity<List<UserPaymentDTO>> findPaymentByProvider(@PathVariable PaymentProvider providerName){
        var list = service.findPaymentByProvider(providerName);
        if(list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @Operation(summary = "Get List of Payments By Payment Type", description = "Retrieve All User Payments by payment type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Payments With this type"),
            @ApiResponse(responseCode = "200", description = "Payments were successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a wrong type")
    })
    @GetMapping("/type/{typeName}")
    public ResponseEntity<List<UserPaymentDTO>> findPaymentByType(@PathVariable PaymentType typeName){
        var list = service.findPaymentByType(typeName);
        if(list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
