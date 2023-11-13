package com.compassuol.sp.challenge.msuser.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public ExceptionsResponse handleExceptionsBadRequest(ConstraintViolationException e) {

        List<FieldErrorRecord> details = new ArrayList<>();

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            details.add(new FieldErrorRecord(field, message));
        }

        return new ExceptionsResponse(
                400,
                HttpStatus.BAD_REQUEST.name(),
                "Invalid request",
                details
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UsernameNotFoundException.class})
    public ExceptionsResponse handleExceptionsBadRequest(UsernameNotFoundException e) {

        List<FieldErrorRecord> details = new ArrayList<>();

        return new ExceptionsResponse(
                400,
                HttpStatus.BAD_REQUEST.name(),
                "User/Password Incorrect!",
                details
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ExceptionsResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = e.getMessage();
        String field = parseFieldFromMessage(message);

        List<FieldErrorRecord> details = new ArrayList<>();
        if (field != null) {
            details.add(new FieldErrorRecord(field, field + " can't be duplicated"));
        }

        return new ExceptionsResponse(
                400,
                HttpStatus.BAD_REQUEST.name(),
                "Invalid request due to constraint violation",
                details
        );
    }

    private String parseFieldFromMessage(String message) {
        Pattern patternEmail = Pattern.compile("'([\\w.]+@[\\w.]+)'");
        Pattern patternCpf = Pattern.compile("'(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})'");

        Matcher matcherCpf = patternCpf.matcher(message);
        Matcher matcherEmail = patternEmail.matcher(message);

        if (matcherCpf.find()) {
            return "cpf";
        }
        if(matcherEmail.find()){
            return "email";
        }
        return null;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ExceptionsResponse handleExceptionsBadRequest(MethodArgumentNotValidException e){
        List<FieldErrorRecord> details = new ArrayList<>();

        for (FieldError violation : e.getBindingResult().getFieldErrors()) {
            String field = violation.getField().replace("Request", "");
            String message = violation.getDefaultMessage();
            details.add(new FieldErrorRecord(field, message));
        }

        return new ExceptionsResponse(
                400,
                HttpStatus.BAD_REQUEST.name(),
                "Invalid request",
                details
        );
    }

    // enum invalid value
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ExceptionsResponse handleBusinessException(HttpMessageNotReadableException e){
        String usefulMessage = e.getMessage().split("String")[1];
        return new ExceptionsResponse(
                400,
                HttpStatus.BAD_REQUEST.name(),
                usefulMessage,
                new ArrayList<>()
        );
    }

    // our errors
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BusinessException.class})
    public ExceptionsResponse handleBusinessException(BusinessException e){
        return new ExceptionsResponse(
                400,
                "Bad Request",
                e.getMessage(),
                new ArrayList<>()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordException.class)
    public ExceptionsResponse handleExceptionNotFound(PasswordException e){
        return new ExceptionsResponse(
                400,
                "Invalid request",
                e.getMessage(),
                new ArrayList<>()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionsResponse handleExceptionNotFound(IllegalArgumentException e){
        return new ExceptionsResponse(
                400,
                "Invalid request",
                e.getMessage(),
                new ArrayList<>()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionsResponse handleExceptionNotFound(NotFoundException e){
        return new ExceptionsResponse(
                404,
                "Not Found",
                e.getMessage(),
                new ArrayList<>()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionsResponse handleAllException(){

        return new ExceptionsResponse(
                500,
                "Internal Server Error",
                "Unexpected Error",
                new ArrayList<>()
        );
    }
}
