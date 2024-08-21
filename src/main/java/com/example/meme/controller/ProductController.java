package com.example.meme.controller;

import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.ProductService;
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

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {

    private final ProductService service;

    @Operation(summary = "Retrieve All Products", description = "Paginated Retrieval for all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of products is empty"),
            @ApiResponse(responseCode = "200", description = "Successful Retrieval of Product List")
    })
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Get Product By Id", description = "Retrieve a single product by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Product isn't found"),
            @ApiResponse(responseCode = "200", description = "Product was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Integer id) {
        var product = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Create a new  Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product is successfully created"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO x) {
        var createdProduct = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Product isn't found"),
            @ApiResponse(responseCode = "200", description = "Product was successfully Updated"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id ,@Valid @RequestBody ProductDTO x) {
        var updatedProduct = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        } catch(IllegalArgumentException | ConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Delete Product By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Product isn't found"),
            @ApiResponse(responseCode = "204", description = "Product was successfully Deleted"),
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

    @Operation(summary = "Get Products For Specific Category", description = "Retrieve a single product by category Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Products were found"),
            @ApiResponse(responseCode = "200", description = "Products are successfully Found"),
            @ApiResponse(responseCode = "204", description = "No Products in this category"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDTO>> findProductsWithCategoryId(@PathVariable Integer id){
        var list = service.findProductsWithCategoryId(id);
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

    @Operation(summary = "Search Products", description = "Paginated Retrieval for products by searching")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of products is empty"),
            @ApiResponse(responseCode = "200", description = "Successful Retrieval of Product List")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String desc,
            @RequestParam(required = false) Boolean discountStatus,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        var result = service.search(name, desc, discountStatus, categoryName, minPrice, maxPrice, page, size);
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
