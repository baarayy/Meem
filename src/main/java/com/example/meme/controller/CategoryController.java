package com.example.meme.controller;

import com.example.meme.dto.CategoryDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.CategoryService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Validated
@CrossOrigin("http://localhost:8080")
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = service.findAll(page,size);
        if(result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody  CategoryDTO x){
        var createdCategory = service.create(x);
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch(ConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Integer id, @Valid @RequestBody  CategoryDTO x){
        var updatedCategory = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
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

    @GetMapping("/search")
    public ResponseEntity<Page<CategoryDTO>> search(
            @RequestParam(required=false) String name,
            @RequestParam(required=false) String desc,
            @RequestParam(required=false) String productName,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size
    ){
        var result = service.search(name, desc, productName, page, size);
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
