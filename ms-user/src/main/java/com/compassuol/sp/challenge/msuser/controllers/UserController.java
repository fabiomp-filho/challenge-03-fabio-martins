package com.compassuol.sp.challenge.msuser.controllers;

import com.compassuol.sp.challenge.msuser.domain.dto.*;
import com.compassuol.sp.challenge.msuser.domain.entities.User;
import com.compassuol.sp.challenge.msuser.infra.security.TokenService;
import com.compassuol.sp.challenge.msuser.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1", produces = {"application/json"})
@Tag(name = "ms-user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Authenticate an user and returns a jwt token", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucessful Login"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Unexpected Error")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new TokenDTO(token));
    }

    @Operation(summary = "Update an user by his id and returns his updated details", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucessful Update"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Unexpected Error")
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(id, userUpdateDTO));
    }

    @Operation(summary = "Update an user password by his id and returns his updated password", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucessful Update"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Unexpected Error")
    })
    @PutMapping("/users/{id}/password")
    public ResponseEntity<UserResponseDTO> updatePassword(@PathVariable Long id, @RequestBody UserPasswordUpdateDTO userPasswordUpdateDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updatePassword(id, userPasswordUpdateDTO));
    }

    @Operation(summary = "get an user details by his id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucessful"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Unexpected Error")
    })

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUser(id));
    }

    @Operation(summary = "Create an user and returns his created details", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucessful Created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Unexpected Error")
    })
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userRegistrationDTO));
    }
}
