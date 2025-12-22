package com.org.skytechnology.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
}