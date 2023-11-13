package com.compassuol.sp.challenge.msuser.domain.dto;

import com.compassuol.sp.challenge.msuser.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String cpf;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthdate; // Date format
    private String email;
    private RoleEnum role;
    private boolean active;


}
