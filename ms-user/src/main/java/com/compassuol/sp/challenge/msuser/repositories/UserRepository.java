package com.compassuol.sp.challenge.msuser.repositories;

import com.compassuol.sp.challenge.msuser.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
