package com.product.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessException extends RuntimeException {
    private HttpStatus httpStatus;


}
