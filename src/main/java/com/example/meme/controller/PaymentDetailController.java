package com.example.meme.controller;

import com.example.meme.dto.PaymentDetailDTO;
import com.example.meme.dto.PaymentDetailResponseDTO;
import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.PaymentDetailService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment_details")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class PaymentDetailController {

    private final PaymentDetailService service;

    @GetMapping
    public ResponseEntity<Page<PaymentDetailResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

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

    @PostMapping
    public ResponseEntity<PaymentDetailResponseDTO> create(@Valid @RequestBody PaymentDetailDTO x) {
        var createdPaymentDetail = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPaymentDetail);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

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
