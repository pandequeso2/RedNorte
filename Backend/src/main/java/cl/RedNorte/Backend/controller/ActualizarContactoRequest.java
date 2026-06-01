package cl.RedNorte.Backend.controller;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class ActualizarContactoRequest {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String fono;
}