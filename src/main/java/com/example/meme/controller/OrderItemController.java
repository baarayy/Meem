package com.example.meme.controller;

import com.example.meme.dto.OrderItemDTO;
import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.models.OrderItem;
import com.example.meme.service.OrderItemService;
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
@RequestMapping("/api/orderitems")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService service;

    @GetMapping
    public ResponseEntity<Page<OrderItemDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> findById(@PathVariable Integer id) {
        var orderItem = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> create(@Valid @RequestBody OrderItemDTO x) {
        var createdProduct = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> update(@PathVariable Integer id ,@Valid @RequestBody OrderItemDTO x) {
        var updatedOrderItem = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrderItem);
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

    @GetMapping("/product/{id}")
    public ResponseEntity<List<OrderItemDTO>> findOrderDetailsWithProductId(@PathVariable Integer id){
        var list = service.findOrderDetailsForProduct(id);
        try{
            if(list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
