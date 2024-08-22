package com.example.meme.controller;

import com.example.meme.dto.OrderDTO;
import com.example.meme.dto.OrderResponseDTO;
import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.OrderService;
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
@Validated
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
public class OrderController {

    private final OrderService service;

    @Operation(summary = "Retrieve All orders", description = "Paginated Retrieval for all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of orders is empty"),
            @ApiResponse(responseCode = "200", description = "Successfull Retrieval of orders List")
    })
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Order By Id", description = "Retrieve a single Order by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Order isn't found"),
            @ApiResponse(responseCode = "200", description = "Order was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable Integer id) {
        var order = service.findById(id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Create a new  Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order is successfully created"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@Valid @RequestBody OrderDTO x) {
        var createdOrder = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "order isn't found"),
            @ApiResponse(responseCode = "200", description = "order was successfully Updated"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> update(@PathVariable Integer id ,@Valid @RequestBody OrderDTO x) {
        var updatedOrder = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
        } catch(IllegalArgumentException | ConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Delete order By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "order isn't found"),
            @ApiResponse(responseCode = "204", description = "order was successfully Deleted"),
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

    @Operation(summary="Find Order By User ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode="204", description="Order isn't found"),
            @ApiResponse(responseCode="200", description="Order was successfully found"),
            @ApiResponse(responseCode="400", description="Client Entered invalid id"),
            @ApiResponse(responseCode="404", description="order could not found"),
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponseDTO>> findOrdersWithUserId(@PathVariable Integer id){
        var list = service.findOrdersByUser(id);
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
