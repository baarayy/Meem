package com.example.meme.controller;

import com.example.meme.dto.InventoryDTO;
import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.InventoryService;
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
@RequestMapping("/api/inventories")
@Validated
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class InventoryController {

    private final InventoryService service;

    @Operation(summary = "Retrieve All inventories", description = "Paginated Retrieval for all inventories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of inventories is empty"),
            @ApiResponse(responseCode = "200", description = "Successfull Retrieval of inventories List")
    })
    @GetMapping
    public ResponseEntity<Page<InventoryDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @Operation(summary="Get Inventory By Id", description="Retrieve a single Inventory by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Inventory isn't found"),
            @ApiResponse(responseCode = "200", description = "Inventory was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
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

    @Operation(summary = "Create a new  Inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory is successfully created"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping
    public ResponseEntity<InventoryDTO> create(@Valid @RequestBody InventoryDTO x) {
        var createdInventory = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "inventory isn't found"),
            @ApiResponse(responseCode = "200", description = "inventory was successfully Updated"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body")
    })
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

    @Operation(summary = "Delete inventory By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "inventory isn't found"),
            @ApiResponse(responseCode = "204", description = "inventory was successfully Deleted"),
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
