package com.org.skytechnology.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistroUsuarioDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String confirmPassword;
}