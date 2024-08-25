package com.example.meme.controller;

import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.pageDTOs.ProductDTOPage;
import com.example.meme.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @ApiResponse(responseCode = "204", description = "List of products is empty", content = @Content),
            @ApiResponse(responseCode = "200", description = "Successful Retrieval of Product List",content = {
                    @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTOPage.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
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
            @ApiResponse(responseCode = "404", description = "Product isn't found", content = @Content),
            @ApiResponse(responseCode = "200", description = "Product was successfully Found",content = {
                    @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    public ResponseEntity<ProductDTO> findById(@PathVariable Integer id) {
        var product = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    @Operation(summary = "Create a new  Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product is successfully created",content = {
                    @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO x) {
        var createdProduct = service.create(x);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    @Operation(summary = "Update Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Product isn't found", content = @Content),
            @ApiResponse(responseCode = "200", description = "Product was successfully Updated",content = {
                    @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body",content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id ,@Valid @RequestBody ProductDTO x) {
        var updatedProduct = service.update(id, x);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    @Operation(summary = "Delete Product By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Product isn't found", content = @Content),
            @ApiResponse(responseCode = "204", description = "Product was successfully Deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
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
        if(list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
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
