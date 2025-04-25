package com.sparta.demo.web;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpartaExceptionHandler {
    @ExceptionHandler({ UsernameNotFoundException.class })
    protected ErrorResponse handleServerException(UsernameNotFoundException ex){
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getLocalizedMessage()).build();
    }

    /**
     * Validation 에서 발생하는 Exception
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions( MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream().map(err->err.getDefaultMessage()).collect(Collectors.joining(", "));
        return ErrorResponse.builder(ex, ex.getStatusCode(), errorMessage).title("request argument not valid").build();
    }
    
    /**
     * 인지하지 못한 예외에 대한 처리
     * 예외에 대해 최대한 찾아서 Exception 별로 처리를 해야함
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler({ Exception.class })
    protected ErrorResponse handleServerException(Exception ex) {
        return ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage()).build();
    }
}
