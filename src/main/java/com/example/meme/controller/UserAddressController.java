package com.example.meme.controller;

import com.example.meme.dto.UserAddressDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.UserAddressService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService service;

    @GetMapping
    public ResponseEntity<Page<UserAddressDTO>> findAll(
            @RequestParam(defaultValue="0")int page,
            @RequestParam (defaultValue="10")int size){
        var result = service.findAll(page, size);
        if(result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAddressDTO> findById(@PathVariable Integer id){
        var userAddress = service.findById(id);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userAddress);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<UserAddressDTO> create(@Valid @RequestBody  UserAddressDTO x){
        var createdProduct = service.create(x);
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch(ConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAddressDTO> update(@PathVariable Integer id, @Valid @RequestBody  UserAddressDTO x){
        var updatedUserAddress = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedUserAddress);
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

    @GetMapping("/user/{id}")
    public ResponseEntity<UserAddressDTO> findAddressByUser(@PathVariable Integer id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.findAddressByUserId(id));
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
