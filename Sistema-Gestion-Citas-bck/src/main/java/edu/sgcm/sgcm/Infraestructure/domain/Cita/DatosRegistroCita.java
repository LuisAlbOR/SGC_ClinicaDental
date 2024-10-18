package edu.sgcm.sgcm.Infraestructure.domain.Cita;

import edu.sgcm.sgcm.Infraestructure.domain.Medico.Medico;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.Paciente;

import java.sql.Date;
import java.sql.Time;

public record DatosRegistroCita(
        Integer id,
        Date fecha,
        Time hora,
        Integer id_paciente,
        Integer id_medico
) {
}
