package com.org.skytechnology.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String mensaje;
    private T data;
    
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> success(String mensaje, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .mensaje(mensaje)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> error(String mensaje) {
        return ApiResponse.<T>builder()
                .success(false)
                .mensaje(mensaje)
                .build();
    }
}