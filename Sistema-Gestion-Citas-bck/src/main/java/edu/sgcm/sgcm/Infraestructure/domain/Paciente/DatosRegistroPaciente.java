package edu.sgcm.sgcm.Infraestructure.domain.Paciente;

import java.sql.Date;

public record DatosRegistroPaciente(
        Integer id,
        String nombre,
        String apellido,
        Date   fechaNacimiento,
        String genero,
        String correo,
        String telefono,
        String direccion,
        String password

){}