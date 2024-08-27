package com.product.exception;

import com.product.common.ApiResponse;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handelMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        HashMap<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

        });
        return ApiResponse.error(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponse handelBusinessException(BusinessException ex) {
        return ApiResponse.error(ex.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(ValidationException.class)
    public void handelValidationException(ValidationException ex) {

    }
}
