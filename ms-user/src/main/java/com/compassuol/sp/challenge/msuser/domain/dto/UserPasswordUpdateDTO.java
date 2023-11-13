package com.compassuol.sp.challenge.msuser.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPasswordUpdateDTO {
    @Size(min = 6)
    private String currentPassword;
    @Size(min = 6)
    private String newPassword;

}
