package com.example.meme.controller;

import com.example.meme.dto.PaymentDetailDTO;
import com.example.meme.dto.PaymentDetailResponseDTO;
import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.PaymentDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment_details")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
public class PaymentDetailController {

    private final PaymentDetailService service;

    @Operation(summary = "Retrieve All payment details", description = "Paginated Retrieval for all payment details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of payment details is empty"),
            @ApiResponse(responseCode = "200", description = "Successful Retrieval of payment details List")
    })
    @GetMapping
    public ResponseEntity<Page<PaymentDetailResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Payment Detail By Id", description = "Retrieve a single Payment Detail by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Payment Detail isn't found"),
            @ApiResponse(responseCode = "200", description = "Payment Detail was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetailResponseDTO> findById(@PathVariable Integer id) {
        var paymentDetail = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(paymentDetail);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Create a new  Payment Detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment Detail is successfully created"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping
    public ResponseEntity<PaymentDetailResponseDTO> create(@Valid @RequestBody PaymentDetailDTO x) {
        var createdPaymentDetail = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPaymentDetail);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update payment detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "payment detail isn't found"),
            @ApiResponse(responseCode = "200", description = "payment detail was successfully Updated"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDetailResponseDTO> update(@PathVariable Integer id ,@Valid @RequestBody PaymentDetailDTO x) {
        var updatedPaymentDetail = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedPaymentDetail);
        } catch(IllegalArgumentException | ConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Delete payment detail By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "payment detail isn't found"),
            @ApiResponse(responseCode = "204", description = "payment detail was successfully Deleted"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        try{
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
