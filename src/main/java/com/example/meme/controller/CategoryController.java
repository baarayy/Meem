package com.example.meme.controller;

import com.example.meme.dto.CategoryDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Retrieve All categories" , description = "Paginated retrieval of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Successful retrieval of category list"),
            @ApiResponse(responseCode = "204" , description = "List of categories is empty"),
    })
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

    @Operation(summary="Get Category By Id", description="Retrieve a single Category by Id")
    @ApiResponses(value={
            @ApiResponse(responseCode = "404", description = "Category cannot be found"),
            @ApiResponse(responseCode = "200", description = "Category was successfully found"),
            @ApiResponse(responseCode = "400", description = "Client entered invalid id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Integer id){
        var category = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(category);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @Operation(summary="Create a new  Category")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "Category is successfully created"),
            @ApiResponse(responseCode = "400", description = "Client entered invalid object body")
    })
    @PostMapping
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody  CategoryDTO x){
        var createdCategory = service.create(x);
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch(ConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary="Update category")
    @ApiResponses(value={
            @ApiResponse(responseCode = "404", description = "category cannot be found"),
            @ApiResponse(responseCode = "200", description = "category was successfully updated"),
            @ApiResponse(responseCode = "400", description = "Client entered invalid id Or object body")
    })
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
    @Operation(summary = "Delete category By Id")
    @ApiResponses(value={
            @ApiResponse(responseCode = "404", description = "category isn't found"),
            @ApiResponse(responseCode = "204", description = "category was successfully Deleted"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
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
