package com.example.meme.controller;

import com.example.meme.dto.OrderItemDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/orderitems")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
public class OrderItemController {

    private final OrderItemService service;

    @Operation(summary = "Retrieve All order items", description = "Paginated Retrieval for all order items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of order items is empty"),
            @ApiResponse(responseCode = "200", description = "Successful Retrieval of order items List")
    })
    @GetMapping
    public ResponseEntity<Page<OrderItemDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Order Item By Id", description = "Retrieve a single Order Item by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Order Item isn't found"),
            @ApiResponse(responseCode = "200", description = "Order Item was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> findById(@PathVariable Integer id) {
        var orderItem = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Create a new  OrderItem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OrderItem is successfully created"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping
    public ResponseEntity<OrderItemDTO> create(@Valid @RequestBody OrderItemDTO x) {
        var createdProduct = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "order item isn't found"),
            @ApiResponse(responseCode = "200", description = "order item was successfully Updated"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> update(@PathVariable Integer id ,@Valid @RequestBody OrderItemDTO x) {
        var updatedOrderItem = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrderItem);
        } catch(IllegalArgumentException | ConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Delete order item By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "order item isn't found"),
            @ApiResponse(responseCode = "204", description = "order item was successfully Deleted"),
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

    @Operation(summary="Find Order Item Details With Product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Order item isn't found"),
            @ApiResponse(responseCode = "200", description = "Order item was successfully found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id"),
            @ApiResponse(responseCode = "404", description = "Order could not be found"),
    })
    @GetMapping("/product/{id}")
    public ResponseEntity<List<OrderItemDTO>> findOrderDetailsWithProductId(@PathVariable Integer id){
        var list = service.findOrderDetailsForProduct(id);
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
}
