package com.example.meme.controller;

import com.example.meme.dto.SessionResponseDTO;
import com.example.meme.dto.UserShoppingSessionDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.service.SessionService;
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
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
public class SessionController {

    private final SessionService service;

    @Operation(summary = "Retrieve All user shopping sessions", description = "Paginated Retrieval for all user shopping sessions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of user shopping sessions is empty"),
            @ApiResponse(responseCode = "200", description = "Successful Retrieval of user shopping sessions List")
    })
    @GetMapping
    public ResponseEntity<Page<SessionResponseDTO>> findAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) {
        var result = service.findAll(page , size);
        if(result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get shopping session By Id", description = "Retrieve a single shopping session by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "shopping session isn't found"),
            @ApiResponse(responseCode = "200", description = "shopping session was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
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

    @Operation(summary = "Create a new  User Shopping Session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Shopping Session is successfully created"),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body")
    })
    @PostMapping
    public ResponseEntity<SessionResponseDTO> create(@Valid @RequestBody UserShoppingSessionDTO x) {
        var createdSession = service.create(x);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update shopping session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "shopping session isn't found"),
            @ApiResponse(responseCode = "200", description = "shopping session was successfully Updated"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> update(@PathVariable Integer id ,@Valid @RequestBody UserShoppingSessionDTO x) {
        var updatedSession = service.update(id, x);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(updatedSession);
        } catch(IllegalArgumentException | ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Delete shopping session By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "shopping session isn't found"),
            @ApiResponse(responseCode = "204", description = "shopping session was successfully Deleted"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        try{
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get Shopping Session By User Id", description = "Retrieve a single shopping session by user Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "shopping session isn't found"),
            @ApiResponse(responseCode = "200", description = "shopping session was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
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
