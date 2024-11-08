package edu.sgcm.sgcm.Infraestructure.domain.Cita;

import edu.sgcm.sgcm.Infraestructure.domain.Medico.Medico;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    Optional<Cita> findByFechaAndHoraAndPacienteAndMedico(Date fecha, Time hora, Paciente paciente, Medico medico);
    List<Cita> findByMedicoAndFecha(Medico medico, Date fecha);


    @Query("SELECT c FROM Cita c WHERE c.paciente.id_paciente = :id_paciente")
    List<Cita> findByPacienteId(@Param("id_paciente") Integer id_paciente);

    @Query("SELECT c FROM Cita c WHERE c.medico.id_medico = :id_medico")
    List<Cita> findByMedicoId(@Param("id_medico") Integer id_medico);
}

