package edu.sgcm.sgcm.Infraestructure.domain.Administrador;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DatosRegistroAdministrador(
    Integer id,
    String nombre,
    String apellido,
    String correo,
    String password)
{}