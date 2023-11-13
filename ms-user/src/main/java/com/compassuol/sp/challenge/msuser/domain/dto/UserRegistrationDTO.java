package com.compassuol.sp.challenge.msuser.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String cpf;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate; // ISO-8601 format
    private String email;
    private String password;
}
