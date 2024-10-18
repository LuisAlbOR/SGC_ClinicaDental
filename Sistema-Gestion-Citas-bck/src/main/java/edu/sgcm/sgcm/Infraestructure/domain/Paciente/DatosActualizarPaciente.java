package edu.sgcm.sgcm.Infraestructure.domain.Paciente;

import java.sql.Date;

public record DatosActualizarPaciente(
        String nombre,
        String apellido,
        Date   fechaNacimiento,
        String correo,
        String telefono,
        String direccion

){}