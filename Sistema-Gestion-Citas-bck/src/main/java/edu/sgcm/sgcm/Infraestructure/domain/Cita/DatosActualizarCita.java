package edu.sgcm.sgcm.Infraestructure.domain.Cita;

import edu.sgcm.sgcm.Infraestructure.domain.Medico.Medico;

import java.sql.Date;
import java.sql.Time;

public record DatosActualizarCita(
        Date fecha,
        Time hora,
        Medico id_medico
) {
}
