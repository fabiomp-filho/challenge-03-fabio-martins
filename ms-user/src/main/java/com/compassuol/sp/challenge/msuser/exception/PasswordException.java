package com.compassuol.sp.challenge.msuser.exception;

public class PasswordException extends RuntimeException{
    public PasswordException() {
        super("Password size must be between 6 and 255");
    }
}
