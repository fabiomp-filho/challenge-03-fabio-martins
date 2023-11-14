package com.compassuol.sp.challenge.controller;

import com.compassuol.sp.challenge.msuser.controllers.UserController;
import com.compassuol.sp.challenge.msuser.domain.dto.*;
import com.compassuol.sp.challenge.msuser.domain.entities.User;
import com.compassuol.sp.challenge.msuser.enums.RoleEnum;
import com.compassuol.sp.challenge.msuser.infra.security.TokenService;
import com.compassuol.sp.challenge.msuser.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Test
    void whenLoginWithValidCredentials_thenReturnToken() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("user@example.com", "password");
        String token = "generatedToken";

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(new User(1L, "maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com", "123456", RoleEnum.USER, true));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenService.generateToken(any(User.class))).thenReturn(token);

        ResponseEntity<TokenDTO> response = userController.login(userLoginDTO);

        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().getToken());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(any(User.class));
    }
    @Test
    void whenUpdateUser_thenReturnUpdatedUser() {
        Long userId = 1L;
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com");
        UserResponseDTO updatedUser = new UserResponseDTO(1L,"maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com",RoleEnum.USER, true);

        when(userService.updateUser(userId, userUpdateDTO)).thenReturn(updatedUser);

        ResponseEntity<UserResponseDTO> response = userController.updateUser(userId, userUpdateDTO);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }
    @Test
    void whenGetUser_thenReturnUser() {
        Long userId = 1L;
        UserResponseDTO user = new UserResponseDTO(1L,"maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com",RoleEnum.USER, true);

        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<UserResponseDTO> response = userController.getUser(userId);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
    @Test
    void whenCreateUser_thenReturnCreatedUser() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com", "123456");
        UserResponseDTO newUser = new UserResponseDTO(1L,"maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com",RoleEnum.USER, true);

        when(userService.createUser(userRegistrationDTO)).thenReturn(newUser);

        ResponseEntity<UserResponseDTO> response = userController.createUser(userRegistrationDTO);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser, response.getBody());
    }
}
