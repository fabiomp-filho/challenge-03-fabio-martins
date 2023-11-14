package com.compassuol.sp.challenge.msuser.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("User not found");
    }
}
