package com.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse<T> {
    private T response;
    private String message;
    private Integer status;

    public static <T> ApiResponse ok(T response, String message, Integer status) {
        return ApiResponse
                .builder()
                .response(response)
                .message(message)
                .status(status)
                .build();
    }
}
