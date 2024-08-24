package com.example.meme.controller;

import com.example.meme.dto.UserAddressDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.pageDTOs.UserAddressDTOPage;
import com.example.meme.service.UserAddressService;
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

@RestController
@RequestMapping("/api/addresses")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
public class UserAddressController {

    private final UserAddressService service;

    @Operation(summary = "Retrieve All user addresses", description = "Paginated Retrieval for all user addresses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of user addresses is empty",content = @Content),
            @ApiResponse(responseCode = "200", description = "Successful Retrieval of user addresses List",content = {
                    @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserAddressDTOPage.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<UserAddressDTO>> findAll(
            @RequestParam(defaultValue="0")int page,
            @RequestParam (defaultValue="10")int size){
        var result = service.findAll(page, size);
        if(result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get User Address By Id", description = "Retrieve a single User Address by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Address isn't found",content = @Content),
            @ApiResponse(responseCode = "200", description = "User Address was successfully Found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAddressDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id",content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserAddressDTO> findById(@PathVariable Integer id){
        var userAddress = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userAddress);
    }

    @Operation(summary = "Create a new  User Address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Address is successfully created", content = { @
                    Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAddressDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Client Entered a non Valid Entity Body",content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserAddressDTO> create(@Valid @RequestBody  UserAddressDTO x){
        var createdProduct = service.create(x);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "Update User Address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Address isn't found",content = @Content),
            @ApiResponse(responseCode = "200", description = "User Address was successfully Updated", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAddressDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id Or a Non Valid Entity Body" , content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserAddressDTO> update(@PathVariable Integer id, @Valid @RequestBody  UserAddressDTO x){
        var updatedUserAddress = service.update(id, x);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUserAddress);
    }

    @Operation(summary = "Delete User Address By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Address isn't found",content = @Content),
            @ApiResponse(responseCode = "204", description = "User Address was successfully Deleted",content = @Content),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id",content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get User Address By User Id", description = "Retrieve a single User Address by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Address isn't found"),
            @ApiResponse(responseCode = "200", description = "User Address was successfully Found"),
            @ApiResponse(responseCode = "400", description = "Client Entered a Negative id")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<UserAddressDTO> findAddressByUser(@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAddressByUserId(id));
    }
}
