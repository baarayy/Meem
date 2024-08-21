package com.example.meme.controller;

import com.example.meme.dto.UserPaymentDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.UserPaymentService;
import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentType;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user_payments")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class UserPaymentController {

    private final UserPaymentService service;

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

    @PostMapping
    public ResponseEntity<UserPaymentDTO> create(@Valid @RequestBody  UserPaymentDTO x){
        var createdUserPayment = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserPayment);
        } catch(ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

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

    @GetMapping("/provider/{providerName}")
    public ResponseEntity<List<UserPaymentDTO>> findPaymentByProvider(@PathVariable PaymentProvider providerName){
        var list = service.findPaymentByProvider(providerName);
        if(list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/type/{typeName}")
    public ResponseEntity<List<UserPaymentDTO>> findPaymentByType(@PathVariable PaymentType typerName){
        var list = service.findPaymentByType(typerName);
        if(list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
