package com.example.meme.controller;

import com.example.meme.dto.DiscountDTO;
import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.DiscountService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class DiscountController {
    private final DiscountService service;

    @GetMapping
    public ResponseEntity<Page<DiscountDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> findById(@PathVariable Integer id) {
        var discount = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(discount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<DiscountDTO> create(@Valid @RequestBody DiscountDTO x) {
        var createdDiscount = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscount);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountDTO> update(@PathVariable Integer id ,@Valid @RequestBody DiscountDTO x) {
        var updatedDiscount = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedDiscount);
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
