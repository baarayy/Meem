package com.example.meme.controller;

import com.example.meme.dto.ProductDTO;
import com.example.meme.dto.SessionResponseDTO;
import com.example.meme.dto.UserShoppingSessionDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.SessionService;
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
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class SessionController {

    private final SessionService service;

    @GetMapping
    public ResponseEntity<Page<SessionResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> findById(@PathVariable Integer id) {
        var session = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(session);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<SessionResponseDTO> create(@Valid @RequestBody UserShoppingSessionDTO x) {
        var createdSession = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> update(@PathVariable Integer id ,@Valid @RequestBody UserShoppingSessionDTO x) {
        var updatedSession = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedSession);
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
    public ResponseEntity<SessionResponseDTO> findSessionByUserId(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findSessionByUserId(id));
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
