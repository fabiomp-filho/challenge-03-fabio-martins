package com.compassuol.sp.challenge.msuser.services;

import com.compassuol.sp.challenge.msuser.domain.dto.UserPasswordUpdateDTO;
import com.compassuol.sp.challenge.msuser.domain.dto.UserRegistrationDTO;
import com.compassuol.sp.challenge.msuser.domain.dto.UserResponseDTO;
import com.compassuol.sp.challenge.msuser.domain.dto.UserUpdateDTO;
import com.compassuol.sp.challenge.msuser.domain.entities.User;
import com.compassuol.sp.challenge.msuser.exception.NotFoundException;
import com.compassuol.sp.challenge.msuser.exception.PasswordException;
import com.compassuol.sp.challenge.msuser.mapper.UserMapper;
import com.compassuol.sp.challenge.msuser.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(UserRegistrationDTO userRegistrationDTO) {
        if(userRegistrationDTO.getPassword().length() < 6){
            throw new PasswordException();
        }
        User user = UserMapper.toEntity(userRegistrationDTO, passwordEncoder);
        User UserSaved = userRepository.save(user);

        return UserMapper.toDto(UserSaved);
    }

    public UserResponseDTO updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException());
        User userToUpdate = UserMapper.toUpdate(userUpdateDTO, user);
        User updatedUser = userRepository.save(userToUpdate);

        return UserMapper.toDto(updatedUser);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException());

        Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(email, user.getPassword(), authorities);
    }

    public UserResponseDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        return UserMapper.toDto(user);
    }

    public UserResponseDTO updatePassword(Long id, UserPasswordUpdateDTO userPasswordUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());


        if (!passwordEncoder.matches(userPasswordUpdateDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if(userPasswordUpdateDTO.getNewPassword().length() < 6){
            throw new PasswordException();
        }

        user.setPassword(passwordEncoder.encode(userPasswordUpdateDTO.getNewPassword()));

        User updatedUser = userRepository.save(user);

        return UserMapper.toDto(updatedUser);
    }
}

