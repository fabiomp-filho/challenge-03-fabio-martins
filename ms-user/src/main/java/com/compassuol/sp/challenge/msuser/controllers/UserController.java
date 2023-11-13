package com.compassuol.sp.challenge.msuser.controllers;

import com.compassuol.sp.challenge.msuser.domain.dto.*;
import com.compassuol.sp.challenge.msuser.domain.entities.User;
import com.compassuol.sp.challenge.msuser.infra.security.TokenService;
import com.compassuol.sp.challenge.msuser.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new TokenDTO(token));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(id, userUpdateDTO));
    }

    @PutMapping("/users/{id}/password")
    public ResponseEntity<UserResponseDTO> updatePassword(@PathVariable Long id, @RequestBody UserPasswordUpdateDTO userPasswordUpdateDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updatePassword(id, userPasswordUpdateDTO));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUser(id));
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userRegistrationDTO));
    }
}
