package com.compassuol.sp.challenge.msuser.mapper;

import com.compassuol.sp.challenge.msuser.domain.dto.UserRegistrationDTO;
import com.compassuol.sp.challenge.msuser.domain.dto.UserResponseDTO;
import com.compassuol.sp.challenge.msuser.domain.dto.UserUpdateDTO;
import com.compassuol.sp.challenge.msuser.domain.entities.User;
import com.compassuol.sp.challenge.msuser.enums.RoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static User toEntity(UserRegistrationDTO dto, PasswordEncoder passwordEncoder) {

        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .cpf(dto.getCpf())
                .birthdate(dto.getBirthdate())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .active(true)
                .role(RoleEnum.USER)
                .build();
    }

    public static UserResponseDTO toDto(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .cpf(user.getCpf())
                .birthdate(user.getBirthdate())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
    public static User toUpdate(UserUpdateDTO dto, User user){
        return User.builder()
                .id(user.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .cpf(dto.getCpf())
                .birthdate(dto.getBirthdate())
                .email(dto.getEmail())
                .password(user.getPassword())
                .active(true)
                .role(RoleEnum.USER)
                .build();
    }
}
