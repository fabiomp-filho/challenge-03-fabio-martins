package com.compassuol.sp.challenge.msuser.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserPasswordUpdateDTO {
    @Size(min = 6)
    private String currentPassword;
    @Size(min = 6)
    private String newPassword;

}
