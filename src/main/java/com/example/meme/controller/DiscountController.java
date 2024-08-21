package com.example.meme.controller;

import com.example.meme.dto.DiscountDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.DiscountService;
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
@Validated
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class DiscountController {
    private final DiscountService service;

    @Operation(summary = "Retrieve All discounts", description = "Paginated Retrieval for all discounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of discounts is empty"),
            @ApiResponse(responseCode = "200", description = "Successfull Retrieval of discounts List")
    })
    @GetMapping
    public ResponseEntity<Page<DiscountDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Discount By Id", description = "Retrieve a single Discount by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Discount isn't found"),
            @ApiResponse(responseCode = "200", description = "Discount was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
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

    @Operation(summary = "Create a new  Discount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Discount is successfully created"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping
    public ResponseEntity<DiscountDTO> create(@Valid @RequestBody DiscountDTO x) {
        var createdDiscount = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscount);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary="Update discount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "discount isn't found"),
            @ApiResponse(responseCode = "200", description = "discount was successfully Updated"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body")
    })
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

    @Operation(summary = "Delete discount By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "discount isn't found"),
            @ApiResponse(responseCode = "204", description = "discount was successfully Deleted"),
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
}
