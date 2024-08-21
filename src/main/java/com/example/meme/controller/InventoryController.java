package com.example.meme.controller;

import com.example.meme.dto.InventoryDTO;
import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.InventoryService;
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
@RequestMapping("/api/inventories")
@Validated
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class InventoryController {
    private final InventoryService service;
    @GetMapping
    public ResponseEntity<Page<InventoryDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> findById(@PathVariable Integer id) {
        var Inventory = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Inventory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> create(@Valid @RequestBody InventoryDTO x) {
        var createdInventory = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> update(@PathVariable Integer id ,@Valid @RequestBody InventoryDTO x) {
        var updatedInventory = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedInventory);
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
