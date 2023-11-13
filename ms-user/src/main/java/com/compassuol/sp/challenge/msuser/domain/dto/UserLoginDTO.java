package com.compassuol.sp.challenge.msuser.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserLoginDTO {
    @Email
    private String email;

    @Min(6)
    private String password;
}
