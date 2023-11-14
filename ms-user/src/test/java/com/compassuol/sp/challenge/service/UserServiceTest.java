package com.compassuol.sp.challenge.service;

import com.compassuol.sp.challenge.msuser.domain.dto.UserPasswordUpdateDTO;
import com.compassuol.sp.challenge.msuser.domain.dto.UserRegistrationDTO;
import com.compassuol.sp.challenge.msuser.domain.dto.UserResponseDTO;
import com.compassuol.sp.challenge.msuser.domain.dto.UserUpdateDTO;
import com.compassuol.sp.challenge.msuser.domain.entities.User;
import com.compassuol.sp.challenge.msuser.enums.RoleEnum;
import com.compassuol.sp.challenge.msuser.exception.NotFoundException;
import com.compassuol.sp.challenge.msuser.exception.PasswordException;
import com.compassuol.sp.challenge.msuser.repositories.UserRepository;
import com.compassuol.sp.challenge.msuser.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    public void createUser_WhenValidInput_ThenReturnUser() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com", "123456");
        User savedUser = new User(1L, "maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com", "123456", RoleEnum.USER, true);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.createUser(userRegistrationDTO);

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUser_WhenUserExists_ThenReturnUpdatedUser() {
        Long userId = 1L;
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com");
        User existingUser = new User(1L, "maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com", "123456", RoleEnum.USER, true);
        User updatedUser = new User(1L, "update", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com", "123456", RoleEnum.USER, true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDTO result = userService.updateUser(userId, userUpdateDTO);

        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUser_WhenUserNotFound_ThenThrowNotFoundException() {
        Long userId = 1L;
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, userUpdateDTO));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void createUser_WhenPasswordTooShort_ThenThrowPasswordException() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("maria", "silva", "000.000.000-00", Date.valueOf("2000-08-08"), "maria@mail.com", "123456");
        userRegistrationDTO.setPassword("123"); // Senha muito curta

        assertThrows(PasswordException.class, () -> userService.createUser(userRegistrationDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenValidEmail_thenUserShouldBeFound() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        UserDetails found = userService.loadUserByUsername(email);

        assertEquals(email, found.getUsername());
    }

    @Test
    void whenEmailNotFound_thenThrowUsernameNotFoundException() {
        String email = "inexistente@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void whenValidId_thenUserShouldBeFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDTO found = userService.getUser(userId);

        assertNotNull(found);
        assertEquals(userId, found.getId());
    }

    @Test
    void whenInvalidId_thenNotFoundExceptionShouldBeThrown() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    void whenValidCredentials_thenPasswordShouldBeUpdated() {
        Long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        User user = new User();
        user.setId(userId);
        user.setPassword(currentPassword);

        UserPasswordUpdateDTO dto = new UserPasswordUpdateDTO("123456", "12345678");
        dto.setCurrentPassword(currentPassword);
        dto.setNewPassword(newPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO updatedUser = userService.updatePassword(userId, dto);

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.getId());
    }

    @Test
    void whenInvalidCurrentPassword_thenIllegalArgumentExceptionShouldBeThrown() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setPassword("currentPassword");

        UserPasswordUpdateDTO dto = new UserPasswordUpdateDTO("123456", "12345678");
        dto.setCurrentPassword("wrongPassword");
        dto.setNewPassword("newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(userId, dto));
    }

    @Test
    void whenInvalidNewPassword_thenPasswordExceptionShouldBeThrown() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setPassword("currentPassword");

        UserPasswordUpdateDTO dto = new UserPasswordUpdateDTO("123456", "12345678");
        dto.setCurrentPassword("currentPassword");
        dto.setNewPassword("short");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertThrows(PasswordException.class, () -> userService.updatePassword(userId, dto));
    }

}