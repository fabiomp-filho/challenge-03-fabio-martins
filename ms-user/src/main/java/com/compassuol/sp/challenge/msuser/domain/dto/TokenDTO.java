package com.compassuol.sp.challenge.msuser.domain.dto;

public record TokenDTO(String token){
    public String getToken() {
        return token;
    }
}
