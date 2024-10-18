package edu.sgcm.sgcm.Infraestructure.domain.Medico;

public record DatosRegistroMedico(
         Integer id,
         String nombre,
         String apellido,
         String especialidad,
         String telefono,
         String correo,
         String password
) {
}
