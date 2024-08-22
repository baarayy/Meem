package com.example.meme.controller;

import com.example.meme.dto.CartItemDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cartitems")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
public class CartItemController {
    private final CartItemService service;

    @Operation(summary = "Retrieve all cart items" , description = "Paginated retrieval of all cart items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204" , description = "List of cart items is empty"),
            @ApiResponse(responseCode = "200" , description = "Successful retrieval of cart items list")
    })
    @GetMapping
    public ResponseEntity<Page<CartItemDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Cart Item By Id" , description = "Retrieve a single cart item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404" , description = "Cart item cannot be found"),
            @ApiResponse(responseCode = "200" , description = "Cart item is successfully found"),
            @ApiResponse(responseCode = "400" , description = "Client entered invalid id"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CartItemDTO> findById(@PathVariable Integer id) {
        var result = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Create a New Cart Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "Cart item is successfully created"),
            @ApiResponse(responseCode = "400" , description = "Client entered invalid object body"),
    })
    @PostMapping
    public ResponseEntity<CartItemDTO> create(@Valid @RequestBody CartItemDTO x) {
        var createdItem = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Cart item was successfully updated"),
            @ApiResponse(responseCode = "404" , description = "Cart item was not found"),
            @ApiResponse(responseCode = "400" , description = "Client entered invalid id or object body"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<CartItemDTO> update(@PathVariable Integer id , @Valid @RequestBody CartItemDTO x) {
        var updatedItem = service.update(id , x);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(updatedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Delete Cart Item By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204" , description = "Cart item has been successfully deleted"),
            @ApiResponse(responseCode = "400" , description = "Client entered invalid id"),
            @ApiResponse(responseCode = "404" , description = "Cart item cannot be found"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

}
